package com.alx.etx.model;

import com.alx.etx.data.CoordinationState;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public final @Data class CoordinationData {
    private String id;
    private String businessKey;
    private String applicationId;
    private OffsetDateTime createTime;
    private OffsetDateTime endTime;
    private OffsetDateTime timeout;
    private CoordinationState state;
}
