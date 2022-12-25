package com.example.zalpia.ui.myaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.zalpia.R;
import com.example.zalpia.UserModell;
import com.example.zalpia.databinding.FragmentMyAccountBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


public class MyAccountFragment extends Fragment {

    private static final String TAG = "MyAccountFragment1";
    FragmentMyAccountBinding binding;
    myAccountVIewModel viewModel;
    Uri imageUri = null;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    String phone;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(myAccountVIewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getUserData().observe(getViewLifecycleOwner(), userModell -> {
            binding.profileName.setText(userModell.getName());
            binding.profileEmail.setText(userModell.getEmail());
            binding.profilePhone.setText(userModell.getPhone());
            if (userModell.getImage() != null)
                binding.profilePic.setImageURI(Uri.parse(userModell.getImage()));
try {
    if (auth.getCurrentUser().getPhoneNumber().isEmpty()) {
        binding.phoneVerifiedImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24_gray);
    } else {
        binding.phoneVerifiedImage.setImageResource(R.drawable.ic_baseline_check_circle_24_grean);
    }
}
catch (Exception e){
    binding.phoneVerifiedImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24_gray);
}

            SetNavHeaderData(getActivity());
        });
        viewModel.setUserData();
        binding.editImageFloatingButton.setOnClickListener(view1 -> openGallery());

        binding.phoneOnclick.setOnClickListener(view12 -> VerifiyPhoneDIalog());
        binding.nameOnclick.setOnClickListener(view15 -> openNameDialog());
        binding.passwordOnclick.setOnClickListener(view13 -> openPasswordDialog());
        binding.emailOnclick.setOnClickListener(view14 -> openEmailDialog());

        if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified())
            binding.emailVerifiedImage.setImageResource(R.drawable.ic_baseline_check_circle_24_grean);
        else
            binding.emailVerifiedImage.setImageResource(R.drawable.ic_baseline_check_circle_outline_24_gray);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        doSomeOperations(Objects.requireNonNull(data));
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        binding.profileLanguage.setText(Locale.getDefault().getDisplayLanguage());

    }

    private void openNameDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.updata_phone_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        TextView txt = view.findViewById(R.id.phone_dialog);
        txt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        txt.setHint(R.string.name_myacc);
        MaterialButton update = view.findViewById(R.id.update_dialog);
        update.setOnClickListener(v -> {
            String name = txt.getText().toString().trim();
            if (!name.isEmpty())
                UpdateName(name);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void UpdateName(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        binding.profileName.setText(name);
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

                Objects.requireNonNull(auth.getCurrentUser()).updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(getActivity(),  R.string.done, Toast.LENGTH_SHORT).show();
                              //  viewModel.setUserData();
                            }
                        });

            } else {
                Log.i(TAG, "onComplete: ", task.getException());
            }
        });

    }

    private void openEmailDialog() {

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.update_or_verifiy_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        MaterialButton update = view.findViewById(R.id.update_dialog_new);
        update.setOnClickListener(v -> {
            UpdateEmailDIalog();
            dialog.dismiss();
        });
        MaterialButton verifiy = view.findViewById(R.id.verifiy_dialog);
        verifiy.setOnClickListener(v -> {
            if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                Toast.makeText(requireActivity(),  R.string.already_ver, Toast.LENGTH_SHORT).show();
            } else {
                auth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(),  R.string.email_has_ben_sent, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            } else {
                                Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            dialog.dismiss();
        });
        dialog.show();

    }

    private void UpdateEmailDIalog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.updata_phone_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        TextView txt = view.findViewById(R.id.phone_dialog);
        txt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        txt.setHint(R.string.email_acc);
        MaterialButton update = view.findViewById(R.id.update_dialog);
        update.setOnClickListener(v -> {
            String password = txt.getText().toString().trim();
            if (!password.isEmpty())
                UpdateEmail(password);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void UpdateEmail(String email) {
        binding.profileEmail.setText(email);
        Objects.requireNonNull(auth.getCurrentUser()).updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("email", email);
                        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).update(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                       // viewModel.setUserData();
                                        Toast.makeText(getActivity(), R.string.done, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.i(TAG, "onComplete: ", task1.getException());
                                        Toast.makeText(requireActivity(), Objects.requireNonNull(task1.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }



    private void openPasswordDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.updata_phone_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        TextView txt = view.findViewById(R.id.phone_dialog);
        txt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        txt.setHint(R.string.new_password);
        MaterialButton update = view.findViewById(R.id.update_dialog);
        update.setOnClickListener(v -> {
            String password = txt.getText().toString().trim();
            if (!password.isEmpty())
                UpdatePassword(password);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void UpdatePassword(String password) {

        Objects.requireNonNull(auth.getCurrentUser()).updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), R.string.done, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(requireActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @SuppressLint("SetTextI18n")
    private void VerifiyPhoneDIalog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.updata_phone_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(view);
        TextView txt = view.findViewById(R.id.phone_dialog);
        txt.setInputType(InputType.TYPE_CLASS_PHONE);
        txt.setHint(R.string.phone_number);
        CountryCodePicker countryCodePicker = view.findViewById(R.id.ccp);

        countryCodePicker.setVisibility(View.VISIBLE);
        phone = binding.profilePhone.getText().toString().trim();
        if (!phone.isEmpty()) {
            if (phone.charAt(0) == '0' && phone.length() == 11) {
                {
                    phone = phone.substring(1);
                    phone = countryCodePicker.getTextView_selectedCountry().getText().toString() + phone;
                    txt.setText(phone);
                }
            } else if (phone.charAt(0) == '1' && phone.length() == 10)
                phone = countryCodePicker.getTextView_selectedCountry().getText().toString() + phone;
            txt.setText(phone);
        }

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                phone = txt.getText().toString().trim();
                if (phone.charAt(0) == '1' && phone.length() == 10){
                    phone = countryCodePicker.getTextView_selectedCountry().getText().toString() + phone;
                    txt.setText(phone);
                }
                else if(phone.length()>10){
                    phone = phone.substring(phone.length()-10);
                    if (phone.charAt(0) == '1' && phone.length() == 10){
                        phone = countryCodePicker.getTextView_selectedCountry().getText().toString() + phone;
                        txt.setText(phone);
                    }
                }
            }
        });
        MaterialButton update = view.findViewById(R.id.update_dialog);
        update.setText(R.string.set_phone);
        update.setOnClickListener(v -> {
            phone = txt.getText().toString().trim();
            if (!phone.isEmpty()) {
                if (phone.charAt(0) == '0' && phone.length() == 11) {
                    phone = phone.substring(1);
                    phone = countryCodePicker.getTextView_selectedCountry().getText().toString() + phone;
                } else if (phone.charAt(0) == '1' && phone.length() == 10)
                    phone = countryCodePicker.getTextView_selectedCountry().getText().toString() + phone;

                if (phone.equals(Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber()))
                    Toast.makeText(requireActivity(), R.string.already_ver, Toast.LENGTH_SHORT).show();
                else
                {
                    MyAccountFragmentDirections.ActionNavMyaccountToPhoneVerifiyFragment action =
                            MyAccountFragmentDirections.actionNavMyaccountToPhoneVerifiyFragment(phone);
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(action);
                   // getOTPCode(phone);
                    dialog.dismiss();}
            } else
                Toast.makeText(requireActivity(),  R.string.what_is_your_number, Toast.LENGTH_SHORT).show();

        });
        dialog.show();
        dialog.setOnCancelListener(dialogInterface -> {
            // countryCodePicker.setVisibility(View.GONE);
        });
    }


    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        //startActivityForResult(intent, 1);
        someActivityResultLauncher.launch(intent);

    }

    private void doSomeOperations(Intent data) {
        imageUri = data.getData();
        UploadImageStorage(imageUri);
        binding.profilePic.setImageURI(data.getData());

    }


    private void UploadImageStorage(Uri imageUri) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyDialogThemeDark);
        progressDialog.setTitle(getString(R.string.Uploading));
        progressDialog.show();
        storageReference.child("profile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).putFile(imageUri).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.child("profile").child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            Log.i(TAG, "onComplete: " + imageUrl);
                            UploadImageLinkFirestorm(imageUrl);
                            progressDialog.dismiss();
                        }).addOnFailureListener(e -> {
                            Log.i(TAG, "onFailure: ", e);
                            progressDialog.dismiss();
                        });

                    } else {
                        Log.i(TAG, "onComplete: ", task.getException());
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                    .getTotalByteCount());
            progressDialog.setMessage(getString(R.string.Uploaded) + (int) progress + "%");
        });


    }

    private void UploadImageLinkFirestorm(String imageUrl) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("image", imageUrl);
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(imageUrl))
                        .build();

                Objects.requireNonNull(auth.getCurrentUser()).updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                               // viewModel.setUserData();
                                Toast.makeText(getActivity(), R.string.done, Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                Log.i(TAG, "onComplete: ", task.getException());
            }
        });
    }

    public void SetNavHeaderData(Context context) {
        NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView userImage = headerView.findViewById(R.id.my_image_view);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModell user = task.getResult().toObject(UserModell.class);

                userName.setText(Objects.requireNonNull(user).getName());
                userEmail.setText(user.getEmail());
                if (user.getImage() != null)
                    userImage.setImageURI(Uri.parse(user.getImage()));

            } else {
                Toast.makeText(context, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onComplete: ", task.getException());
            }
        });
    }
}