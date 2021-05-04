package com.example.zalpia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zalpia.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);


    }

    public void RegisterNow(View view) {
        String username = binding.usernameReg.getText().toString().trim();
        String emailReg = binding.emailReg.getText().toString().trim();
        String passwordReg = binding.passwordReg.getText().toString().trim();
        String phoneReg = binding.phoneReg.getText().toString().trim();
        if(username.isEmpty()) return;
        if(emailReg.isEmpty()) return;
        if(passwordReg.isEmpty()) return;
        if(phoneReg.isEmpty()) return;
        //Favorites favorites =new Favorites(username,emailReg,passwordReg,phoneReg,"modikadskj456d4as");
      //  DaoDatabases.getInstance(this).daoDML().addToFavorites(favorites);

        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    public void GoToLogin(View view) {
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.reg_anim_fromhideleft_to_show, R.anim.reg_anim_fromshow_to_hidetorightside);
    }
}