package com.iaskdata.util;

import java.util.Arrays;

/**
 * 配置常量
 * @author jiangzhx@gmail.com
 * @date 2015年7月17日
 */
public class Constant {
    private static ConfigUtil configUtil = ConfigUtil.newInstance();
    
    public static final String rest[] = {"install", "startup", "register", "loggedin", "payment",
                                        "economy", "quest", "event", "heartbeat", "profile"};
    
    public static final String kafkaBroker = configUtil.getString("kafka.broker");
    
    
    public static final String TRANSFER = "transfer";
    
    public static final String UNKNOWN = "unknown";
    
    static {
        Arrays.sort(rest);
    }
    
    public static enum Method {
        rest,
    }
}
