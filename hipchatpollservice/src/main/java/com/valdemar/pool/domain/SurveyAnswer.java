package com.valdemar.pool.domain;

import java.util.Objects;

public class SurveyAnswer {

    private String user;

    public SurveyAnswer() { }

    public SurveyAnswer(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyAnswer)) return false;
        SurveyAnswer that = (SurveyAnswer) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return "SurveyAnswer{" +
                "user=" + user +
                '}';
    }
}
