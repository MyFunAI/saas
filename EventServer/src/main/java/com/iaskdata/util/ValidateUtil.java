package com.iaskdata.util;

import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 校验工具类
 * @author ruijie jiangzhx@gmail.com
 * @date 2015年1月22日
 */
public class ValidateUtil {

	/**
	 * 判断string是否有效
	 * @param str
	 * @return
	 */
	public static final boolean isValid(String str) {
		if (str == null || "".equals(str.trim())) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断集合的有效性
	 * @param col
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static final boolean isValid(Collection col) {
		if (col == null || col.isEmpty()) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断map的有效性
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static final boolean isValid(Map map) {
		if (map == null || map.isEmpty()) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断数组有效性
	 * @param arr
	 * @return
	 */
	public static final boolean isValid(Object[] arr) {
		if (arr == null || arr.length == 0) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断对象有效性
	 * @param obj
	 * @return
	 */
	public static final boolean isValid(Object obj) {
		if (obj == null) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断json有效性
	 * @param obj
	 * @return
	 */
	public static final boolean isValid(JSONObject json) {
		if (json == null || json.isEmpty()) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断json array有效性
	 * @param jsonArray
	 * @return
	 */
	public static final boolean isValid(JSONArray jsonArray) {
	    if (jsonArray == null || jsonArray.isEmpty()) {
	        return false ;
	    }
	    return true ;
	}
}
