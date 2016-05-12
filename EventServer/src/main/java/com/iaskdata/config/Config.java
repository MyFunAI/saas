package com.iaskdata.config;

import com.alibaba.fastjson.JSONObject;
import com.iaskdata.controller.FunctionController;
import com.iaskdata.controller.RestController;
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
