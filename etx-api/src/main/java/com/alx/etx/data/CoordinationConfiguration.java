package com.alx.etx.data;

import java.time.temporal.ChronoUnit;

public class CoordinationConfiguration {
    /**
     * Amount of time that an inconsistent coordination will wait until produce a signal to cancel all participants.
     * Default is 60 seconds (60000 milliseconds)
     */
    private Long timeout = 60000L;
    private ChronoUnit timeoutUnit;

    public ChronoUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public void setTimeoutUnit(ChronoUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeoutInMillis) {
        this.timeout = timeoutInMillis;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CoordinationConfiguration cfg = new CoordinationConfiguration();

        public Builder timeoutSec(Long value) {
            return timeout(value, ChronoUnit.SECONDS);
        }

        public Builder timeoutMin(Long value) {
            return timeout(value, ChronoUnit.MINUTES);
        }

        public Builder timeoutHours(Long value) {
            return timeout(value, ChronoUnit.HOURS);
        }

        public Builder timeout(Long value, ChronoUnit chronoUnit) {
            cfg.setTimeout(value);
            cfg.setTimeoutUnit(chronoUnit);
            return this;
        }

        public CoordinationConfiguration build() {
            return cfg;
        }
    }
}
