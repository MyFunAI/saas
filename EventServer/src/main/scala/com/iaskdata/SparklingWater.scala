/**
  * Created by Administrator on 2016/5/13.
  */
package com.iaskdata

import java.io.File

import hex.tree.gbm.GBMModel.GBMParameters
import hex.tree.gbm.{GBM, GBMModel}
import org.apache.spark.h2o.{H2OContext, StringHolder}
import org.apache.spark.{SparkConf, SparkContext, SparkFiles}
import water.fvec.H2OFrame

/**
  * Example of Sparkling Water based application.
  */

class SparklingWater {


}

object SparklingWater {

  val conf = configure("Sparkling Water Droplet")
  val sc = new SparkContext(conf)
  // Create H2O Context
  val h2oContext = new H2OContext(sc).start()

  def addFile(filePath: String, fileName: String): H2OFrame = {
    sc.addFile(filePath)
    val irisTable = new H2OFrame(new File(SparkFiles.get(fileName)))

    irisTable
  }

  // Build GBM model
  def train(irisTable: H2OFrame): GBMModel = {
    import h2oContext.implicits._
    val gbmParams = new GBMParameters()
    gbmParams._train = irisTable
    gbmParams._response_column = 'class
    gbmParams._ntrees = 5

    val gbm = new GBM(gbmParams)
    val gbmModel = gbm.trainModel.get
    gbmModel
  }

  def dump(gbmModel: GBMModel): Unit = {
    val destFile = new File("data/GbmModel.java")
    val fos = new java.io.FileOutputStream(destFile)
    val writer = new gbmModel.JavaModelStreamWriter(false)
    try {
      writer.writeTo(fos)
    } finally {
      fos.close()
    }
  }

  def predict(gbmModel: GBMModel, irisTable: H2OFrame): Unit = {
    import h2oContext.implicits._
    // Make prediction on train data
    val predict = gbmModel.score(irisTable)('predict)

    // Compute number of mispredictions with help of Spark API
    val trainRDD = h2oContext.asRDD[StringHolder](irisTable('class))
    val predictRDD = h2oContext.asRDD[StringHolder](predict)

    // Make sure that both RDDs has the same number of elements
    assert(trainRDD.count() == predictRDD.count)
    val numMispredictions = trainRDD.zip(predictRDD).filter(i => {
      val act = i._1
      val pred = i._2
      act.result != pred.result
    }).collect()

    val numPredictions = trainRDD.zip(predictRDD).collect()

    println(
      s"""
         |Number of Predictions: ${numPredictions.length}
         |Number of mispredictions: ${numMispredictions.length}
         |
         |Mispredictions:
         |
         |actual X predicted
         |------------------
         |${numMispredictions.map(i => i._1.result.get + " X " + i._2.result.get).mkString("\n")}
       """.stripMargin)

  }

  def main(args: Array[String]) {

    val irisTable = this.addFile("data/iris_sparklingwater.csv", "iris_sparklingwater.csv")
//
//    val tmp =this.addFile("data/iris_sparklingwater.csv", "iris_sparklingwater.csv");
//    val xx = irisTable.add(tmp)

    val gbmModel = this.train(irisTable)

    this.predict(gbmModel, irisTable)

    // Shutdown application
    sc.stop()
  }

  def configure(appName: String = "Sparkling Water Demo"): SparkConf = {
    val conf = new SparkConf()
      .setAppName(appName)
    conf.setIfMissing("spark.master", sys.env.getOrElse("spark.master", "local"))
    conf
  }
}
