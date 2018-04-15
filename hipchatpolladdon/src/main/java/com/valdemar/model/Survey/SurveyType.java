package com.valdemar.model.Survey;

public class SurveyType {

    private int numberOfAnswersAllowed;

    public SurveyType() {
    }

    public SurveyType(int numberOfAnswersAllowed) {
        this.numberOfAnswersAllowed = numberOfAnswersAllowed;
    }

    public int getNumberOfAnswersAllowed() {
        return numberOfAnswersAllowed;
    }
}
