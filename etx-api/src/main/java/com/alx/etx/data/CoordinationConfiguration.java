package com.alx.etx.data;

import lombok.Data;

import java.time.temporal.ChronoUnit;

public @Data class CoordinationConfiguration {
    /**
     * Amount of time that an inconsistent coordination will wait until produce a signal to cancel all participants.
     * Default is 60 seconds (60000 milliseconds)
     */
    public static Long DEFAULT_TIMEOUT = 60000L;
    public static ChronoUnit DEFAULT_TIMEOUT_UNIT = ChronoUnit.MILLIS;

    private Long timeout = DEFAULT_TIMEOUT;
    private ChronoUnit timeoutUnit = DEFAULT_TIMEOUT_UNIT;
    private String businessKey;
    private String applicationId;

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
