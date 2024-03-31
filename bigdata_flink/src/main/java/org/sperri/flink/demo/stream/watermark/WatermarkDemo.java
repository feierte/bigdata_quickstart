package org.sperri.flink.demo.stream.watermark;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.sperri.flink.demo.EventLog;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author Jie Zhao
 * @date 2024/2/21 20:53
 */
@Slf4j
public class WatermarkDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setAutoWatermarkInterval(200); // 设置 watermark 的产生时间间隔，时间单位是 ms

        DataStreamSource<EventLog> source = env.fromElements(
                EventLog.of("1", System.currentTimeMillis(), "page01"),
                EventLog.of("2", System.currentTimeMillis(), "page02")
        );

        SingleOutputStreamOperator<EventLog> watermarkedStream = source.assignTimestampsAndWatermarks(
                WatermarkStrategy.<EventLog>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner((event, recordTimestamp) -> event.getTime())
        );


        SingleOutputStreamOperator<Tuple2<String, EventLog>> mappedStream = watermarkedStream
                .map(event -> new Tuple2<>(event.getUid(), event))
                .returns(TypeInformation.of(new TypeHint<Tuple2<String, EventLog>>() {
                    @Override
                    public TypeInformation<Tuple2<String, EventLog>> getTypeInfo() {
                        return super.getTypeInfo();
                    }
                }));

        SingleOutputStreamOperator<Tuple2<String, EventLog>> processedStream = mappedStream.process(new ProcessFunction<Tuple2<String, EventLog>, Tuple2<String, EventLog>>() {
            @Override
            public void processElement(Tuple2<String, EventLog> tuple2, ProcessFunction<Tuple2<String, EventLog>, Tuple2<String, EventLog>>.Context context, Collector<Tuple2<String, EventLog>> collector) throws Exception {
                long processingTime = context.timerService().currentProcessingTime();
                long watermark = context.timerService().currentWatermark();
                System.out.println("当前处理时间：{}" + processingTime);
                log.info("当前处理时间：{}", processingTime);
                log.info("当前水位线：{}", watermark);
                log.info("当前事件：{}", tuple2.f1);
            }
        });

        processedStream.print();
        env.execute();
    }
}
