

# 流处理API

## 算子



### 算子概念



### Source算子

#### KafkaSource算子



```java
KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setTopics("test")
                .setGroupId("test")
                .setBootstrapServers("test")
                // 设置 kafka 偏移量
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.LATEST)) // 消费起始位移选择之前所提交的偏移量（如果没有，则重置为 LATEST）
                //.setStartingOffsets(OffsetsInitializer.earliest()) // 消费起始位移为 “最早”
                //.setStartingOffsets(OffsetsInitializer.latest()) // 消费起始位移为 “最新”
                //.setStartingOffsets(OffsetsInitializer.offsets(Map< TopicPartition, Long>)) // 消费起始位置为：方法传入的每个分区及对应的起始偏移量
                // 设置 kafka 消息反序列化器
                .setValueOnlyDeserializer(new SimpleStringSchema()) // 设置反序列化器，kafka 消息中只有 value 值
                //.setDeserializer(KafkaRecordDeserializationSchema) // 设置反序列化器，kafka 消息中既有 key 值，也有 value 值
                // 把 source 算子设置成 BOUNDED 属性（即有界流），就是读到传入参数指定的位置就停止读取并退出。
                // 常用于补数或者重跑某一段历史数据
                .setBounded(OffsetsInitializer.committedOffsets())
                // 把 source 算子设置成 UNBOUNDED 属性（即无界流），但是并不会一直读数据，而是到达参数指定位置后就停止读取，但程序不退出。
                // 主要应用场景：需要从 kafka 中读取某一段固定长度的数据，然后拿着这段数据去跟另外一个真正的无界流联合处理
                .setUnbounded(OffsetsInitializer.committedOffsets())
                // 设置 kafka 的其他属性
                //.setProperties()
                // 开启 kafka 底层消费者的自动偏移量提交机制，它会把最新的消费偏移提交到 kafka 的 consumer_offsets 中。
                // 就算开启了自动偏移里昂提交机制，KafkaSource 依然不依靠自动偏移量提交机制（宕机重启时，优先从 flink 自己的状态中获取 topic 的偏移量，因为更可靠；
                // 状态中没有的话才从 kafka 的 consumer_offsets 中拿）
                .setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
                .build();
```





#### 自定义Source算子



### Transformation算子

#### map算子



#### flatMap算子



#### project算子



类似 sql 中的 select



### Sink算子



#### StreamFileSink



#### KafkaSink



#### JdbcSink



#### RedisSink



### 分区算子

​	涉及到分布式存储概念的系统或者框架中常有分区概念，例如kafka、es等。它们常常把数据切割分为不同的部分存储到集群中的不同节点机器上，这样的好处显而易见：不会受单台机器存储能力的限制，利用分布式集群的水平扩展能力支持海量数据的存储。

​	flink是一个分布式计算的框架，也存在数据分区概念，但和分布式存储中的分区概念略有不同。flink会将task划分为一个或多个subtask，每个subtask处理一部分数据，从而提升计算处理能力。所以flink中的分区指的是将上游算子产生的数据发送到下游算子的哪一个subtask中。提供这种能力的算子就叫做**分区算子**。

> task和subtask的详细内容会在接下来的部分进行详细介绍



#### 分区策略



## 多流转换



#### 连接流（connect）



#### 广播流（broadcast）



#### 合并流（union）



#### 关联操作（join）

类似sql中的join关键字



#### 协同分组（coGroup）





#### 侧输出流（side-ouput）







## 



# Flink架构

## 概述



## JobManager



## TaskManager



### TM中核心概念



#### task

#### 算子链

disableChain startNewChain



#### taskslot

槽位共享组（slot sharing group）



## Flink部署模式

# 核心概念



## 时间语义



事件时间、处理时间、注入时间



## 水位线（watermark）



### 水位线生成策略





### 水位线传播

单并行度下和多并行度下的水位线传播



## 窗口（window）

Flink程序运行起来，第一个窗口的起始时间是多少？有啥规律？



### 窗口类别



### 窗口处理算子



### 窗口触发器





# Process Function

# 状态管理



## 概述



### 状态类别





# 容错机制（Fault Tolerance）



# Flink调优





# Table & SQL













