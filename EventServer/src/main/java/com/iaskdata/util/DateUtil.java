package com.iaskdata.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换工具
 */
public class DateUtil {
	public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 获取验证过的when
	 * 		若when前后超过24小时则取当前时间
	 * @param when
	 * @return
	 */
	// update-Author:ruijie Date:2016-04-15 for: 添加isValidate参数控制是否验证when前后超过24小时 --------
	public static final String getValidateWhen(String when, boolean isValidate) {
		String result = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(C_TIME_PATTON_DEFAULT);
		
		try {
		    if (ValidateUtil.isValid(when)) {
		        Date now = sdf.parse(when);
	            
	            Calendar c = Calendar.getInstance();
	            c.add(Calendar.DAY_OF_MONTH, - 1);
	            Date start = c.getTime();
	            
	            c = Calendar.getInstance();
	            c.add(Calendar.DAY_OF_MONTH, 1);
	            Date end = c.getTime();
	            
	            result = ! isValidate || (now.after(start) && now.before(end)) ? sdf.format(now) : sdf.format(new Date());
		    } else {
		        result = sdf.format(new Date());
		    }
		} catch (Exception e) {
			result = sdf.format(new Date());
		}
		
		return result;
	}
}
