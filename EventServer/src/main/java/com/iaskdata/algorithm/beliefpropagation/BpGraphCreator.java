package com.iaskdata.algorithm.beliefpropagation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteOrder;


public class BpGraphCreator {

    /**
     * Reads edges from delimited edge file from a text file on disk (delimiter is specified by user),
     * and stores each edge, follow by the edge's two associated (first-state) message values, in an binary file on the hard disk.
     * <p>
     * Each "line" in the binary file is of the follow format:
     * {@code [int sourceId] [int targetId] [float message_src_to_tgt(first state)] [float message_tgt_to_src(first state)]}
     * <p>
     * This code assumes each node only has two states. Therefore each message also only has two states.
     * And since the values of the two states (say, of a message) always add up to 1, we only need to store one of them;
     * this code only writes the first-state message values to the binary file.
     *
     * @param edgeFilePath - textual edge file PATH (in)
     * @param messagesFilePath - binary edge file PATH (out)
     * @param separator - delimiter (in)
     */
    public static void createBPMessages(String edgeFilePath, String messagesFilePath, String separator) {
        MemoryMappedFile out = new MemoryMappedFile(messagesFilePath, MemoryMappedFile.Mode.READ_WRITE, ByteOrder.LITTLE_ENDIAN);

        try {
            BufferedReader in = new BufferedReader(new FileReader(edgeFilePath));
            long linesRead = 0;
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;

                String[] parts = line.split(separator); //str.split("\\t");
                int sourceId = Integer.valueOf(parts[0]).intValue();
                int targetId = Integer.valueOf(parts[1]).intValue();

                // each "line" in the binary file contains the following
                out.putInt(sourceId);	// source node ID
                out.putInt(targetId);	// target node ID
                out.putFloat(0.5f);		// source->target message default value
                out.putFloat(0.5f);		// target->source message default value

                linesRead++;
//            	if (linesRead - lastLine >= 1000000){
//            		lastLine = linesRead;
//            		System.out.println( 1.0 * linesRead / edgeCount  );
//            	}
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close(true);
    }

    /**
     * Reads delimited node priors from a text file on disk, store them in an float array and serialize the array to disk.
     *
     * @param inputPath - textual prior beliefs PATH (in)
     * @param outputPath - binary prior beliefs PATH (out)
     * @param nodeCount - node count (in)
     * @param delimiter - delimiter (in)
     */
    public static void createBPPriors(String inputPath, String outputPath, int nodeCount, String delimiter) {
        float[] priors = new float[nodeCount];

        //int C1 = 8; // or 9
        float C1 = 3.5f; // or 3
        int C2 = 5;

        try {
            BufferedReader in = new BufferedReader(new FileReader(inputPath));
            long linesRead = 0;
            long lastLine = 0;
            String str;
            while ((str = in.readLine()) != null) {
                String[] parts = str.split(delimiter);
                int nodeId = Integer.valueOf(parts[0]).intValue();
                int loc = parts[1].indexOf(",");
                float nodePrior = 0.0f;
                if (loc < 0) {
                    nodePrior = Float.valueOf( parts[1] ).floatValue();
                } else {
                    String[] sub_parts = parts[1].split("\\s*,\\s*");
                    for (int i = 0; i < sub_parts.length; i++) {
                        nodePrior += Float.parseFloat(sub_parts[i]);
                    }
                    nodePrior /= sub_parts.length;
                    //nodePrior = (float)(0.5f + 0.5f / (1 + Math.pow(Math.E, -2 * C1 * nodePrior + C2)));
                    nodePrior = (float)(1.0f / (1 + Math.pow(Math.E, -C1 * nodePrior)));
                }
                priors[nodeId] = nodePrior;

                linesRead++;
                if (linesRead - lastLine >= 1000000){
                    lastLine = linesRead;
                    System.out.println( 1.0 * linesRead / nodeCount  );
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // serialize priors (array of floats) to disk
        Serializer.serialize(priors, outputPath);
    }

}
