package com.iaskdata.interceptor;

import java.util.Arrays;

import com.iaskdata.util.Constant;
import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.Controller;

/**
 * 接口方法验证拦截器
 * @author jiangzhx@gmail.com
 * @date 2015年7月16日
 */
public class MethodInterceptor extends PrototypeInterceptor {
	
	@Override
	public void doIntercept(Invocation invocation) {
	    String actionKey = invocation.getActionKey();
		
		Controller controller = invocation.getController();
		
        String method = controller.getPara();
        
        boolean result = false;
        if ("/rest".equals(actionKey)) {
            result = contains(Constant.rest, method);
            if (! result ) {
                controller.setAttr("message", "The REST sdk available method: " + Arrays.toString(Constant.rest));
            }
        }  else {
            result = true;
        }
        
        if (result) {
            invocation.invoke();
        } else {
            controller.setAttr("status", -1);
            controller.render("", 404);
        }
	}
	
	private boolean contains(String[] arr, String targetValue) {
        if (Arrays.binarySearch(arr, targetValue) >= 0)
            return true;
        else
            return false;
    }
}