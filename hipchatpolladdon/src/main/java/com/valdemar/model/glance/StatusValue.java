package com.valdemar.model.glance;

import com.valdemar.model.LozengeType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class StatusValue {

    @XmlElement
    private String label;
    @XmlElement
    private LozengeType type;
    @XmlElement
    private String url;
    @XmlElement(name = "url@2x")
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
}
