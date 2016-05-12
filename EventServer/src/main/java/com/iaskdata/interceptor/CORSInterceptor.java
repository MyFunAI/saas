package com.iaskdata.interceptor;

import javax.servlet.http.HttpServletResponse;

import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.Controller;

/**
 * ajax跨域拦截器
 * @author jiangzhx@gmail.com
 * @date 2015年7月17日
 */
public class CORSInterceptor extends PrototypeInterceptor {

    @Override
    public void doIntercept(Invocation invocation) {
        Controller controller = invocation.getController();

        HttpServletResponse response = controller.getResponse();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        invocation.invoke();
    }
}