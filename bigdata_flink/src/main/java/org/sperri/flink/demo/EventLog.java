package org.sperri.flink.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * @author Jie Zhao
 * @date 2024/2/16 上午11:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {

    private String uid;
    private Long time;
    private String pageId;
    private String sip;
    private String host;

    public static EventLog of(String uid, Long time, String pageId) {
        return new EventLog(uid, time, pageId, null, null);
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "uid='" + uid + '\'' +
                ", time=" + time +
                ", pageId='" + pageId + '\'' +
                ", sip='" + sip + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
