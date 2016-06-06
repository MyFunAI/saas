package com.iaskdata.controller;

import java.util.ArrayList;
import java.util.List;


import com.iaskdata.TestScalaObject;
import com.iaskdata.TestScalaObject$;
import com.iaskdata.service.PredictService;
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

}
