package com.valdemar.model.glance;

import com.valdemar.model.LozengeType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
https://www.hipchat.com/docs/apiv2/glances?_ga=2.236925572.44988695.1521544983-2109851313.1521114702
 */
public final class Glance {

    private Label label;
    // private final Metadata metadata;
    private GlanceStatus status;

    private Glance(Label label, GlanceStatus status) {
        this.label = label;
        this.status = status;
    }

    private Glance() {
        //jaxb
    }

    public static Glance ofIcon(String label, String url, String url2x){
        return new Glance(Label.ofHtml(label), GlanceStatus.ofIcon(url, url2x));
    }

    public static Glance ofLozenge(String label, LozengeType type, String lozengeLabel){
        return new Glance(Label.ofHtml(label), GlanceStatus.ofLozenge(type, lozengeLabel));
    }

}
