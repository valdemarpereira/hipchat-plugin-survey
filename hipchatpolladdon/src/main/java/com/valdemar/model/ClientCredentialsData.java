package com.valdemar.model;


import org.springframework.data.annotation.Id;

import java.util.Objects;

//TODO: Make this immutable and a builder
public class ClientCredentialsData {

    private String capabilitiesUrl;
    @Id
    private String oauthId;
    private String oauthSecret;
    private int groupId;
    private int roomId;
    private String tokenEndpointUrl;
    private String apiEndpointUrl;


    public String getCapabilitiesUrl() {
        return capabilitiesUrl;
    }

    public void setCapabilitiesUrl(String capabilitiesUrl) {
        this.capabilitiesUrl = capabilitiesUrl;
    }

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public String getOauthSecret() {
        return oauthSecret;
    }

    public void setOauthSecret(String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getTokenEndpointUrl() {
        return tokenEndpointUrl;
    }

    public void setTokenEndpointUrl(String tokenEndpointUrl) {
        this.tokenEndpointUrl = tokenEndpointUrl;
    }

    public String getApiEndpointUrl() {
        return apiEndpointUrl;
    }

    public void setApiEndpointUrl(String apiEndpointUrl) {
        this.apiEndpointUrl = apiEndpointUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientCredentialsData that = (ClientCredentialsData) o;
        return groupId == that.groupId &&
                roomId == that.roomId &&
                Objects.equals(capabilitiesUrl, that.capabilitiesUrl) &&
                Objects.equals(oauthId, that.oauthId) &&
                Objects.equals(oauthSecret, that.oauthSecret) &&
                Objects.equals(tokenEndpointUrl, that.tokenEndpointUrl) &&
                Objects.equals(apiEndpointUrl, that.apiEndpointUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capabilitiesUrl, oauthId, oauthSecret, groupId, roomId, tokenEndpointUrl, apiEndpointUrl);
    }

    @Override
    public String toString() {
        return "ClientCredentialsData{" +
                "capabilitiesUrl='" + capabilitiesUrl + '\'' +
                ", oauthId='" + oauthId + '\'' +
                ", oauthSecret='" + oauthSecret + '\'' +
                ", groupId=" + groupId +
                ", roomId=" + roomId +
                ", tokenEndpointUrl='" + tokenEndpointUrl + '\'' +
                ", apiEndpointUrl='" + apiEndpointUrl + '\'' +
                '}';
    }
}
