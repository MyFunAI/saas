package com.iaskdata.controller;

import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSONObject;
import com.iaskdata.TestScalaObject;
import com.iaskdata.interceptor.ContentTypeInterceptor;
import com.iaskdata.interceptor.JSONInterceptor;
import com.iaskdata.interceptor.MethodInterceptor;
import com.iaskdata.service.PredictService;
import com.iaskdata.util.Constant;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 * 功能接口
 * 
 */

public class FunctionController extends Controller {

	/**
	 * 获取系统时间
	 */
	@ActionKey("/gettime")
	public void getTime() {

		setAttr("status", 0);
		setAttr("ts", System.currentTimeMillis());
	}

	/**
	 * 模型训练接口
	 */
	@ActionKey("/train")
	public void train() {
//        TestScalaObject tso=new TestScalaObject();
//        tso.sayhello();
		setAttr("status", -1);
		setAttr("msg", "Unimplemented");
		setAttr("ts", System.currentTimeMillis());

	}

	/**
	 * 模型預測接口
	 */
	@ActionKey("/predict")
	public void predict() {
		setAttr("status", 0);
		this.getRequest().getQueryString();
		PredictService ps = new PredictService();
//		try {			
			List result = new ArrayList(); 
					//ps.predictHandle(this.getRequest().getQueryString());
			setAttr("result", result);
//		} catch (PredictException e) {
//			setAttr("status", -1);
//			setAttr("msg", "Unimplemented");
//			setAttr("ts", System.currentTimeMillis());
//			e.printStackTrace();
//		}

	}

}
