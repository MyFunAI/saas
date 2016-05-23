# receive
主要和日志收集相关,java目录的代码都是我自己写的,并且在线上测试过,不用担心  

* com.iaskdata.config
项目相关配置
* com.iaskdata.controller
相关接口,日志收集,操作接口:例如train,predict
* com.iaskdata.interceptor
拦截器,统一处理关于json,cors,http请求的过滤
* **com.iaskdata.model**
    * Model.java 
        日志收集使用
    * BadLoanModel.java/InterestRateModel.java 
        app-consumer-loan项目训练出来的模型
    * GBM_model_1463400594745_1.java 
        SparklingWater.scala 训练出的模型
    * gbm_02c92461_25ea_45d2_9bf9_af2fa92758d2.java
        配合scala/com.iaskdata.PojoExample测试使用

* com.iaskdata.render
为支持IE,编写json渲染工具
* com.iaskdata.service
    * **PredictService.java**
    使用com.iaskdata.model里面的模型,进行数据预测的接口实现
    * RestService.java
    日志收集,报送数据到kafka的相关实现
* com.iaskdata.service
工具类
* com.iaskdata.validator
日志收集过程中,进行必填参数验证