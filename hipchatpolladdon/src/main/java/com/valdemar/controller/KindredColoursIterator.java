package com.valdemar.controller;

public class KindredColoursIterator {

    int counter = -1;

    public String next(){
        counter = (++counter) % 5;
        return KindredColours.values()[counter].name();
    }

    private enum KindredColours{
        blue,
        orange,
        pink,
        green,
        purple
    }
}
