package com.example.professionaltesting.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.professionaltesting.adapter.AdapterItemClickListener;
import com.example.professionaltesting.adapter.RecycleViewAdapter;
import com.example.professionaltesting.databinding.ActivityCheckQuestionBinding;
import com.example.professionaltesting.databinding.ActivityQuestionBinding;
import com.example.professionaltesting.model.Question;
import com.example.professionaltesting.viewmodel.CheckQuestionViewModel;
import com.google.gson.Gson;

import java.util.List;

public class CheckQuestionActivity extends AppCompatActivity implements AdapterItemClickListener {

    private RecycleViewAdapter adapter;
    private ActivityCheckQuestionBinding binding;

    private CheckQuestionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCheckQuestionBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(CheckQuestionViewModel.class);

        setContentView(binding.getRoot());

        setAdapter();
        setObservers();
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycleView.setLayoutManager(linearLayoutManager);

        adapter = new RecycleViewAdapter(this);
        binding.recycleView.setAdapter(adapter);
    }

    private void setObservers() {
        viewModel.getQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                adapter.clearItems();
                adapter.setItems(questions);
            }
        });
    }


    @Override
    public void onItemClicked(Question question) {
        Intent intent = new Intent(this, ChangeDataQuestionActivity.class);
        Gson gson = new Gson();
        intent.putExtra("question", gson.toJson(question));
        startActivity(intent);
    }
}