package com.valdemar.pool.exceptions;

public class BaseSurveyException extends Exception {

    private String message;

    public BaseSurveyException(String message) {
        super(message);
    }
}
