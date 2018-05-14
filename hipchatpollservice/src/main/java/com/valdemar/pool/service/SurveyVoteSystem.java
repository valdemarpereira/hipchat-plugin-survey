package com.valdemar.pool.service;

import com.valdemar.pool.domain.Survey;
import com.valdemar.pool.domain.SurveyQuestion;
import com.valdemar.pool.exceptions.SurveyQuestionNotFound;

import java.util.List;
import java.util.Optional;

public class SurveyVoteSystem {


    public Survey vote(Survey survey, String questionId, String userId) throws SurveyQuestionNotFound {

        SurveyQuestion question = getQuestion(survey, questionId);

        long numberOfVotes = survey.getQuestions().stream().filter(q -> q.getAnswers().stream().anyMatch(a -> a.getUser().equals(userId))).count();

        if(numberOfVotes >= survey.getSurveyType().getNumberOfAnswersAllowed()){
            //throw new SurveyAnswerNotAllowed(survey.getId(), questionId, );
        }

        return null;

    }

    private SurveyQuestion getQuestion(Survey survey, String questionId) throws SurveyQuestionNotFound {
        List<SurveyQuestion> questions = survey.getQuestions();

        Optional<SurveyQuestion> question = questions.stream().filter(f -> f.getId().equals(questionId)).findFirst();

        if (!question.isPresent()){
            throw new SurveyQuestionNotFound(survey.getId(), questionId);
        }

        return question.get();
    }
}
