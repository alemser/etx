package com.alx.etx.client;

public class Participant {
    private String name;
    private String payload;
    private String callbackUrl;
    private String callbackToken;
    private ParticipantState state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackToken() {
        return callbackToken;
    }

    public void setCallbackToken(String callbackToken) {
        this.callbackToken = callbackToken;
    }

    public void setState(ParticipantState state) {
        this.state = state;
    }

    public ParticipantState getState() {
        return state;
    }

    /**
     * Not using any api for json parsing due to simplicity and avoid conflicts for users.
     *
     * @return the json representation of the participant.
     */
    String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append(getField("state", getState().name()));
        json.append(getField("callbackToken", getCallbackToken()));
        json.append(getField("callbackUrl", getCallbackUrl()));
        json.append(getField("payload", getPayload()));
        json.append(getField("name", getName()));
        String str = json.substring(0, json.length()-1);
        return str + "}";
    }

    private String getField(String name, String value) {
        if (value != null) {
            return "\"" + name + "\" : \"" + value + "\",";
        }
        return "";
    }
}
