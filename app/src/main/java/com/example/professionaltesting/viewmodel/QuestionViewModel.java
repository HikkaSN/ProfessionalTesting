package com.example.professionaltesting.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.professionaltesting.model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionViewModel extends ViewModel {

    private FirebaseFirestore db;
    private Map<String, Object> result = new HashMap();

    private MutableLiveData<String> currentAnswer = new MutableLiveData<>();
    private MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<List<Question>> questionList = new MutableLiveData<>();

    private MutableLiveData<String> alertMessage = new MutableLiveData<>();

    private MutableLiveData<Boolean> isFinish = new MutableLiveData<>();

    private List<Question> listBuffer = new ArrayList<>();

    private int pos = 0;


    public QuestionViewModel(){

        db = FirebaseFirestore.getInstance();

        db.collection("question")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            listBuffer = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listBuffer.add(document.toObject(Question.class));
                            }

                            startTesting();
                        }
                    }
                });
    }

    public void startTesting(){

        questionList.setValue(listBuffer);
        currentQuestion.setValue(listBuffer.get(0));
    }

    public void goNextAnswer(){
        if(listBuffer.size() != pos){
            currentQuestion.setValue(listBuffer.get(pos));
        } else {


            String alertStr = "";
            for (Map.Entry entry: result.entrySet()) {
                alertStr += entry.getKey() + ": " + entry.getValue();
                alertStr += "\n";
            }
            alertMessage.setValue(alertStr);

            result.put("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid());

            db.collection("result")
                    .add(result);
        }
    }

    public void setCurrentAnswer(String questionTitle, String value){
        currentAnswer.setValue(value);

        result.put(questionTitle, value);

        pos++;

        goNextAnswer();
    }

    public MutableLiveData<Boolean> getIsFinish() {
        return isFinish;
    }

    public MutableLiveData<List<Question>> getQuestionList() {
        return questionList;
    }

    public MutableLiveData<String> getCurrentAnswer() {
        return currentAnswer;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }
}
