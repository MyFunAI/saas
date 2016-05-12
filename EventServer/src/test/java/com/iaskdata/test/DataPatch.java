package com.iaskdata.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.iaskdata.service.RestService;
import com.iaskdata.validator.Validator;

public class DataPatch {

    
    @Test
    public void toJson() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("e:/data/idfa.csv"));
        
        FileWriter fw = new FileWriter("e:/data/idfa.json");
        
        String ip = "127.0.0.1";
        String appid = "624db144e6753ed593e5eaf8f98b4dea";
        
        String line = null;
        while( (line = br.readLine()) != null) {
            JSONObject json = new JSONObject();
            
            JSONObject context = new JSONObject();
            
            String tmp[] = StringUtils.split(line, ",");
            String when = tmp[0];
            String channelid = tmp[1];
            String idfa = tmp[2].toUpperCase();
            
            String deviceid = idfa;
            
            json.put("when", when);
            json.put("appid", appid);
            
            context.put("channelname", "gdt");
            context.put("channelid", channelid);
            context.put("idfa", idfa);
            context.put("deviceid", deviceid);
            context.put("ip", ip);
            
            json.put("context", context);
            
            System.out.println(json.toString());
            fw.write(json.toString() + "\r\n");
        }
        
        br.close();
        fw.close();
    }
}
