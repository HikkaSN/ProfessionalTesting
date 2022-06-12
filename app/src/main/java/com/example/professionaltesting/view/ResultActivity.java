package com.example.professionaltesting.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.professionaltesting.adapter.RecycleViewAdapter;
import com.example.professionaltesting.adapter.ResultViewAdapter;
import com.example.professionaltesting.databinding.ActivityResultBinding;
import com.example.professionaltesting.model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private ResultViewAdapter adapter;
    private Map<Integer, Map<String, Object>> results = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setResult();

    }

    private void setResult() {
        FirebaseFirestore.getInstance().collection("result")
                .whereEqualTo("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int count = 0;
                        for (QueryDocumentSnapshot doc : value) {

                            Map<String, Object> map = doc.getData();

                            map.remove("userUid");

                            results.put(count, map);
                            count += 1;
                        }

                        System.out.println(results);
                        setAdapter();
                    }
                });


    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycleView.setLayoutManager(linearLayoutManager);

        adapter = new ResultViewAdapter();
        binding.recycleView.setAdapter(adapter);

        adapter.setItems(results);
    }
}