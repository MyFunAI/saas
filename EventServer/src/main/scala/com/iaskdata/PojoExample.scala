/**
  * Created by Administrator on 2016/5/13.
  */
package com.iaskdata


import hex.genmodel.GenModel
import hex.genmodel.easy.{EasyPredictModelWrapper, RowData}
import org.apache.spark.sql.Row
import org.apache.spark.{Logging, SparkConf, SparkContext, SparkFiles}

/**
  * Simple example showing how to use pojo directly from Spark.
  */
object PojoExample extends Logging {

  def main(args: Array[String]): Unit = {
    logInfo("Starting SparkContext, SQLContext, ....")
    // Create Spark Context
    val conf = configure("Sparkling Water Droplet")
    val sc = new SparkContext(conf)

    // Register file for reading around cluster.
    // The data file is simple iris file without any header
    sc.addFile("data/iris.csv")

    logInfo(s"Loading data from ${SparkFiles.get("iris.csv")} into DataFrame")
    val testDataRdd = sc.textFile(SparkFiles.get("iris.csv"))
      .map(_.trim)
      .filter(!_.isEmpty)
      .map(_.split(","))
      .map(p => Row(f(p(0)), f(p(1)), f(p(2)), f(p(3))))

    logInfo("Loading POJO by its class name: 'gbm_02c92461_25ea_45d2_9bf9_af2fa92758d2'")
    // We use classloading here instead of referencing pojo directly.
    // In normal case, you should use here a dedicated URI classloader to load
    // POJO from given URI.
    val genModel:GenModel = Class.forName("com.iaskdata.model.gbm_02c92461_25ea_45d2_9bf9_af2fa92758d2").newInstance().asInstanceOf[GenModel]
    // Create EasyWrapper for generated model
    val easyModel = new EasyPredictModelWrapper(genModel)
    val header = genModel.getNames

    logInfo("Making prediction using loaded POJO...")
    // Now we hava pojo ready and can generate prediction for each row
    // It is a categorical model with 3 output categories
    logInfo("  - pojo type:          " + easyModel.getModelCategory)
    logInfo("  - pojo input columns: " + header.mkString(","))
    logInfo("  - pojo prediction categories: " + easyModel.getResponseDomainValues.mkString(","))

    val predictionRdd = testDataRdd.map(row => {
      val r = new RowData
      // This is simple use that header of POJO is matching
      header.indices.foreach(idx => r.put(header(idx), row.getDouble(idx).asInstanceOf[AnyRef]) )
      val prediction = easyModel.predictMultinomial(r)
      prediction
    })

    // Collect predictions and print them
    predictionRdd.collect().foreach { prediction =>
      logInfo(s"Prediction: ${prediction.label}: ${prediction.classProbabilities.mkString(",")}")
    }

    sc.stop()
  }

  /** Convert string to double.
    *
    * It replaces all non-parseable strings with Double.NaN  */
  def f(s: String): Double = {
    try {
      s.trim.toDouble
    } catch {
      case _: Throwable => Double.NaN
    }
  }

  /** Creates default Spark config */
  def configure(appName: String = "POJO in Spark example"): SparkConf = {
    val conf = new SparkConf()
      .setAppName(appName)
    conf.setIfMissing("spark.master", sys.env.getOrElse("spark.master", "local"))
    conf
  }

}
