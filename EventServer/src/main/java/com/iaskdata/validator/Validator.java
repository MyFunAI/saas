package com.iaskdata.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.iaskdata.util.ValidateUtil;

/**
 * 数据校验
 * @author jiangzhx@gmail.com
 * @date 2015年7月27日
 */
public class Validator {
    private static final Logger logger = LoggerFactory.getLogger("stdout");
    
    private static JSONObject validator;
    
    public static final void load() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Validator.class.getClassLoader().getResourceAsStream("validate.json")));
            
            StringBuffer sb = new StringBuffer();
            
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            reader.close();
            
            validator = JSONObject.parseObject(sb.toString());
        } catch (IOException e) {
            logger.error("Validate.json read error, receive will be exit!", e);
            System.exit(-1);
        }
    }
    
    public static final JSONObject validate(JSONObject data, String what) {
        JSONObject result = new JSONObject();
        
        if ( ! ValidateUtil.isValid(data.getString("context"))) {
            result.put("context", "Context can not be null!");
        }
        
        JSONObject commonValidator = validator.getJSONObject("common");
        validate(data, commonValidator, result);
        
        JSONObject whatValidator = validator.getJSONObject(what);
        
        if (ValidateUtil.isValid(whatValidator)) {
            validate(data, whatValidator, result);
        }
        
        return result;
    }
    
    private static void validate(JSONObject data, JSONObject validator, JSONObject result) {
        Set<String> keys = validator.keySet();
        
        String value = null;
        for (String key : keys) {
            if ( ! "context".equals(key)) {
                value = data.getString(key);
                if (ValidateUtil.isValid(value)) {
                    if ("appid".equals(key)) {
                        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
                        Matcher matcher = pattern.matcher(value);
                        if ( ! matcher.matches()) {
                            result.put(key, "Appid can only contain letters or numbers!");
                        }
                    }
                } else {
                    result.put(key, "Can not be null!");
                }
            }
        }
        
        // context验证
        JSONObject contextValidator = validator.getJSONObject("context");
        String contextStr = data.getString("context");
        
        if (ValidateUtil.isValid(contextValidator) &&
                ValidateUtil.isValid(contextStr)) {
            CaseInsensitiveMap<String, String> context = JSON.parseObject(contextStr, new TypeReference<CaseInsensitiveMap<String, String>>() {});
            
            JSONObject contextResult = result.getJSONObject("context");
            if ( ! ValidateUtil.isValid(contextResult)) {
                contextResult = new JSONObject();
            }
            
            int size = contextValidator.getIntValue("size");
            
            if (size != 0 && context.size() > size) {
                contextResult.put("size", "No more than " + size + " keys!" );
            }
            
            Set<String> contextKeys = contextValidator.keySet();
            
            String contextValue = null;
            for (String key : contextKeys) {
                if ( ! "size".equals(key)) {
                    contextValue = context.get(key);
                    if ( ! ValidateUtil.isValid(contextValue)) {
                        contextResult.put(key, "Can not be null!");
                    }
                }
            }
            
            if (ValidateUtil.isValid(contextResult)) {
                result.put("context", contextResult);
            }
        }
    }
}
