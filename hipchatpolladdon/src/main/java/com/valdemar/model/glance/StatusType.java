package com.valdemar.model.glance;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusType {

    @JsonProperty("lozenge")
    LOZENGE,
    @JsonProperty("icon")
    ICON
}
