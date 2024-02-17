package org.sperri.flink.demo.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.internals.metrics.KafkaConsumerMetricConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;

/**
 * @author Jie Zhao
 * @date 2024/2/15 下午9:04
 */
public class KafkaSourceDemo {

    public static void main(String[] args) {

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

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka-source");
    }
}
