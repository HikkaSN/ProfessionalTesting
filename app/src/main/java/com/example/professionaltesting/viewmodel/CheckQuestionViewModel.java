package com.example.professionaltesting.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.professionaltesting.model.Question;
import com.example.professionaltesting.services.QuestionService;

import java.util.List;

public class CheckQuestionViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions = new MutableLiveData<List<Question>>();
    private QuestionService questionService = new QuestionService();


    public CheckQuestionViewModel(){
        questions = questionService.getQuestions();
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }
}
