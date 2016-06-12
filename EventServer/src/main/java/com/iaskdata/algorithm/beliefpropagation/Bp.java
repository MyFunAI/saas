package com.iaskdata.algorithm.beliefpropagation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Loppy Belief Propagation for inference on graph.
 * This code currently only supports two states (i.e., each node has two states)
 * <p>
 * The edge file, an input, that describes the graph will be read from the hard drive; this edge file can be created using {@link BpGraphCreator}.
 * The prior node beliefs, another input, will be read from a serialized float array on the hard drive; this array can be created using {@link BpGraphCreator}.
 * <p>
 * This BP assumes homophily over nodes; this assumption is expressed through the "edge compatibility function" being a diagonally dominant matrix.
 * The diagonal values can be set through {@link #sameGroupProb}.
 * <p>
 * This BP runs for a user-specified number of iterations. The node beliefs after each iteration is written to tab-separated {@code beliefsi.txt}, where i is the iteration number.
 *
 */
public final class Bp {

    private final String messagesPath;
    private final String priorsPath;

    private final float[] priors;
    private final float[] products;
    private final float[] beliefs;

    private final int iterationCount;


    // user must specify number of nodes and edges in the graph
    private final int nodeCount; // = 1413511394;
    private final long edgeCount; // = 6636600779L;

    // sameGroupProb is the value of the diagonal entries of the "edge compatibility function", diffGroupProb is that of the non-diagonal ones
    private final float sameGroupProb = 0.9f;
    private final float diffGroupProb = 1f - sameGroupProb;

    // Default unknown file's prior probability - since we only have two states, an unbiased value is 0.5
    private final float unknownProb = 0.5f;

    // bounds for all probability values - prevents numerical underflow and overflow
    private final float minProb = 0.0001f;
    private final float maxProb = 1f - minProb;

    private final File beliefOutputDirectoryPath;

    // timer for executing tasks that print read/write progress of disk-based message file
    private final java.util.Timer timer;

    private static Logger logger = Logger.getLogger(Bp.class.getName());

    public Bp(String messagesPath, String priorsPath, int nodeCount, long edgeCount, int iterationCount, String beliefOutputDirectory){

        this.messagesPath = messagesPath;
        this.priorsPath = priorsPath;
        this.nodeCount = nodeCount;
        this.edgeCount = edgeCount;
        this.iterationCount = iterationCount;
        if ( !new File(beliefOutputDirectory).exists() ){
            throw new IllegalArgumentException("Output directory ("+beliefOutputDirectory+") for beliefs does not exist.");
        } else {
            beliefOutputDirectoryPath = new File(beliefOutputDirectory);
        }

        if (this.priorsPath.equalsIgnoreCase("null")){
            priors = createRandomFloatArray( this.nodeCount );
        } else {
            priors = (float[]) Serializer.deserialize(this.priorsPath);
        }

        products = new float[nodeCount];
        beliefs = new float[nodeCount];

        timer = new java.util.Timer(true);
    }

    /**
     * Generate an array of random floats, whose values lie between 0.01 and 0.99 inclusively.
     * @param arrayLength
     * @return
     */
    private static float[] createRandomFloatArray(int arrayLength){
        float[] array = new float[arrayLength];
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < array.length; i++) {
            float value = random.nextFloat();
            while (value<0.01f || value>0.99f){
                value = random.nextFloat();
            }
            array[i] = value;
        }
        return array;
    }


    public void run(){
        System.out.println("=== Running Belief Propagation (" + iterationCount + " iterations) ===");

        try {
            FileHandler logFile = new FileHandler("bp_log.txt");
            logFile.setFormatter(new SimpleFormatter());
            logger.addHandler(logFile);
            logger.setUseParentHandlers(false);
            // === Step 0: reset messages, products
            System.out.println("Resetting messages...");

            resetMessages();


            // timer to time each iteration's duration
            Timer iterationTimer = new Timer();

//		System.out.println("\nStep 1: propagating messages... ");

            for (int iteration = 0; iteration < iterationCount; iteration++) {

                System.out.printf("%n%nIteration %d started...%n", iteration);

                resetProducts();

                iterationTimer.reset();
                iterationTimer.start();

                // === 1st pass begins: compute intermediate node products
                MemoryMappedFile messages = new MemoryMappedFile( messagesPath, MemoryMappedFile.Mode.READ_WRITE, ByteOrder.LITTLE_ENDIAN);

                // start the task that repeatedly print progress
                PrintProgressTimerTask task = new PrintProgressTimerTask(messages);
                timer.schedule(task, 0, 1000);

                for (long i = 0; i < edgeCount; i++) {
                    int sourceId = messages.getInt();
                    int targetId = messages.getInt();

                    float sdMessage = messages.getFloat();
                    float dsMessage = messages.getFloat();
                    logger.info("node " + i + " got updated!");
                    products[sourceId] = (products[sourceId] * dsMessage) /  (products[sourceId] * dsMessage + (1f-products[sourceId]) * (1f-dsMessage));
                    products[targetId] = (products[targetId] * sdMessage) /  (products[targetId] * sdMessage + (1f-products[targetId]) * (1f-sdMessage));

                    if (products[sourceId] < minProb) {
                        products[sourceId] = minProb;
                    } else if (products[sourceId] > maxProb) {
                        products[sourceId] = maxProb;
                    }

                    if (products[targetId] < minProb) {
                        products[targetId] = minProb;
                    } else if (products[targetId] > maxProb) {
                        products[targetId] = maxProb;
                    }
                }

                messages.close(true);

                task.end();
                // === 1st pass ends


                // === 2nd pass begins: compute new messages
                messages = new MemoryMappedFile( messagesPath, MemoryMappedFile.Mode.READ_WRITE, ByteOrder.LITTLE_ENDIAN);

                // start the task that repeatedly print progress
                task = new PrintProgressTimerTask(messages);
                timer.schedule(task, 0, 1000);

                float[] tempM = new float[2];

                for (long i = 0; i < edgeCount; i++) {
                    int sourceId = messages.getInt();
                    int targetId = messages.getInt();

                    messages.getFloatArrayThenRewind(tempM);

                    float sdMessage = tempM[0];
                    float dsMessage = tempM[1];

                    float newSdMessage = generateOutMessage(products[sourceId], priors[sourceId], dsMessage);
                    float newDsMessage = generateOutMessage(products[targetId], priors[targetId], sdMessage);
                    messages.putFloat( newSdMessage );
                    messages.putFloat( newDsMessage );

                    //System.out.println( sourceId + "--" + targetId + ": [" + newSdMessage + ", " + newDsMessage +"]" );
                }

                messages.close(true);

                task.end();
                // === 2nd pass ends

                System.out.printf("Iteration %d ending... Message computation took %.1fs. ", iteration, iterationTimer.seconds());


                writeBeliefs(iteration);
            }
        } catch (IOException e) {
        }
    }

    private void writeBeliefs(int iteration){

        Timer beliefComputationTimer = new Timer();
        beliefComputationTimer.start();

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(beliefOutputDirectoryPath.getCanonicalPath() + "/" + "beliefs"+iteration+".txt"));

            String newLine=System.getProperty("line.separator");

            for (int i = 0; i < nodeCount; i++) {
                beliefs[i] = multiply(priors[i], products[i]);
                out.write(i + "\t" + beliefs[i] + newLine);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < nodeCount; i++) {
            beliefs[i] = multiply(priors[i], products[i]);
        }

        System.out.printf("Belief computation took %.1fs%n", beliefComputationTimer.seconds());
    }

    private float multiply(float m0, float m1) {
        float v0 = (m0 * m1) / (m0 * m1 + (1f - m0) * (1f - m1));
        if (v0 < minProb) {
            v0 = minProb;
            return v0;
        } else if (v0 > maxProb) {
            v0 = maxProb;
            return v0;
        } else {
            return v0;
        }
    }

    private float generateOutMessage(float nodeProduct, float nodePrior, float inMessage) {

        float modifiedProd = (nodeProduct / inMessage) / (nodeProduct / inMessage + (1f - nodeProduct) / (1f - inMessage));
        if (modifiedProd < minProb) {
            modifiedProd = minProb;
        } else if (modifiedProd > maxProb) {
            modifiedProd = maxProb;

        }

        return normalize(
                nodePrior * sameGroupProb * modifiedProd + (1f - nodePrior) * diffGroupProb * (1f - modifiedProd),// P(m=good)
                nodePrior * diffGroupProb * modifiedProd + (1f - nodePrior) * sameGroupProb * (1f - modifiedProd) // P(m=bad)
        );

    }

    private void resetProducts() {
        Arrays.fill(products, unknownProb);
    }


    private void resetMessages() {

        MemoryMappedFile messages = new MemoryMappedFile( messagesPath , MemoryMappedFile.Mode.READ_WRITE, ByteOrder.LITTLE_ENDIAN);

        // start the task that repeatedly print progress
        PrintProgressTimerTask task = new PrintProgressTimerTask(messages);
        timer.schedule(task, 0, 1000);

        for (long i = 0; i < edgeCount; i++) {
            messages.advance(8);	// skip source ID (4 bytes), target ID (4 bytes)
            messages.putFloat(unknownProb);	// set source->target message as unbiased value 0.5
            messages.putFloat(unknownProb);	// set target->source message as unbiased value 0.5
        }

        messages.close(false);
        task.end();
    }

    private float normalize(float v0, float v1) {
        v0 /= (v0 + v1);
        if (v0 < minProb) {
            v0 = minProb;
            return v0;
        } else if (v0 > maxProb) {
            v0 = maxProb;
            return v0;
        } else {
            return v0;
        }
    }
}
