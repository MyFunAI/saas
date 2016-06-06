package com.iaskdata.controller;


/**
 * Created by sylar on 16/6/6.
 */
public interface Prediction {

    /**
     * 模型训练接口
     */
    public void train() ;

    /**
     * 模型預測接口
     */
    public void predict() ;

}
