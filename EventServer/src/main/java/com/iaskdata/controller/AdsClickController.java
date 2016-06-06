package com.iaskdata.controller;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 * ads click
 */
public class AdsClickController extends Controller implements Prediction {


    /**
     * 模型训练接口
     */
    @ActionKey("/ac/train")
    public void train() {
        setAttr("status", -1);
        setAttr("msg", "Unimplemented");
        setAttr("ts", System.currentTimeMillis());

    }


    /**
     * 模型預測接口
     */
    @ActionKey("/ac/predict")
    public void predict() {
        setAttr("status", 0);
    }

}
