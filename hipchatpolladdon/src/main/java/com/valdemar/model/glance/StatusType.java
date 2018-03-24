package com.valdemar.model.glance;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum StatusType {

    @SerializedName("lozenge")
    LOZENGE,
    @SerializedName("icon")
    ICON
}
