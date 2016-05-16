/**
  * Created by Administrator on 2016/5/13.
  */
package com.iaskdata

import java.io.File

import hex.tree.gbm.GBM
import hex.tree.gbm.GBMModel.GBMParameters
import org.apache.spark.h2o.{StringHolder, H2OContext}
import org.apache.spark.{SparkFiles, SparkContext, SparkConf}
import water.fvec.H2OFrame

/**
  * Example of Sparkling Water based application.
  */
object SparklingWaterDroplet {

  def main(args: Array[String]) {

    // Create Spark Context
    val conf = configure("Sparkling Water Droplet")
    val sc = new SparkContext(conf)

    // Create H2O Context
    val h2oContext = new H2OContext(sc).start()
    import h2oContext.implicits._

    // Register file to be available on all nodes
//    sc.addFile(new File("data/iris_sparklingwaterdroplet.csv").getAbsolutePath)
    sc.addFile("data/iris_sparklingwaterdroplet.csv")

    // Load data and parse it via h2o parser
    val irisTable = new H2OFrame(new File(SparkFiles.get("iris_sparklingwaterdroplet.csv")))

    // Build GBM model
    val gbmParams = new GBMParameters()
    gbmParams._train = irisTable
    gbmParams._response_column = 'class
    gbmParams._ntrees = 5

    val gbm = new GBM(gbmParams)
    val gbmModel = gbm.trainModel.get

    //genmodel start
    import water._
    import _root_.hex._
    import java.net.URI
    import water.serial.ObjectTreeBinarySerializer
    val destFile = new File("data/GbmModel.java")
    val fos = new java.io.FileOutputStream(destFile)
    val writer = new gbmModel.JavaModelStreamWriter(false)
    try {
      writer.writeTo(fos)
    } finally {
      fos.close()
    }

    //--genmodel end


    // Make prediction on train data
    val predict = gbmModel.score(irisTable)('predict)

    // Compute number of mispredictions with help of Spark API
    val trainRDD = h2oContext.asRDD[StringHolder](irisTable('class))
    val predictRDD = h2oContext.asRDD[StringHolder](predict)

    // Make sure that both RDDs has the same number of elements
    assert(trainRDD.count() == predictRDD.count)
    val numMispredictions = trainRDD.zip(predictRDD).filter( i => {
      val act = i._1
      val pred = i._2
      act.result != pred.result
    }).collect()

    println(
      s"""
         |Number of mispredictions: ${numMispredictions.length}
         |
         |Mispredictions:
         |
         |actual X predicted
         |------------------
         |${numMispredictions.map(i => i._1.result.get + " X " + i._2.result.get).mkString("\n")}
       """.stripMargin)

    // Shutdown application
    sc.stop()
  }

  def configure(appName:String = "Sparkling Water Demo"):SparkConf = {
    val conf = new SparkConf()
      .setAppName(appName)
    conf.setIfMissing("spark.master", sys.env.getOrElse("spark.master", "local"))
    conf
  }
}
