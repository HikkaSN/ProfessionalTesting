package com.example.professionaltesting.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.professionaltesting.databinding.ActivityChangeDataQuestionBinding;
import com.example.professionaltesting.model.Question;
import com.example.professionaltesting.viewmodel.ChangeDataQuestionViewModel;
import com.google.gson.Gson;

public class ChangeDataQuestionActivity extends AppCompatActivity {

    private Question currentQuestion;
    private ChangeDataQuestionViewModel viewModel;
    private ActivityChangeDataQuestionBinding binding;

    private boolean isCreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangeDataQuestionBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(ChangeDataQuestionViewModel.class);


        String questionJson = getIntent().getStringExtra("question");


        setContentView(binding.getRoot());

        if(questionJson != null) {
            Gson gson = new Gson();
            currentQuestion = gson.fromJson(questionJson, Question.class);
            setUI();
        } else {
            binding.toolbarHeader.setText("Создание вопроса");
            isCreate = true;
        }

        setListener();
        setObserver();
    }

    private void setObserver(){
        viewModel.getSuccessUpdate().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    finish();
            }
        });
    }

    private void setListener(){
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Question question = new Question();
                question.setTitle(binding.editTextQuestionTitle.getText().toString());
                question.setFirstAnswer(binding.editTextFirstAnswer.getText().toString());
                question.setSecondAnswer(binding.editTextSecondAnswer.getText().toString());
                question.setThirdAnswer(binding.editTextThirdAnswer.getText().toString());
                question.setFourthAnswer(binding.editTextFourthAnswer.getText().toString());

                if(!isCreate) {
                    viewModel.updateQuestion(question, currentQuestion.getUid());

                    finish();
                } else {
                    viewModel.createQuestion(question);
                    finish();
                }
            }
        });
    }


    private void setUI() {
        binding.editTextQuestionTitle.setText(currentQuestion.getTitle());
        binding.editTextFirstAnswer.setText(currentQuestion.getFirstAnswer());
        binding.editTextSecondAnswer.setText(currentQuestion.getThirdAnswer());
        binding.editTextThirdAnswer.setText(currentQuestion.getThirdAnswer());
        binding.editTextFourthAnswer.setText(currentQuestion.getFourthAnswer());
    }


}