package com.example.zalpia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        // token= DaoDatabases.getInstance(this).daoDML().getUserData().get(0).getToken();
        if (!token.isEmpty()) {
            //get the user data from the api
        }
        String finalToken = token;

        Completable
                .timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (token.isEmpty()) {
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


//        Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .takeWhile(new Predicate<Long>() {
//                    @Override
//                    public boolean test(Long aLong) throws Throwable {
//                        return aLong < 3;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Long aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


    }

    public void SignIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void Register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}