package com.iaskdata.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iaskdata.model.Model;
import com.iaskdata.util.KafkaUtil;
import com.iaskdata.util.LogUtil;
import com.iaskdata.util.ValidateUtil;
import com.iaskdata.validator.Validator;

/**
 * 
 * @author jiangzhx@gmail.com
 * @date 2015年7月31日
 */
public class RestService {

	/**
	 * 
	 * @param jsonData
	 * @param what
	 * @param ip
	 * @return
	 */
	public Map<String, Object> restHandler(JSONObject jsonData, String what,
			String ip, boolean isValidateWhen) {
		Map<String, Object> result = new HashMap<String, Object>();

		JSONObject validateMsg = Validator.validate(jsonData, what);
		if (!ValidateUtil.isValid(validateMsg)) {
			Model model = JSON.parseObject(jsonData.toString(), Model.class);
			model.validate(what, ip, false, isValidateWhen);

			String data = model.toString();
			if ("event".equals(what)) {
				LogUtil.segments(data);
			} else if (!what.equalsIgnoreCase("profile")) {
				LogUtil.rawdata(data);
			}

			KafkaUtil.getInstance().send("game-" + what, data);

			result.put("status", 0);
		} else {
			if ("event".equals(what)) {
				LogUtil.segmentsTrash(jsonData.toString());
			} else {
				LogUtil.rawdataTrash(jsonData.toString());
			}
			result.put("result", validateMsg);
			result.put("status", -1);
		}

		return result;
	}

	/**
	 * 
	 * @param jsonData
	 * @param what
	 * @param ip
	 * @return
	 */
	public Map<String, Object> eventHandler(JSONObject jsonData, String what,
			String ip) {
		Map<String, Object> result = new HashMap<String, Object>();

		JSONObject validateMsg = Validator.validate(jsonData, what);
		if (!ValidateUtil.isValid(validateMsg)) {
			Model model = JSON.parseObject(jsonData.toString(), Model.class);
			model.validate(what, ip, false, true);

			String data = model.toString();
			LogUtil.segments(data);

			result.put("status", 0);
		} else {
			result.put("result", validateMsg);
			result.put("status", -1);
		}

		return result;
	}
}
