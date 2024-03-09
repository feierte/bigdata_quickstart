package org.sperri.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Random;

/**
 * @author Jie Zhao
 * @date 2022/5/2 21:26
 */
@Data
@Builder
public class EventLog {

    private static final Random DEFAULT_RANDOM = new Random();
    private static final int[] API_IDS = new int[]{1, 2, 3, 4, 5, 6};


    @JsonProperty("api_id")
    private Integer apiId;
    @JsonProperty("occur_time")
    private Date occurTime;

    public static EventLog of() {
        long lateness = DEFAULT_RANDOM.nextInt(10000) * 1000;
        long latenessTime = new Date().getTime() - lateness;

        return EventLog.builder()
                .apiId(API_IDS[new Random().nextInt(6)])
                .occurTime(new Date(latenessTime))
                .build();
    }

}
