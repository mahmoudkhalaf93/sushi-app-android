package com.example.zalpia.ui.phoneverify;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.zalpia.R;
import com.example.zalpia.databinding.PhoneVerifiyFragmentBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PhoneVerifiyFragment extends Fragment {

    private static final String TAG = "PhoneVerifiyFragment1";
    PhoneVerifiyFragmentBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
            BultinVerification(credential);
            codeFromCredential=credential.getSmsCode();
        }
        @Override
        public void onVerificationFailed(@NotNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            try {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigateUp();
            }catch (Exception ex){
                Log.i(TAG, "onVerificationFailed: ",ex);
            }

//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                // Invalid request
//
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                // The SMS quota for the project has been exceeded
//            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

           PhoneVerifiyFragment.this.verificationId=verificationId;

        }
    };
    String phone = "",verificationId="", codeFromCredential="",codeFromTextView="";
    //private PhoneVerifiyViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.phone_verifiy_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  mViewModel = new ViewModelProvider(this).get(PhoneVerifiyViewModel.class);
        if (getArguments() != null) {
            PhoneVerifiyFragmentArgs args = PhoneVerifiyFragmentArgs.fromBundle(getArguments());
            phone = args.getPhone();
            getOTPCode(phone);
        } else
            return;


        binding.phonenumber.setText(phone);

        binding.codeVerifiy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Toast.makeText(requireActivity(), "before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().equals(codeFromCredential)) {
                  Verification(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //  Toast.makeText(requireActivity(), "after", Toast.LENGTH_SHORT).show();

            }
        });

        binding.codeVerifiy.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        Verification( binding.codeVerifiy.getText().toString().trim());
                        return true;
                    }
                    return false;
                });

        binding.sendCode.setOnClickListener(view1 -> Verification( binding.codeVerifiy.getText().toString().trim()));


    }

    private void BultinVerification(PhoneAuthCredential credential) {
        Objects.requireNonNull(auth.getCurrentUser()).updatePhoneNumber(credential)
                .addOnSuccessListener(unused -> UpdatePhoneToFirbase(codeFromCredential)).addOnFailureListener(e -> {
                    Log.i(TAG, "onFailure: ", e);
                    Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                   try {
                       NavController navController = Navigation.findNavController(requireView());
                       navController.navigateUp();
                   }catch (Exception ex){
                       Log.i(TAG, "BultinVerification: ",ex);
                   }

                });
    }

    private void Verification(String userCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userCode);
        Objects.requireNonNull(auth.getCurrentUser()).updatePhoneNumber(credential)
                .addOnSuccessListener(unused -> {UpdatePhoneToFirbase(userCode);

                }).addOnFailureListener(e -> {
                    Log.i(TAG, "onFailure: ", e);
                    Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                    try {
//                        NavController navController = Navigation.findNavController(requireView());
//                        navController.navigateUp();
//                    }catch (Exception ex){
//                        Log.i(TAG, "CodeSendVerification: ",ex);
//                    }

                });
    }


    public void getOTPCode(String phoneNumber) {
        // phoneNumber = editText_otp_phone.getText().toString().trim();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void UpdatePhoneToFirbase(String finalcode) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).update(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.codeVerifiy.setText("Su-"+finalcode);
                        binding.codeVerifiy.setEnabled(false);
                        binding.sendCode.setEnabled(false);
                        Completable
                                .timer(3, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                    }
                                    @Override
                                    public void onComplete() {
                                        try {
                                            NavController navController = Navigation.findNavController(requireView());
                                            navController.navigateUp();
                                        }catch (Exception e){
                                            Log.i(TAG, "onComplete: ",e);
                                        }

                                        Toast.makeText(requireActivity(),  R.string.phone_verified, Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    }
                                });
                    } else {
                        Log.i(TAG, "onComplete: ", task.getException());
                        Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        try {
                            NavController navController = Navigation.findNavController(requireView());
                            navController.navigateUp();
                        }catch (Exception e){
                            Log.i(TAG, "UpdatePhoneToFirbase: ",e);
                        }


                    }
                });
    }


}