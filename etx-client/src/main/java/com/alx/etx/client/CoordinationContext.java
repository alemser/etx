package com.alx.etx.client;

/**
 * Temporary container for thread local information for a coordination.
 * To be replaced by something more sofisticated.
 */
public class CoordinationContext {

    private static ThreadLocal<CoordinationInfo> coordTL = new ThreadLocal<>();

    private static class CoordinationInfo {
        private String coordinationId;
        private String participantId;

        public CoordinationInfo(String coordinationId, String participantId) {
            this.coordinationId = coordinationId;
            this.participantId = participantId;
        }
    }

    public static String getCurrentCoordinationId() {
        return initiated() ? coordTL.get().coordinationId : null;
    }

    public static String getCurrentParticipantId() {
        return initiated() ? coordTL.get().participantId : null;
    }

    public static void initiate(String coordinationId, String partiipantId) {
        coordTL.set(new CoordinationInfo(coordinationId, partiipantId));
    }

    public static boolean initiated() {
        return  coordTL.get() != null;
    }
}
