package com.example.professionaltesting.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.professionaltesting.R;
import com.example.professionaltesting.databinding.ActivityProfileBinding;
import com.example.professionaltesting.model.Question;
import com.example.professionaltesting.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private String urlPathUserPhoto;

    private ActivityProfileBinding binding;
    public static final int CAMERA_ACTION_RESULT = 1;

    private Bitmap bitmap;

    FirebaseStorage storage;
    StorageReference storageReference;

    private String userFIO;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        setListeners();
        setUI();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        setContentView(binding.getRoot());
    }

    private void setUI(){

        //User user =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        //binding.profileImage.setImageDrawable(drawableFromUrl());

        String userUID = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("users").document(userUID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    User user = document.toObject(User.class);

                    userEmail = user.getEmail();
                    userFIO = user.getFio();

                    binding.editTextEmail.setText(userEmail);
                    binding.editTextFIO.setText(userFIO);
                    Picasso picasso = Picasso.get();

                    picasso
                            .load(user.getUserImage())
                            .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                            .into(binding.profileImage);

                }
            }
        });
    }

    private void setListeners() {
        binding.buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_ACTION_RESULT);
            }
        });

        binding.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validation())
                    return;

                if(userEmail != null)
                    updateEmail();
                if(bitmap != null)
                    uploadPhoto();

                updatePassword();
                updateUserData();
            }
        });
    }

    private void updatePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(binding.editTextPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
    }

    private void updateEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("users").document(user.getUid());


        user.updateEmail(binding.editTextEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                documentReference.update("email", binding.editTextEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                    }
                });
            }
        });
    }

    private void updateUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("users").document(user.getUid());


        Map<String,Object> updates = new HashMap<>();
        if(userFIO != null)
            updates.put("FIO", binding.editTextFIO.getText().toString());
        if(bitmap != null)
            updates.put("userImage", urlPathUserPhoto);

        documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
            }
        });
    }

    private void uploadPhoto() {
        if (bitmap == null)
            return;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInBytes = baos.toByteArray();

        StorageReference filepath = storageReference
                .child(UUID.randomUUID() + ".jpg");

        UploadTask uploadTask = filepath.putBytes(imageInBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        urlPathUserPhoto = uri.toString();
                        updateUserData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ProfileActivity.this, "BAD " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CAMERA_ACTION_RESULT && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto  = (Bitmap)bundle.get("data");
            this.bitmap = finalPhoto;
            binding.profileImage.setImageBitmap(finalPhoto);

        }
    }


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean validation() {
        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(binding.editTextEmail.getText().toString());
        if (emailMatcher.find() == false) {
            Toast.makeText(this, "Почта неверного формата", Toast.LENGTH_LONG).show();
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
        }

        return true;
    }
}