package com.alx.etx.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public @Data class CoordinationConfigData {
    private Integer timeout;
    private ChronoUnit timeoutUnit;
    private String businessKey;
    private String applicationId;
}
