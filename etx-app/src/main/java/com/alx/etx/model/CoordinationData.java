package com.alx.etx.model;

import com.alx.etx.data.CoordinationState;
import lombok.Data;

import java.time.OffsetDateTime;

public final @Data class CoordinationData {
    private String id;
    private String businessKey;
    private String applicationId;
    private OffsetDateTime createTime;
    private OffsetDateTime endTime;
    private OffsetDateTime timeout;
    private CoordinationState state;
}
