package org.sperri.flink.demo.stream;

import org.sperri.flink.demo.EventLog;

import java.util.Random;
import java.util.UUID;

/**
 * @author Jie Zhao
 * @date 2024/3/29 21:51
 */
public class EventLogGenerator {

    private Random random = new Random(System.currentTimeMillis());
    private OutOfOrderness outOfOrderness;
    private static final long[] IDS = new long[]{1, 2, 3, 4, 5, 6, 7, 8};


    public EventLogGenerator(int bounded) {
        outOfOrderness = new OutOfOrderness(bounded);
    }

    public EventLog getEventLog() {
        EventLog eventLog = new EventLog();

        eventLog.setUid(UUID.randomUUID().toString());
        eventLog.setId(IDS[random.nextInt(IDS.length - 1)]);
        eventLog.setTime(outOfOrderness.getTime());
        eventLog.setSip(randomIp());
        return eventLog;
    }

    public String randomIp() {
        StringBuffer sb = new StringBuffer();
        sb.append(random.nextInt(255));
        sb.append(".");
        sb.append(random.nextInt(255));
        sb.append(".");
        sb.append(random.nextInt(255));
        sb.append(".");
        sb.append(random.nextInt(255));
        return sb.toString();
    }

    public class OutOfOrderness {

        private int bounded;

        public OutOfOrderness(int bounded) {
            this.bounded = bounded;
        }

        public long getTime() {
            return System.currentTimeMillis() - random.nextInt(bounded);
        }
    }
}
