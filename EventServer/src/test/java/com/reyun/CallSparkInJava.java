package com.reyun;

import com.iaskdata.SparklingWater$;
import hex.tree.gbm.GBMModel;
import water.fvec.H2OFrame;

/**
 * Created by sylar on 16/6/10.
 */
public class CallSparkInJava {
    public static  void main(String args[]){
        //				SparklingWater$.MODULE$.configure("test");

        H2OFrame irisTable= SparklingWater$.MODULE$.addFile("data/iris_sparklingwater.csv", "iris_sparklingwater.csv");

        GBMModel gbmModel = SparklingWater$.MODULE$.train(irisTable);

        SparklingWater$.MODULE$.predict(gbmModel, irisTable);

    }
}
