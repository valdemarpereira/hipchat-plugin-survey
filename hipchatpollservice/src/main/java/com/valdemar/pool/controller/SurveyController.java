package com.valdemar.pool.controller;

import com.valdemar.pool.domain.Survey;
import com.valdemar.pool.resource.Message;
import com.valdemar.pool.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

//    @GetMapping("/")
//    public HttpEntity<Message> getAllAvailableSurveys() {
//        List<Survey> surveys = surveyService.findAll();
//        return new ResponseEntity<>(Message.ofOK(surveys), OK);
//    }
//
//    @PostMapping("/{userId}")
//    public HttpEntity<Message> addNewSurvey(@PathVariable("userId") String userId, @RequestBody Survey survey) {
//        Survey savedSurvey = surveyService.save(survey);
//
//        return new ResponseEntity<>(Message.ofOK(savedSurvey), OK);
//    }


    @GetMapping("/")
    public HttpEntity<List<Survey>> getAllAvailableSurveys() {
        List<Survey> surveys = surveyService.findAll();
        return new ResponseEntity<>(surveys, OK);
    }

    @GetMapping("/{userId}")
    public HttpEntity<List<Survey>> getAllAvailableSurveysByUser(@PathParam("userId") String userId) {
        List<Survey> surveys = surveyService.findByUserId(userId);
        return new ResponseEntity<>(surveys, OK);
    }

    //TODO: Implement security
    @PostMapping(path="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Survey> addNewSurvey( @RequestBody Survey survey) {
        Survey savedSurvey = surveyService.save(survey);

        return new ResponseEntity<>(savedSurvey, OK);
    }
}
