package com.alx.etx.data;

public class CoordinationConfiguration {
    /**
     * Amount of time that an inconsistent coordination will wait until produce a signal to cancel all participants.
     * Default is 60 seconds (60000 milliseconds)
     */
    private Long timeoutInMillis = 60000L;

    public Long getTimeoutInMillis() {
        return timeoutInMillis;
    }

    public void setTimeoutInMillis(Long timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }
}
