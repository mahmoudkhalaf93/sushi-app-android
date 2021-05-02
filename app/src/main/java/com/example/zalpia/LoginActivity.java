package com.example.zalpia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zalpia.databinding.ActivityLoginBinding;
import com.example.zalpia.room.DaoDatabases;
import com.example.zalpia.room.UserData;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity1";
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);



    }

    public void LoginNow(View view) {
       String username=  binding.usernameLogin.getText().toString().trim();
        String password=  binding.passwordLogin.getText().toString().trim();

        if(username.isEmpty() || password.isEmpty())return;

        List<UserData> userData= DaoDatabases.getInstance(this).daoDML().getUserData();

        for(UserData userData1f : userData ){

            if((userData1f.getEmail().equals(username) ||userData1f.getPhone().equals(username)||userData1f.getName().equals(username))
            && userData1f.getPassword().equals(password)) {
                startActivity(new Intent(this,HomeActivity.class));
                return;
            }

        }
        Toast.makeText(this, "username or password are wrong ", Toast.LENGTH_SHORT).show();

    }


    public void GoToRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.login_anim_fromhideright_to_show, R.anim.login_anim_fromshowing_to_hidetoleftside);
    }
}