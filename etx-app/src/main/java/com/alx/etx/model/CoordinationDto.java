package com.alx.etx.model;

import java.time.temporal.ChronoUnit;

public class CoordinationDto {

    private String id;
    private Integer timeout;
    private ChronoUnit timeoutUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public ChronoUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public void setTimeoutUnit(ChronoUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }
}
