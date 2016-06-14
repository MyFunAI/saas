package com.iaskdata.config;

import com.iaskdata.SparklingWater$;
import com.iaskdata.controller.*;
import hex.tree.gbm.GBMModel;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.alibaba.fastjson.JSONObject;
import com.iaskdata.interceptor.CORSInterceptor;
import com.iaskdata.render.JsonRender;
import com.iaskdata.validator.Validator;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.render.IErrorRenderFactory;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;
import org.apache.spark.h2o.H2OContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import water.fvec.H2OFrame;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * API引导式配置
 * 
 * @author jiangzhx@gmail.com
 * @date 2015年1月22日
 */
public class Config extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {


		Thread spark = new Thread() {

			@Override
			public void run() {

                H2OFrame irisTable=SparklingWater$.MODULE$.addFile("data/iris_sparklingwater.csv", "iris_sparklingwater.csv");

                GBMModel gbmModel = SparklingWater$.MODULE$.train(irisTable);

                SparklingWater$.MODULE$.predict(gbmModel, irisTable);
			}
		};
		spark.start();


		loadPropertyFile("conf.properties");
		me.setDevMode(getPropertyToBoolean("devMode", true));

		me.setMainRenderFactory(new IMainRenderFactory() {
			@Override
			public Render getRender(String view) {
				return new JsonRender();
			}

			@Override
			public String getViewExtension() {
				return null;
			}
		});

		me.setErrorRenderFactory(new IErrorRenderFactory() {

			@Override
			public Render getRender(int errorCode, String view) {
				JSONObject json = new JSONObject();
				json.put("status", errorCode);
				if (errorCode == 404) {
					json.put(
							"message",
							"You access the interface does not exist! Please refer to the document [http://game.reyun.com/restdoc]");
				} else {
					json.put("message", "System error!");
				}
				return new JsonRender(json.toString(), errorCode);
			}
		});

		Validator.load();
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/rest", RestController.class);
		me.add("/function", FunctionController.class);
        me.add("/ac", AdsClickController.class);
        me.add("/cl", ConsumerLoanController.class);
        me.add("/ec", ECommerceController.class);
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {

	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new CORSInterceptor());
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {

	}
}
