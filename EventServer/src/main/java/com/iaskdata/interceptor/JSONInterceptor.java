package com.iaskdata.interceptor;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.iaskdata.util.Constant;
import com.iaskdata.util.ValidateUtil;
import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.Controller;

/**
 * json格式验证拦截器
 * @author jiangzhx@gmail.com
 * @date 2015年7月15日
 */
public class JSONInterceptor extends PrototypeInterceptor {
	
	@Override
	public void doIntercept(Invocation invocation) {
	    Controller controller = null;
		try {
		    controller = invocation.getController();
			
		    BufferedReader reader = controller.getRequest().getReader();
			
		    StringBuffer sb = new StringBuffer();
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			
			JSONObject data = JSONObject.parseObject(sb.toString());
			
			if (ValidateUtil.isValid(data)) {
			    controller.setAttr(Constant.TRANSFER, data);
			    controller.setAttr("ip", getIpAddr(controller.getRequest()));
	            invocation.invoke();
			} else {
			    controller.setAttr("status", -1);
	            controller.setAttr("message", "The data you send is null!");
			}
		} catch (JSONException e) {
			controller.setAttr("status", -1);
			controller.setAttr("message", "The data you send is not a validated json!");
			controller.render("", 400);
		} catch (IOException e) {
			controller.setAttr("status", -1);
			controller.setAttr("message", "IO exception!");
		}
	}
	
	private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}