package com.valdemar.model;

public class Installable {

    private String capabilitiesUrl;
    private String oauthId;
    private String oauthSecret;
    private int groupId;
    private int roomId;


    public Installable(String capabilitiesUrl, String oauthId, String oauthSecret, int groupId, int roomId) {
        this.capabilitiesUrl = capabilitiesUrl;
        this.oauthId = oauthId;
        this.oauthSecret = oauthSecret;
        this.groupId = groupId;
        this.roomId = roomId;
    }

    public Installable() {}

    public String getCapabilitiesUrl() {
        return capabilitiesUrl;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getOauthSecret() {
        return oauthSecret;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getRoomId() {
        return roomId;
    }
}
