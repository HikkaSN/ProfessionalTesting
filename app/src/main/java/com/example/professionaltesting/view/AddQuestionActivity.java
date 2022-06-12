package com.example.professionaltesting.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.professionaltesting.databinding.ActivityQuestionBinding;

public class AddQuestionActivity extends AppCompatActivity {

    private ActivityQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }
}