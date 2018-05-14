package com.valdemar.pool.domain;

public class SurveyType {

    private int numberOfAnswersAllowed;

    public SurveyType() {
    }

    public SurveyType(int numberOfAnswersAllowed) {
        this.numberOfAnswersAllowed = numberOfAnswersAllowed;
    }

    public static SurveyType ofSingleAnswer(){
        return new SurveyType(0);
    }

    public static SurveyType ofMultipleAnswers(int numOfAllowedAnswers){
        return new SurveyType(numOfAllowedAnswers);
    }

    public int getNumberOfAnswersAllowed() {
        return numberOfAnswersAllowed;
    }

    public void setNumberOfAnswersAllowed(int numberOfAnswersAllowed) {
        this.numberOfAnswersAllowed = numberOfAnswersAllowed;
    }

    public boolean isSingleVote(){
        return numberOfAnswersAllowed <= 1;
    }
}
