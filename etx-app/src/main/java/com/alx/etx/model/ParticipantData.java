package com.alx.etx.model;

import com.alx.etx.data.ParticipantState;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public @Data class ParticipantData {
    private String id;
    private String name;
    private String payload;
    private String callbackUrl;
    private String callbackToken;
    private OffsetDateTime joinTime;
    private OffsetDateTime executeTime;
    private OffsetDateTime confirmTime;
    private OffsetDateTime cancelTime;
    private ParticipantState state;
}
