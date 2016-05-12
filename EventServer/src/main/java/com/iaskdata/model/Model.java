package com.iaskdata.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.iaskdata.util.Constant;
import com.iaskdata.util.DateUtil;
import com.iaskdata.util.ValidateUtil;

/**
 * 接收用户发送数据模型
 * @author ruijie jiangzhx@gmail.com
 * @date 2015年1月22日
 */
public class Model {

	private String who;
	
	private String when;
	
    private String where;
	
	private String what;
	
	private CaseInsensitiveMap<String, String> context;
	
	private String appid;
	
    private String ds;
	
	public void setWho(String who) {
		this.who = who;
	}
	
	public void setWhen(String when) {
		this.when = when;
	}
	
	public void setWhat(String what) {
	    this.what = what;
	}
	
	public void setContext(CaseInsensitiveMap<String, String> context) {
        this.context = context;
	}
	
	public void setAppid(String appid) {
        this.appid = appid;
    }
	
    public String getWho() {
		return who;
	}
	
	public String getWhen() {
		return when;
	}
	
	public String getWhere() {
		return where;
	}
	
	public String getWhat() {
		return what;
	}
	
	public Map<String, String> getContext() {
		if (ValidateUtil.isValid(context)) {
			return context;
		} else {
			return null;
		}
	}
	
	public String getAppid() {
        return appid;
    }
	
	public String getDs() {
        return ds;
    }

	// update-Author:ruijie Date:2016-04-15 for: 添加isValidateWhen参数 --------
	public void validate(String what, String ip, boolean deviceid2UpperCase, boolean isValidateWhen) {
	    if ( ! ValidateUtil.isValid(this.who)) {
            this.who = Constant.UNKNOWN;
        }
	    
        if ( ! ValidateUtil.isValid(this.what)) {
            this.what = what;
        }
        
        if ("register".equals(this.what)) {
            this.what = "reged";
        } else if ("heartbeat".equals(this.what)) {
            this.what = "hb";
        } else if ("pfheartbeat".equals(this.what)) {
            this.what = "pfhb";
        }
        
        where = what;
        
        when = DateUtil.getValidateWhen(when, isValidateWhen);
        ds = StringUtils.substringBefore(when, " ");
        
        if (ValidateUtil.isValid(context)) {
            if ( ! ValidateUtil.isValid(context.get("tz"))) {
                context.put("tz", "+8");
            }
            if ( ! ValidateUtil.isValid(context.get("ip"))) {
                context.put("ip", ip);
            }
            if ( ! ValidateUtil.isValid(context.get("serverid"))) {
                context.put("serverid", Constant.UNKNOWN);
            }
            if ( ! ValidateUtil.isValid(context.get("channelid"))) {
                context.put("channelid", Constant.UNKNOWN);
            }
            if ("payment".equals(this.what) && ! ValidateUtil.isValid(context.get("virtualcoinamount"))) {
                context.put("virtualcoinamount", "0");
            }
            
            if (deviceid2UpperCase) {
                String deviceid = context.get("deviceid");
                if (ValidateUtil.isValid(deviceid) && ! StringUtils.isAllUpperCase(deviceid)) {
                    context.put("deviceid", deviceid.toUpperCase());
                }
                
                String idfa = context.get("idfa");
                if (ValidateUtil.isValid(idfa) && ! StringUtils.isAllUpperCase(idfa)) {
                    context.put("idfa", idfa.toUpperCase());
                }
                
                String mac = context.get("mac");
                if (ValidateUtil.isValid(mac) && ! StringUtils.isAllUpperCase(mac)) {
                    context.put("mac", mac.toUpperCase());
                }
            }
        }
	}
	
	public void validatePkginfo(String ip) {
        if ( ! ValidateUtil.isValid(this.who)) {
            this.who = Constant.UNKNOWN;
        }
        
        where = what = "pkginfo";
        
        when = DateUtil.getValidateWhen(when, true);
        ds = StringUtils.substringBefore(when, " ");
        
        if (ValidateUtil.isValid(context)) {
            if ( ! ValidateUtil.isValid(context.get("ip"))) {
                context.put("ip", ip);
            }
            
            String pkglist = context.get("pkglist");
            context.put("pkglist", decompressString(pkglist));
        }
    }
	
    public String toJSONString() {
	    return JSON.toJSONString(this);
	}
	
	@Override
	public String toString() {
	    StringBuffer sb = new StringBuffer();
	    
	    // update-Author:ruijie Date:2016-01-25 for: 添加判断当what不为pkginfos则value需要截取 --------
	    boolean isSubStr = ! "pkginfo".equals(what);
	    
	    sb.append(who).append("\t")
            .append(when).append("\t")
            .append(where).append("\t")
            .append(what).append("\t")
            .append(formatContext(context, isSubStr)).append("\t")
            .append(appid).append("\t")
            .append(ds);
	    
		return sb.toString();
	}
	
	private String formatContext(Map<String, String> context, boolean isSubStr) {
	    StringBuffer sb = new StringBuffer();
	    
	    Set<Entry<String, String>> entrySet = context.entrySet();
	    
	    String tmpValue = null;
	    for (Entry<String, String> entry : entrySet) {
	        sb.append(entry.getKey()).append("\001");
	        if (ValidateUtil.isValid(entry.getValue())) {
	            tmpValue = entry.getValue();
	            // update-Author:ruijie Date:2016-01-25 for: 判断value是否需要截取 --------
	            if (isSubStr && tmpValue.length() > 1024) {
	                tmpValue = StringUtils.substring(tmpValue, 0, 1024);
	            }
	            sb.append(tmpValue);
	        } else {
	            sb.append(Constant.UNKNOWN);
	        }
	        sb.append("\002");
	    }
	    
        return StringUtils.replace(sb.deleteCharAt(sb.length() - 1).toString(), "''", Constant.UNKNOWN);
	}
	
	private String decompressString(String str) {
        char[] tempBytes = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tempBytes.length; i++) {
            char c = tempBytes[i];
            char firstCharacter = (char) (c >>> 8);
            char secondCharacter = (char) ((byte) c);
            sb.append(firstCharacter);
            if (secondCharacter != 0)
                sb.append(secondCharacter);
        }
        return sb.toString();
    }
}
