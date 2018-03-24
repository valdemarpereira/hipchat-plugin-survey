package com.valdemar.model;

import com.google.gson.annotations.SerializedName;

public enum LozengeType {

    @SerializedName("default")
    DEFAULT,
    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR,
    @SerializedName("current")
    CURRENT,
    @SerializedName("new")
    NEW,
    @SerializedName("complete")
    COMPLETE,
    @SerializedName("moved")
    MOVED;

}
