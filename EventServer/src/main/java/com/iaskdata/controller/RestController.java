package com.iaskdata.controller;

import com.alibaba.fastjson.JSONObject;
import com.iaskdata.interceptor.ContentTypeInterceptor;
import com.iaskdata.interceptor.JSONInterceptor;
import com.iaskdata.interceptor.MethodInterceptor;
import com.iaskdata.service.RestService;
import com.iaskdata.util.Constant;
import com.iaskdata.util.Constant.Method;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * REST接口
 * 
 * @author jiangzhx@gmail.com
 * @date 2015年7月16日
 */
@Before({ ContentTypeInterceptor.class, MethodInterceptor.class,
		JSONInterceptor.class })
public class RestController extends Controller {

	private static final RestService restService = new RestService();

	/**
	 * 游戏接口
	 */
	public void index() {
		process(Method.rest);
	}

	private void process(Method method) {
		String what = getPara();
		JSONObject jsonData = getAttr(Constant.TRANSFER);
		removeAttr(Constant.TRANSFER);

		String ip = getAttr("ip");
		removeAttr("ip");

		switch (method) {
		case rest:
			setAttrs(restService.restHandler(jsonData, what, ip, true));
			break;
		}
	}
}
