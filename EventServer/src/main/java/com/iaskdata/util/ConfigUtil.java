package com.iaskdata.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件读取工具类
 * @author jiangzhx@gmail.com
 * @date 2015年7月17日
 */
public class ConfigUtil {
	
	private static final Logger logger = LoggerFactory.getLogger("stdout");
	
	private static ResourceBundle bundle = null;
	
	private static ConfigUtil config = null;

	private ConfigUtil() {
	    try {
		    bundle = ResourceBundle.getBundle("conf");
		} catch (Exception e) {
			logger.error("conf.properties read error, receive will be exit!", e);
			System.exit(-1);
		}
	}
	
	public static final ConfigUtil newInstance() {
		if (null == config) {
			config = new ConfigUtil();
		}
		return config;
	}
	
	public String[] getStringArray(String key) {
		return bundle.getString(key).split(",");
	}
	
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(bundle.getString(key));
	}
}
