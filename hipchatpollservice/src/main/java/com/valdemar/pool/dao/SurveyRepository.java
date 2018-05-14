package com.valdemar.pool.dao;

import com.valdemar.pool.domain.Survey;
import com.valdemar.pool.domain.SurveyStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SurveyRepository extends MongoRepository<Survey, String> {

    public List<Survey> findByAuthor(String author);
    public List<Survey> findBySurveyStatus(SurveyStatus surveyStatus);
}
