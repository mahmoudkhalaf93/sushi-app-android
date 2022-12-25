package com.example.zalpia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.zalpia.databinding.ActivityRegisterBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ActivityRegisterBinding binding;
    private CallbackManager mCallbackManager;
    LoginButton loginButton;
   // GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        FacebookSdk.sdkInitialize(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.button_sign_in);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                GoogleSignInClient mGoogleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);
                mGoogleSignInClient.revokeAccess();
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                GoogleSignInClient mGoogleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);
                mGoogleSignInClient.revokeAccess();
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
            }
        });
        // [END initialize_fblogin]
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
//        { binding.passwordReg.setGravity(Gravity.END);
//            binding.phoneReg.setGravity(Gravity.END);}
//        else
//        {   binding.passwordReg.setGravity(Gravity.START);
//            binding.phoneReg.setGravity(Gravity.START);}
//    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if UserModell is signed in (non-null) and update UI accordingly.
        //  FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth.getCurrentUser() != null)
            updateUI();
    }

    public void RegisterNow(View view) {
        String username = binding.usernameReg.getText().toString().trim();
        String emailReg = binding.emailReg.getText().toString().trim();
        String passwordReg = binding.passwordReg.getText().toString().trim();
        String phoneReg = binding.phoneReg.getText().toString().trim();
        if (username.isEmpty()) return;
        if (emailReg.isEmpty()) return;
        if (passwordReg.isEmpty()) return;
        if (phoneReg.isEmpty()) return;

        firebaseAuth.createUserWithEmailAndPassword(emailReg, passwordReg).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModell userModell = new UserModell(username, emailReg, phoneReg);

                firestore.collection("users").document(Objects.requireNonNull(task.getResult().getUser()).getUid()).set(userModell).
                        addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                updateUI();
                            } else {
                                Log.i(TAG, "onComplete: ", task1.getException());
                            }
                        });

            } else {
                Log.i(TAG, "onComplete: " + Objects.requireNonNull(task.getException()).getLocalizedMessage());
                Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GoToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.reg_anim_fromhideleft_to_show, R.anim.reg_anim_fromshow_to_hidetorightside);
    }

    public void RegisterGoogle(View view) {
        signIn();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in UserModell's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        UserModell userModell = new UserModell(Objects.requireNonNull(user).getDisplayName(), user.getEmail(), user.getPhoneNumber(),
                          Objects.requireNonNull(user.getPhotoUrl()).toString() );
                        firestore.collection("users").document(task.getResult().getUser().getUid()).set(userModell).
                                addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        updateUI();
                                    } else {
                                        Log.i(TAG, "onComplete: ", task1.getException());
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the UserModell.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        // updateUI(null);
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = task.getResult().getUser();
                        UserModell userModell = new UserModell(Objects.requireNonNull(user).getDisplayName(), user.getEmail(), user.getPhoneNumber(),
                                Objects.requireNonNull(user.getPhotoUrl()).toString());
                        firestore.collection("users").document(task.getResult().getUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.getData() != null)
                                updateUI();
                            else {
                                firestore.collection("users").document(task.getResult().getUser().getUid()).set(userModell).
                                        addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                updateUI();
                                            } else {
                                                Log.i(TAG, "onComplete: ", task1.getException());
                                            }
                                        });
                            }
                        }).addOnFailureListener(e -> Log.i(TAG, "onFailure: ", e));

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(RegisterActivity.this,  task.getException().getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();


                        GoogleSignInClient mGoogleSignInClient;
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);
                        mGoogleSignInClient.revokeAccess();
                        LoginManager.getInstance().logOut();



                       // updateUI();
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI() {
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.reg_anim_fromhideleft_to_show, R.anim.reg_anim_fromshow_to_hidetorightside);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.reg_anim_fromhideleft_to_show, R.anim.reg_anim_fromshow_to_hidetorightside);
        finish();
    }

}