package com.example.zalpia.ui.myaccount;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.UserModell;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class myAccountVIewModel extends ViewModel {
    MutableLiveData<UserModell> userModel;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TAG = "myAccountVIewModel1";

    public myAccountVIewModel() {
        userModel=new MutableLiveData<>();

    }

    public LiveData<UserModell> getUserData(){
        return userModel;
    }

    public void setUserData(){
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()&&!Objects.requireNonNull(task.getResult().getData()).isEmpty()) {
                        UserModell user = task.getResult().toObject(UserModell.class);
                        userModel.setValue(user);
                    } else {
                        Log.i(TAG, "onComplete: ", task.getException());
                    }
                });
    }
}
