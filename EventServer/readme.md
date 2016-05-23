# EventServer
该项目包含三部分代码，日志收集，离线模型训练，vw jni封装
## lib目录
* config.h
``vw PACKAGE_VERSION信息``
* vw-jni-8.1.2-SNAPSHOT.jar
``通过vw项目编译出来jar包``
* vw_jni.lib
``通过vw项目编译出来出来的jni封装库``

## src目录
* main/java
`` 日志收集相关的类，当前支持日志记录到本地磁盘，发送数据到kafka服务 ``
* main/scala
`` sparkling-water 模型训练相关的代码 ``
* test/java/vw/learner
`` 调用vw jni封装的测试用咧 ``