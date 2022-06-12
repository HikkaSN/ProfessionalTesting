package com.example.professionaltesting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.professionaltesting.R;
import com.example.professionaltesting.databinding.ActivityQuestionBinding;
import com.example.professionaltesting.model.Question;
import com.example.professionaltesting.viewmodel.QuestionViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private ActivityQuestionBinding binding;
    private QuestionViewModel viewModel;

    private int currentQuestionNumber = 1;

    private Question currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        setContentView(binding.getRoot());

        setListeners();
        setObservers();
    }

    private void setObservers() {
        viewModel.getQuestionList().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {

            }
        });

        viewModel.getCurrentQuestion().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                currentQuestion = question;
                binding.textViewQuestion.setText("Вопрос: " + question.getTitle());
                binding.firstButton.setText(question.getFirstAnswer());
                binding.secondButton.setText(question.getSecondAnswer());
                binding.thirdButton.setText(question.getThirdAnswer());
                binding.fourthButton.setText(question.getFourthAnswer());

                binding.textNumberQuestion.setText("Вопрос " + currentQuestionNumber);
                currentQuestionNumber += 1;
            }
        });

        viewModel.getIsFinish().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                finish();
            }
        });

        viewModel.getAlertMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                AlertDialog alertDialog = new AlertDialog.Builder(QuestionActivity.this).create();
                alertDialog.setTitle("Результат");
                alertDialog.setMessage(s);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                finish();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void setListeners(){
        binding.firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentAnswer(currentQuestion.getTitle(),binding.secondButton.getText().toString());
            }
        });

        binding.secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentAnswer(currentQuestion.getTitle(),binding.secondButton.getText().toString());
            }
        });

        binding.thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentAnswer(currentQuestion.getTitle(),binding.thirdButton.getText().toString());
            }
        });

        binding.fourthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCurrentAnswer(currentQuestion.getTitle(),binding.fourthButton.getText().toString());
            }
        });
    }

}