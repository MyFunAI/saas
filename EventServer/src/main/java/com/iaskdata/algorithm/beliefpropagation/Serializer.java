package com.iaskdata.algorithm.beliefpropagation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


/**
 * For serializing and deserializing any Java objects.
 * <p>
 * This class is independent of the Belief Propagation algorithm.
 *
 */
public class Serializer {

    public static void serialize(Object object, String serializedObjectPath){
        try {
            // Serialize to a file
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(serializedObjectPath));
            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(String serializedObjectPath){
        Object o = null;

        try {
            // Deserialize from a file
            File file = new File(serializedObjectPath);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            // Deserialize the object
            o = in.readObject();
            in.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return o;
    }
}
