package com.iaskdata.test;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.iaskdata.validator.Validator;

public class ValidatorTest {

    @BeforeClass
    public static void init() throws IOException {
        Validator.load();
    }
    
    @Test
    public void install() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "install");
        System.out.println("install: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void startup() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "startup");
        System.out.println("startup: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void register() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "register");
        System.out.println("register: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void loggedin() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "loggedin");
        System.out.println("loggedin: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void payment() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        context.put("paymenttype", "xxx");
        context.put("transactionid", "xxx");
        context.put("currencytype", "xxx");
        context.put("currencyamount", "xxx");
        context.put("virtualcoinamount", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "payment");
        System.out.println("payment: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void economy() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        context.put("itemname", "xxx");
        context.put("itemtotalprice", "xxx");
        context.put("itemamount", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "economy");
        System.out.println("economy: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void quest() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        context.put("queststatus", "xxx");
        context.put("questid", "xxx");
        context.put("questtype", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "quest");
        System.out.println("quest: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void event() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        data.put("what", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "event");
        System.out.println("event: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void heartbeat() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "heartbeat");
        System.out.println("heartbeat: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void click() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        context.put("ip", "xxx");
        context.put("channelid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "click");
        System.out.println("click: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void attribute() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "attribute");
        System.out.println("attribute: " + JSONObject.toJSONString(result, true));
    }
    
    @Test
    public void pv() {
        JSONObject data = new JSONObject();
        data.put("appid", "xxx");
        data.put("who", "xxx");
        
        JSONObject context = new JSONObject();
        context.put("deviceid", "xxx");
        
        data.put("context", context);
        
        JSONObject result = Validator.validate(data, "pv");
        System.out.println("pv: " + JSONObject.toJSONString(result, true));
    }
}
