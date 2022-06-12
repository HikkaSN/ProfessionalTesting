package com.example.professionaltesting.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

public class Question {
    @DocumentId
    private String uid;
    @PropertyName("title")
    private String title;
    @PropertyName("first_answer")
    private String firstAnswer;

    @PropertyName("second_answer")
    private String secondAnswer;

    @PropertyName("third_answer")
    private String thirdAnswer;


    @PropertyName("fourth_answer")
    private String fourthAnswer;

    @DocumentId
    public String getUid() {
        return uid;
    }

    @DocumentId
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("first_answer")
    public String getFirstAnswer() {
        return firstAnswer;
    }

    @PropertyName("first_answer")
    public void setFirstAnswer(String firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    @PropertyName("second_answer")
    public String getSecondAnswer() {
        return secondAnswer;
    }

    @PropertyName("second_answer")
    public void setSecondAnswer(String secondAnswer) {
        this.secondAnswer = secondAnswer;
    }

    @PropertyName("third_answer")
    public String getThirdAnswer() {
        return thirdAnswer;
    }

    @PropertyName("third_answer")
    public void setThirdAnswer(String thirdAnswer) {
        this.thirdAnswer = thirdAnswer;
    }

    @PropertyName("fourth_answer")
    public String getFourthAnswer() {
        return fourthAnswer;
    }

    @PropertyName("fourth_answer")
    public void setFourthAnswer(String fourthAnswer) {
        this.fourthAnswer = fourthAnswer;
    }
}
