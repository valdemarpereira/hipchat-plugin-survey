package com.valdemar.model.Survey;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Survey {

    private String id;
    private String title;
    private String author;
    private String userId;
    private List<SurveyQuestion> questions;
    private SurveyType surveyType;
    private Date createDate;
    private Date updateDate;
    private Date endDate;
    private SurveyStatus surveyStatus;

    public Survey(String id, String title, String author, String userId, List<SurveyQuestion> questions, SurveyType surveyType, Date createDate, Date updateDate, Date endDate, SurveyStatus surveyStatus) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.userId = userId;
        this.questions = questions;
        this.surveyType = surveyType;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.endDate = endDate;
        this.surveyStatus = surveyStatus;
    }

    public Survey() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    public SurveyType getSurveyType() {
        return surveyType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SurveyStatus getSurveyStatus() {
        return surveyStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
