package com.example.zalpia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.zalpia.room.DaoDatabases;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        token= DaoDatabases.getInstance(this).daoDML().getUserData().get(0).getToken();
        if(!token.isEmpty()){
            //get the user data from the api
        }

        String finalToken = token;
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Throwable {
                        return aLong < 3;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(token.isEmpty()){
                            //if no token go to login register screen
                            startActivity(new Intent(MainActivity.this,LogRegActivity.class));
                        }
                        else {
                            //if i have toke go to home screen
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }

                    }
                });


    }
}