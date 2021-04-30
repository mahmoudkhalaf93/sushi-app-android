package com.example.zalpia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogRegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

    }

    public void SignIn(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void Register(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}