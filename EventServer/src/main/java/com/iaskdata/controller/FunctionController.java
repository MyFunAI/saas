package com.iaskdata.controller;

import java.util.List;

import hex.genmodel.easy.exception.PredictException;

import com.iaskdata.service.PredictService;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 * 功能接口
 * 
 */
public class FunctionController extends Controller {

	/**
	 * 获取系统时间
	 */
	@ActionKey("/gettime")
	public void getTime() {
		setAttr("status", 0);
		setAttr("ts", System.currentTimeMillis());
	}

	/**
	 * 模型训练接口
	 */
	@ActionKey("/train")
	public void train() {
		setAttr("status", -1);
		setAttr("msg", "Unimplemented");
		setAttr("ts", System.currentTimeMillis());

	}

	/**
	 * 模型預測接口
	 */
	@ActionKey("/predict")
	public void predict() {
		setAttr("status", 0);

		PredictService ps = new PredictService();
		try {
			List result = ps
					.predictHandle("loan_amnt=10000&emp_length=5&home_ownership=RENT&annual_inc=60000&verification_status=verified&purpose=debt_consolidation&addr_state=FL&dti=3&delinq_2yrs=0&revol_util=35&total_acc=4&longest_credit_length=10");
			setAttr("result", result);
		} catch (PredictException e) {
			setAttr("status", -1);
			setAttr("msg", "Unimplemented");
			setAttr("ts", System.currentTimeMillis());
			e.printStackTrace();
		}

	}

}
