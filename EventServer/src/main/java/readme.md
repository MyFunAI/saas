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

==================================
Belief propagation algorithm
==================================
The algorithm details are in package algorithm.beliefpropagation.
The entrance (the calling entry) of the BP algorithm is in AntiFraudController.
Ideally, the data and parameter settings are provided by the users on
the web client, while the code runs the algorithm with the provided settings.
Currently, it takes three steps to run the algorithm:
1) Create an edge binary file from a given edge text file
2) Create a prior binary file from a given prior text file
3) Run the BP algorithm with the binary files and other parameter settings
For example (the following command lines may not work and are only used
for illustration purposes)
1) java BpGraphCreator.createBPMessages ../edges.txt ../edges.bin " "
2) java BpGraphCreator.createBPPriors ../priors.txt ../priors.bin 6 " p"
3) a Bp class needs to be instantiated by
   new Bp("edges.bin", "priors.bin", 6, 5, 10, "./")
   and then
   java Bp.run()
