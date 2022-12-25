package com.example.zalpia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.zalpia.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
ActivityForgetPasswordBinding binding;
    //private static final String TAG = "ForgetPasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_forget_password);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forget_password);
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPassword();
            }
        });
    }

    public void SendPassword() {
        EditText emailet=findViewById(R.id.email_forget);
        String email= emailet.getText().toString().trim();
        if(email.isEmpty())return;

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ForgetPasswordActivity.this,  R.string.the_email_has_sent, Toast.LENGTH_LONG).show();
          startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.login_anim_fromhideright_to_show, R.anim.login_anim_fromshowing_to_hidetoleftside);
                finish();
            }else {
                Toast.makeText(ForgetPasswordActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}