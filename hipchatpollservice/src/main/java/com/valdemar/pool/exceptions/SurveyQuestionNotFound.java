package com.valdemar.pool.exceptions;


public class SurveyQuestionNotFound extends BaseSurveyException {

    public SurveyQuestionNotFound(String surveyId, String questionId) {
        super(String.format("Question with ID '%s' from Survey with the ID '%s' was not found", questionId, surveyId));
    }
}
