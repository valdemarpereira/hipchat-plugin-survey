package com.valdemar.model.Survey;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

public class SurveyQuestion {

    @Id
    private String id;
    private String question;
    private boolean enabled; // not used atm
    private List<SurveyAnswer> answers;

    public SurveyQuestion() {
    }

    public SurveyQuestion(String id, String question, boolean enabled, List<SurveyAnswer> answers) {
        this.id = id;
        this.question = question;
        this.enabled = enabled;
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }
}
