package com.valdemar.model.glance;

public final class Label {

    private LabelType type;
    private String value;


    public Label(LabelType type, String label) {

        if(label != null && (label.length() < 1 || label.length() > 20)){
            throw new IllegalArgumentException("Invalid label range: Valid length range: 1 - 20.");
        }

        this.type = type;
        this.value = label;
    }

    private Label() {
        //jaxb
    }

    public static Label of(LabelType labelType, String value){
        return new Label(labelType, value);
    }

    public static Label ofHtml(String value){
        return new Label(LabelType.HTML, value);
    }

    public LabelType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
