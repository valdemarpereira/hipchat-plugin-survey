package com.valdemar.model.glance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.valdemar.model.LozengeType;

public final class StatusValue {

    private String label;
    private LozengeType type;
    private String url;
    private String url2x;

    private StatusValue() {
        //jaxb
    }

    private StatusValue(String label, LozengeType type, String url, String url2x) {
        this.label = label;
        this.type = type;
        this.url = url;
        this.url2x = url2x;
    }

    public static StatusValue ofLozenge(LozengeType type, String label){

        if(label != null && (label.length() < 1 || label.length() > 20)){
            throw new IllegalArgumentException("Invalid label range: Valid length range: 1 - 20.");
        }

        return new StatusValue(label, type, null, null);
    }

    public static StatusValue ofIcon(String url, String url2x){
        return new StatusValue(null, null, url, url2x);
    }

    public String getLabel() {
        return label;
    }

    public LozengeType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    @JsonProperty("url@2x")
    public String getUrl2x() {
        return url2x;
    }
}
