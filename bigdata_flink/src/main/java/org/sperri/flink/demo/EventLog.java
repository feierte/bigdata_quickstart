package org.sperri.flink.demo;

import lombok.Data;

/**
 * @author Jie Zhao
 * @date 2024/2/16 上午11:53
 */
@Data
public class EventLog {

    private String uid;
    private String sip;
    private String host;
}
