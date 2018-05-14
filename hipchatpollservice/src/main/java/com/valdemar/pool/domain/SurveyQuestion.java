package com.valdemar.pool.domain;

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

    public SurveyQuestion(String question, boolean enabled, List<SurveyAnswer> answers) {
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

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof SurveyQuestion)) return false;
        SurveyQuestion that = (SurveyQuestion) o;
        return enabled == that.enabled &&
                Objects.equals(id, that.id) &&
                Objects.equals(question, that.question) &&
                Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, question, enabled, answers);
    }

    @Override
    public String toString() {
        return "SurveyQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", enabled=" + enabled +
                ", answers=" + answers +
                '}';
    }
}
