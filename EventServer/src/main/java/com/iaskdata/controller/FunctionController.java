package com.iaskdata.controller;

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
		setAttr("status", -1);
		setAttr("msg", "Unimplemented");
		setAttr("ts", System.currentTimeMillis());

	}

	/**
	 * 模型預測接口
	 */
	@ActionKey("/predict")
	public void predict() {
		setAttr("status", -1);
		setAttr("msg", "Unimplemented");
		setAttr("ts", System.currentTimeMillis());
	}

}
