package com.iaskdata.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 *  e-commerce with yelp data
 */
public class ECommerceController extends Controller implements Prediction {


    /**
     * 模型训练接口
     */
    @ActionKey("/ec/train")
    public void train() {
        setAttr("status", -1);
        setAttr("msg", "Unimplemented");
        setAttr("ts", System.currentTimeMillis());

    }


    /**
     * 模型預測接口
     */
    @ActionKey("/ec/predict")
    public void predict() {
        setAttr("status", 0);
    }

}
