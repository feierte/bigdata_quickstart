package org.sperri.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.sperri.kafka.entity.EventLog;
import org.sperri.kafka.util.KafkaUtils;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static org.sperri.kafka.util.KafkaUtils.KAFKA_BROKER_SERVER;

/**
 * @author Jie Zhao
 * @date 2022/5/2 20:55
 */
public class KafkaProducerDemo {

    private static final Random DEFAULT_RANDOM = new Random();

    public static void main(String[] args) throws Exception {
        String topicName = "event-log-topic";
        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_BROKER_SERVER);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        int[] apiIds = new int[] {1, 2, 3, 4, 5, 6};
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < 10; i++) {

            long lateness = DEFAULT_RANDOM.nextInt(10000) * 1000;
            long latenessTime = new Date().getTime() - lateness;
            EventLog accessLog = EventLog.builder()
                    .apiId(apiIds[new Random().nextInt(6)])
                    .occurTime(new Date(latenessTime))
                    .build();
            String accessLogMessage = objectMapper.writeValueAsString(accessLog);
            System.out.println(accessLogMessage);
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, accessLogMessage);

            producer.send(record).get();
        }
    }
}
