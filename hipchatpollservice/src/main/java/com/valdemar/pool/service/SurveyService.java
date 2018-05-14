package com.valdemar.pool.service;

import com.valdemar.pool.dao.SurveyRepository;
import com.valdemar.pool.domain.Survey;
import com.valdemar.pool.domain.SurveyStatus;
import com.valdemar.pool.exceptions.SurveyNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository repository;

    public Survey save(Survey survey){
        return repository.save(survey);
    }

    public List<Survey> findAll(){
        return repository.findAll();
    }

    public List<Survey> getAllLiveSurvey(){
        return repository.findBySurveyStatus(SurveyStatus.LIVE);
    }

    public List<Survey> findByUserId(String userId) {
        return repository.findByAuthor(userId);
    }

    public Survey vote(String surveyId, String questionId) throws SurveyNotFound {
        Optional<Survey> survey = repository.findById(surveyId);

        if(!survey.isPresent()){
            throw new SurveyNotFound(surveyId);
        }

        survey.get().

    }
}
