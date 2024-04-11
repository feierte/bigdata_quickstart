package org.sperri.flink.demo.stream;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.sperri.flink.demo.EventLog;

/**
 * @author Jie Zhao
 * @date 2024/3/29 21:43
 */
public class EventLogSource implements SourceFunction<EventLog> {

    private volatile boolean stop = false;

    public EventLogSource() {

    }

    @Override
    public void run(SourceContext<EventLog> ctx) throws Exception {

        while (!stop) {
            EventLog eventLog = new EventLog();
            ctx.collect(eventLog);
        }
    }

    @Override
    public void cancel() {
        stop = true;
    }
}
