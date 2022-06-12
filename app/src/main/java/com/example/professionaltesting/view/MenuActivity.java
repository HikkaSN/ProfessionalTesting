package com.example.professionaltesting.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.professionaltesting.model.User;
import com.example.professionaltesting.databinding.ActivityMenuBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;
    private FirebaseAuth mAuth;

    private boolean isAdmin;

    public static final int ADD_NEW_USER_CODE = 1;
    public static final int FINISH_TESTING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();

        setContentView(binding.getRoot());

        checkRole();
    }

    private void setListener() {
        if(isAdmin) {

            binding.firstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, CheckQuestionActivity.class);
                    startActivity(intent);
                }
            });

            binding.secondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, RegistrationActivity.class);
                    startActivityForResult(intent, ADD_NEW_USER_CODE);
                }
            });

            binding.buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, ChangeDataQuestionActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            binding.firstButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, QuestionActivity.class);
                    startActivity(intent);
                }
            });

            binding.secondButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, ResultActivity.class);
                    startActivity(intent);
                }
            });
        }

        binding.buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkRole(){
        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users")
                .document(user.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userFromDB = documentSnapshot.toObject(User.class);

                if (userFromDB.isAdmin()){
                    binding.firstButton.setText("Редактировать запросы");
                    binding.secondButton.setText("Зарегестрировать навого пользователя");
                    isAdmin = true;
                    binding.buttonAddQuestion.setVisibility(View.VISIBLE);
                    binding.buttonAddQuestion.setEnabled(true);
                } else {

                    binding.firstButton.setText("Начать тестирование");
                    binding.secondButton.setText("Предыдущие результаты");
                    isAdmin = false;
                }
                setListener();
            }
        });

        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK) {
            if (requestCode == ADD_NEW_USER_CODE)
                Toast.makeText(this, "Пользователь добавлен", Toast.LENGTH_LONG).show();
            else if (requestCode == FINISH_TESTING)
                Toast.makeText(this, "Тестирование завершенно, спасибо !", Toast.LENGTH_LONG).show();
        }
    }
}