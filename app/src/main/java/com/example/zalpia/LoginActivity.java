package com.example.zalpia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.zalpia.databinding.ActivityLoginBinding;
import com.example.zalpia.room.DaoDatabases;
import com.example.zalpia.ui.favorites.Favorites;

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

        //List<Favorites> userData= DaoDatabases.getInstance(this).daoDML().getFavorites();
        startActivity(new Intent(this,HomeActivity.class));
        overridePendingTransition(R.anim.splash_anim_fromsmall_to_big, R.anim.splash_anim_frombig_small);
//        for(Favorites favorites1F : userData ){

//            if((favorites1F.getDescription().equals(username) || favorites1F.getPhone().equals(username)|| favorites1F.getName().equals(username))
//            && favorites1F.getPassword().equals(password)) {
//                startActivity(new Intent(this,HomeActivity.class));
//                return;
//            }

       // }
       // Toast.makeText(this, "username or password are wrong ", Toast.LENGTH_SHORT).show();

    }


    public void GoToRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.login_anim_fromhideright_to_show, R.anim.login_anim_fromshowing_to_hidetoleftside);
    }
}