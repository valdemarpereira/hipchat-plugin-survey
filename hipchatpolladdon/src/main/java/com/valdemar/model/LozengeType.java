package com.valdemar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LozengeType {

    @JsonProperty("default")
    DEFAULT,
    @JsonProperty("success")
    SUCCESS,
    @JsonProperty("error")
    ERROR,
    @JsonProperty("current")
    CURRENT,
    @JsonProperty("new")
    NEW,
    @JsonProperty("complete")
    COMPLETE,
    @JsonProperty("moved")
    MOVED;

}
