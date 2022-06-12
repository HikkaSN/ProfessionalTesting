package com.example.professionaltesting.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

public class User {
    @DocumentId
    private String uid;
    @PropertyName("FIO")
    private String fio;
    @PropertyName("email")
    private String email;
    @PropertyName("isAdmin")
    private boolean isAdmin;
    @PropertyName("userImage")
    private String userImage;

    @DocumentId
    public String getUid() {
        return uid;
    }

    @DocumentId
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @PropertyName("isAdmin")
    public boolean isAdmin() {
        return isAdmin;
    }

    @PropertyName("isAdmin")
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @PropertyName("FIO")
    public String getFio() {
        return fio;
    }

    @PropertyName("FIO")
    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
