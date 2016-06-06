package com.iaskdata.controller;

import com.iaskdata.service.PredictService;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import hex.genmodel.easy.exception.PredictException;

import java.util.ArrayList;
import java.util.List;

/**
 * app consumer loan depends h2o demo
 */
public class ConsumerLoanController extends Controller implements  Prediction{


    /**
     * 模型训练接口
     */
    @ActionKey("/cl/train")
    public void train() {
        setAttr("status", -1);
        setAttr("msg", "Unimplemented");
        setAttr("ts", System.currentTimeMillis());

    }


    /**
     * 模型預測接口
     */
    @ActionKey("/cl/predict")
    public void predict() {
        setAttr("status", 0);
        this.getRequest().getQueryString();
        PredictService ps = new PredictService();
		try {
        List result = new ArrayList();
            result=  ps.predictHandle(this.getRequest().getQueryString());
        setAttr("result", result);
		} catch (PredictException e) {
			setAttr("status", -1);
			setAttr("msg", "Unimplemented");
			setAttr("ts", System.currentTimeMillis());
			e.printStackTrace();
		}

    }

}
