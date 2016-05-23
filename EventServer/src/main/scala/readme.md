# Sparkling-Water 模型训练和预测示例


* com.iaskdata
    * PojoExample.scala
    主要测试如果通过动态加载java模型,进行预测
    * SparklingWater.scala
    针对SparklingWaterDroplet的封装,主要逻辑和SparklingWaterDroplet一样。
    ``mvn scala:run -Dlauncher=PojoExample`` 
    可运行程序#在EventServer根目录执行
    * SparklingWaterDroplet.scala
    通过data/iris_sparklingwaterdroplet.csv 文件进行模型训练,并且预测结果
    ``mvn scala:run -Dlauncher=SparklingWaterDroplet`` 
    可运行程序#在EventServer根目录执行    
    
    
    
    

* Sparkling-water的主要使用逻辑为
    * 训练模型
    * 导出模型为xxx.java文件
    * 添加xxx.java文件到java/com.iaskdata.model目录
    * 修改java/com.iaskdata.service.PredictService.java 进行数据预测
    
    
* 关于新建scala文件,如何运行
    * 编写pom.xml文件,在pom.xml里面搜索launcher,并且按照下面编写就可以 
    ``
        <!-- scala 配置-->
        <launcher>
            <id>PojoExample</id>   #任意不重复的名称
            <mainClass>com.iaskdata.PojoExample</mainClass>  #类路径
        </launcher>
        <!-- scala 配置-->    
        mvn scala:run -Dlauncher=PojoExample 就可以运行了
    ``