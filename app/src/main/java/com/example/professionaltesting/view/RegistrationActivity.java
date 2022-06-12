package com.example.professionaltesting.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.professionaltesting.databinding.ActivityQuestionBinding;
import com.example.professionaltesting.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        firestore = FirebaseFirestore.getInstance();

        setContentView(binding.getRoot());

        setListener();
    }

    private void setListener() {

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validation())
                    return;

                mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(binding.editTextEmail.getText().toString(),
                        binding.editTextPassword.getText().toString()).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Map<String, Object> user = new HashMap<>();
                        user.put("email", binding.editTextEmail.getText().toString());
                        user.put("isAdmin", false);
                        user.put("FIO", binding.editTextFIO.getText().toString());
                        user.put("userImage", "");

                        firestore.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .set(user);

                        setResult(MenuActivity.RESULT_OK);
                        finish();
                    }
                });
            }
        });
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean validation(){

        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(binding.editTextEmail.getText().toString());

        if(binding.editTextFIO.getText().toString().equals("")){
            Toast.makeText(this, "Введите ФИО", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.editTextEmail.getText().toString().equals("")){
            Toast.makeText(this, "Введите почту", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.editTextPassword.getText().toString().equals("")){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show();
            return false;
        } else if (binding.editTextRepeatPassword.getText().toString().equals("")){
            Toast.makeText(this, "Введите повтор пароля", Toast.LENGTH_LONG).show();
            return false;
        } else if (!binding.editTextPassword.getText().toString().equals(binding.editTextRepeatPassword.getText().toString())){
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
            return false;
        } else if (emailMatcher.find() == false) {
            Toast.makeText(this, "Почта неверного формата", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }
}