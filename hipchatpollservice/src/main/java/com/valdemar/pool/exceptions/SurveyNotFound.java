package com.valdemar.pool.exceptions;

public class SurveyNotFound extends BaseSurveyException {

    public SurveyNotFound(String surveyId) {
        super(String.format("Survey with the ID '%s' was not found", surveyId));
    }
}
