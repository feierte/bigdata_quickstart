package org.sperri.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Jie Zhao
 * @date 2022/5/2 21:26
 */
@Data
@Builder
public class AccessLog {

    @JsonProperty("api_id")
    private Integer apiId;
    @JsonProperty("occur_time")
    private Date occurTime;

}
