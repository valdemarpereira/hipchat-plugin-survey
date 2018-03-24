package com.valdemar.model.glance;

import com.valdemar.model.LozengeType;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class GlanceStatus {

    private StatusType type;
    private StatusValue value;

    private GlanceStatus(StatusType type, StatusValue value) {
        this.type = type;
        this.value = value;
    }

    private GlanceStatus() {
        //jaxb
    }

    public static GlanceStatus ofIcon(String url, String url2x){
        return new GlanceStatus(StatusType.ICON, StatusValue.ofIcon(url,url2x));
    }

    public static GlanceStatus ofLozenge(LozengeType type, String label){
        return new GlanceStatus(StatusType.LOZENGE, StatusValue.ofLozenge(type, label));
    }
}
