# Java爬虫-Crawler
## 四个模块
    1.fetcher抓取网页url，进行递归抓取并且存储到mongodb中
    2.spider从运行时队列取url进行源码下载，网页源码存储在solr上
    3.parser从solr中取出未解析的网页源码进行数据清洗，并存储至solr中
    4.storage存储至清洗过后的数据至其它的介质中（暂未实现，可根据需要去实现），接口未开发及规范化

## 用到的开源组件及技术点
    1.多线程，JDK的Executor框架
    2.Jsoup网页解析
    3.mongodb.3.4
    4.solr6.3

## 目前完成度
    1.项目并没有实现通用化
    2.需要开发相应的接口，封装底层，给用户提供表层使用接口，实现相应的接口
    即可使用
    3.代码需要进一步优化，多线程及网络请求，日志等需要更精细的控制

## 主要流程图如下：
![流程图及四大模块](https://raw.githubusercontent.com/yummykang/res/master/process.png)