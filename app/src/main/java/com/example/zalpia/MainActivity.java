package com.example.zalpia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        Completable
                .timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

//                        if (firebaseAuth.getCurrentUser()!=null) {
//                            //get the UserModell data from the api
//                        }
                    }

                    @Override
                    public void onComplete() {
                        if (firebaseAuth.getCurrentUser()==null) {
                            //if no token go to login_register screen
                            setContentView(R.layout.activity_log_reg);
//                            startActivity(new Intent(MainActivity.this,LogRegActivity.class));
//                            overridePendingTransition(0, 0);
                        } else {
                            //if i have toke go to home screen
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            overridePendingTransition(R.anim.splash_anim_fromsmall_to_big, R.anim.splash_anim_frombig_small);
                            finish();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });


    }

    public void SignIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.splash_anim_fromsmall_to_big, R.anim.splash_anim_frombig_small);
        finish();
    }

    public void Register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.splash_anim_fromsmall_to_big, R.anim.splash_anim_frombig_small);
        finish();
    }
}