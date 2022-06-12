package com.example.professionaltesting.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.professionaltesting.model.Question;
import com.example.professionaltesting.services.QuestionService;

public class ChangeDataQuestionViewModel extends ViewModel {

    private MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<String> errorString = new MutableLiveData<>();
    private MutableLiveData<Boolean> successUpdate = new MutableLiveData<>();
    private QuestionService questionService = new QuestionService();

    public void updateQuestion(Question question, String uid){
        questionService.updateData(question, uid);

        successUpdate = questionService.getSuccessUpdate();
    }

    public void createQuestion(Question question) {
        questionService.createQuestion(question);
    }


    public MutableLiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }

    public MutableLiveData<String> getErrorString() {
        return errorString;
    }

    public MutableLiveData<Boolean> getSuccessUpdate() {
        return successUpdate;
    }
}
