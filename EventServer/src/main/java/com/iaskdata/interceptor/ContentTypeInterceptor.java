package com.iaskdata.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.Controller;
import com.jfinal.render.ContentType;

/**
 * 
 * @author jiangzhx@gmail.com
 * @date 2015年7月29日
 */
public class ContentTypeInterceptor extends PrototypeInterceptor {

    @Override
    public void doIntercept(Invocation invocation) {
        Controller controller = invocation.getController();

        HttpServletRequest request = controller.getRequest();
        
        if (null != request.getContentType() && 
                request.getContentType().contains(ContentType.JSON.value())) {
            invocation.invoke();
        } else {
            controller.setAttr("status", -1);
            controller.setAttr("message", "Content type is not validated! Please set Content-Type application/json");
            controller.render("", 415);
        }
    }
}