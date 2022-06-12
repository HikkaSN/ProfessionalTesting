package com.example.professionaltesting.services;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.professionaltesting.model.Question;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<Boolean> successUpdate = new MutableLiveData<>();

    public void createQuestion(Question question){
        db.collection("question")
                .add(question);
    }

    public void updateData(Question question, String uidQuestionToUpdate){
        db.collection("question").document(uidQuestionToUpdate)
                .update(
                        "title", question.getTitle(),
                "first_answer", question.getFirstAnswer(),
                "second_answer", question.getSecondAnswer(),
                "third_answer", question.getThirdAnswer(),
                "fourth_answer", question.getFourthAnswer());

        successUpdate.setValue(true);

    }

    public MutableLiveData<List<Question>> getQuestions() {

        MutableLiveData<List<Question>> questions = new MutableLiveData<>();

        db.collection("question").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("ERROR:", "Listen failed.", error);
                    return;
                }

                List<Question> questoins = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Question question = new Question();

                    question.setUid(doc.getId());

                    if (doc.get("title") != null)
                        question.setTitle((String) doc.get("title"));
                    if(doc.get("first_answer") != null)
                        question.setFirstAnswer((String) doc.get("first_answer"));
                    if(doc.get("second_answer") != null)
                        question.setSecondAnswer((String) doc.get("second_answer"));
                    if(doc.get("third_answer") != null)
                        question.setThirdAnswer((String) doc.get("third_answer"));
                    if(doc.get("fourth_answer") != null)
                        question.setFourthAnswer((String) doc.get("fourth_answer"));
                    questoins.add(question);
                }

                questions.setValue(questoins);
            }
        });

        return questions;
    }

    public MutableLiveData<Boolean> getSuccessUpdate() {
        return successUpdate;
    }
}
