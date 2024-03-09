# 概述



## 安装与配置

### 常规安装



### docker安装





## 常用命令



### 主题操作命令（topic）



```bash
# 创建topic命令
# --topic 指定topic名称
# --bootstrap-server 指定kafka主机及端口
$ bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
```



```bash
# 查看topic详细信息
# --topic 指定topic名称
# --bootstrap-server 指定kafka主机及端口
$ bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
```



### 生产者操作命令（producer）



```bash
# 向topic发送消息
# --topic 指定topic名称
# --bootstrap-server 指定kafka主机及端口
$ bin/kafka-console
producer.sh --topic quickstart-events --bootstrap-server localhost:9092
```





### 消费者操作命令（consumer）



```bash
# 向topic发送消息
# --topic 指定topic名称
# --bootstrap-server 指定kafka主机及端口
# --from-beginning 表示从头开始消费
$ bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
```





# 核心概念



## Producers



## Consumers

​	kafka在消费消息的时候采用的是拉模式（pull）。与之相对应的是推模式（push），即kafka把消息发送到消费者。这样设计的好处是可以**按需拉取**，应用程序能够根据自身消费能力来一次获取多少数据。



### Consumer group

​	一个消息被消费者组里面的某一个消费者消费，那边该消费者组中的其他消费者就不能在消费该消息了。



## Broker

​	kafka是可以由一台或多台机器组成，组成kafka的机器称为broker

## Topic

​	topic中文翻译为主题，表示kafka中某一类日志。通常同一类日志存储到同一个topic中，例如登录日志发送到名为login-topic的topic中，网页浏览行为产生的日志发送到名为page-view-topic的日志中，但是不能将topic混用，即将登录日志发送到page-view-topic这个topic中，这是我们日常使用topic的方式。

​	topic是kafka中的一个逻辑概念，而不是真正存储数据的地方，而是存储到partition中，partition将会在接下来的内容中介绍。

​	为了形象的理解topic和partition的概念，可以将topic想象为一个文件目录，将partition想象为目录下的一个文件，多个partition表示该目录下有多个文件。这样表述不是很准确，可以先这么理解，后面在深入介绍这两者的关系。



**topic的概念是为了解决什么问题？**



## Partition

​	一个topic可以有1个或多个partition，当topic创建时，不进行任何设置的情况下默认是1个partition。可以在创建时，指定topic具有多个partition。当生产者生产一条消息并发送到kafka的topic时，消息实际是以**追加的方式**存储到topic下的partition里面。

![img](.\kafka文档.assets\streams-and-tables-p1_p4.png)

​	上述图片来自于kafka官网，途中描述了一个具有4个partition（P1,P2,P3,P4）的topic。相同颜色的方框表示具有相同key值的消息，具有相同key的消息会被发送到topic下的同一个partition。

​	一个消息将会发送到topic的任一partition（即同一个消息不会出现在两个及以上partition中），这要取决于消息的key值。不同生产者可以将消息发送到同一个partition上，同一个生产者也可以将消息根据key的不同将消息发送到不同的partition上。

**todo：介绍多个partition时，是如何在kafka集群中存放的**



**todo：介绍一下副本机制，放在后面日志存储介绍是否合理**

​	为了高可用和容错性，partition支持副本机制（replication），即一个partition可以有零个或多个副本。



**partition的概念是为了解决什么问题？**

​	为了解决海量存储的问题。

先看如果没有partition的概念情况下，是如何办的？没有partition，即相当于一个topic只有一个partition，

- 当所有生产者生成的消息只能发送给这个partition，这会导致吞吐量下降，因为这样kafka的接受消息能力不高，无法同时接受大量消息。
- 消息存储量受限，一个partition只能存储在一台设备上，磁盘容量的扩展是有限的。

## Message

​	kafka中不是直接将生产者发送的消息存储到磁盘上的，而是先将消息封装为kafka的统一的格式Message。Message除了包含生产者发送的消息，还增加了一些元数据，例如版本信息、offset等，有利于kafka管理和消费。



## Controller

​	kafka 控制器组件(controller) 即 broker， 是 Kafka 的核心组件。它的主要作用是在 zooKeeper或kraft 的帮助下管理和协调整个 Kafka 集群

​	集群中任意一台 broker 都能充当controller的角色，但是在运行过程中，只能有一个 broker 成为controller，来执行管理和协调的职责。换句话说，每个正常运转的 Kafka 集群，在任意时刻都有且只有一个controller。kafka集群采用的是主从架构，controller即相当于分布式软件中的leader。



**Controller职责**

- topic管理

- partition重分配

- partition副本的leader选举

- 集群成员管理

  

- 提供数据服务

  



## Zookeeper









## KRaft





# 消息投递语义

at least once、at most once、exactly once



exactly once

- 生成不丢失，ack机制及retry机制
- 生产不重复，幂等性
- 消息不丢失不重复，手动管理offset等



# 日志存储







## 存储文件



topic是kafka的逻辑概念，partition才是kafka真正存储数据的地方。

**todo：消息存储在哪个目录下的**



## Offset管理



### 定位消息流程





## Partition副本机制



**todo：介绍副本机制及副本作用**

**todo：介绍这些AR、ISR、OSR概念**





### 副本Leader选举



### 副本数据同步





**HW和LEO机制**







## 消息结构





# Controller组件



## Controller选举



## Controller故障转移



## Controller重构

0.11版本重构了Controller





# 生产消息机制



## 概述



**生产者发送消息代码示例**

```java
String topicName = "event-log-topic";
Properties props = new Properties();
props.put("bootstrap.servers", KAFKA_BROKER_SERVER);
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);
ProducerRecord<String, String> record = new ProducerRecord<>(topicName, accessLogMessage);
producer.send(record);
```



**异步发送消息示例**



### 生产者发送消息流程



### 发送消息确认机制



`request.required.acks`









## 自定义分区器





## Kafka事务



## 生产者调优

### 生产者提高发送吞吐量



**调生产者端的发送参数**

参数一：

​	`buffer.memory `设置发送消息的缓冲区，默认值：33554432 ，即32m。该参数指定生产者端发送消息时的缓冲区大小。一般设置32m足以应多绝大数情况了，注意该参数设置的越大也意味着发送延迟（消息发送到kafka接收到消息时间）越大。



参数二：

​	`compression.type`设置压缩消息的方式，默认值：none，不压缩消息，还有其他几种可选的压缩方式：gzip，snappy，lz4，zstd，指定压缩消息可以减少消息体积，从而提高发送吞吐量。注意如果指定了压缩消息，那么会增加生产端的CPU开销。



参数三：

​	`batch.size`设置生产者发送消息的批次大小，默认值：16384 ，即16kb。生产者是将消息聚成一批，然后一起将这一批消息发送给kafka集群，这样做的好处是减少网络请求次数，从而提供发送吞吐量。注意该参数越大可能会导致发送延迟越大，即发送者需要时间将消息批次累计到该参数值后才能发送消息。该参数通常是配合下面参数一起使用。

​	`linger.ms`设置生产者发送消息时间间隔，默认值：0。时间达到发送消息时间间隔就会将该消息发送给kafka。如果达到发送时间间隔，即使没有满足`batch.size`参数指定的消息批次大小，也会将消息发送给kafka，所以这两个参数可以平衡一次发送消息的大小和发送延迟时间。



**使用异步发送方式**





### 发送消息时常见异常



**LeaderNotAvailableException**

​	通过前面的内容介绍可知，parition是有副本机制的，多个副本中在某一个时刻有且仅有一个leader存在，且只有该leader副本才会接受生产者的写请求和消费者的读请求。该异常信息就表示该partition的leader副本挂掉了，需要重新选举出来一个新的leader后，才能继续读和写。

​	该问题可以通过在生产者端配置下面两个参数解决。

`retries`参数指定消息发送失败后的重试次数。

`retry.backoff.ms`参数指定重试时间间隔。





**NotControllerException**

**NetworkException**





### 消息不丢失方案



`request.required.acks`

`min.insync.replicas`



- 分区副本 >= 2
- acks = -1
- min.insync.replicas >= 2





# 消费消息机制



​	Broker不保存订阅者的状态，由订阅者自己保存。 无状态导致消息的删除成为难题（可能删除的消息正在被订阅），Kafka采用基于时间的SLA（服务保证），消息保存一定时间（通常7天）后会删除。 



# Kafka Streams







# 监控





# 生产实践



## 最佳实践



## 运维

### 运维工具



#### kafka-manager



#### kafka-map





### 运维场景



**场景一：topic的partition数太少，需要增加partition数量**



**场景二：增加topic的副本因子**

kafka-reassign-partitions.sh



**创建三：负载不均衡的topic，手动迁移，某台broker上面的topic过多，而这台broker又负载很大，把这台上面的topic迁移到负载较小的topic上**

kafka-reassign-partitions.sh



**场景四：如果某个broker中的leader partition过多，如何负载均衡一下使leader partition均匀分配到各个partition上**



## 生产环境调优









