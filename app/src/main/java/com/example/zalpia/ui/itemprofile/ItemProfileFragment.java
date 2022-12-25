package com.example.zalpia.ui.itemprofile;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentItemProfileBinding;
import com.example.zalpia.room.ItemModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;


public class ItemProfileFragment extends Fragment {
    private static final String TAG = "ItemProfileFragment1";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    //ItemProfileViewModel viewModel;
    FragmentItemProfileBinding binding;
    Boolean isItemInFavority = false;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_profile, container, false);
       // viewModel = new ViewModelProvider(this).get(ItemProfileViewModel.class);


        if (getArguments() != null) {
            ItemProfileFragmentArgs fragmentArgs = ItemProfileFragmentArgs.fromBundle(getArguments());
            //the received item in model variable
            ItemModel model = fragmentArgs.getItemModel();

            if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
            {
                binding.itemProfileName.setText(model.getNameAr());
                binding.itemProfileDescrip.setText(model.getDescriptionAr());
            }
            else {
                binding.itemProfileName.setText(model.getName());
                binding.itemProfileDescrip.setText(model.getDescription());
            }

            binding.itemProfilePrice.setText(model.getPrice() + getString(R.string.EGP));
            binding.itemProfileImageProf.setImageURI(Uri.parse(model.getImage()));

            //check if the item in favorites or not in firebase
            firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .collection("favorites").document(model.getFirebaseId()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.getData() == null) {
                            isItemInFavority = false;
                            binding.itemProfileHeartImage.setImageResource(R.drawable.ic_baseline_favorite_border_24_white);
                            Log.i(TAG, "onComplete: NooooT found it");
                        } else {
                            isItemInFavority = true;
                            binding.itemProfileHeartImage.setImageResource(R.drawable.ic_round_favorite_24);
                            Log.i(TAG, "onComplete: found it");
                        }
                    })
                    .addOnFailureListener(e -> Log.i(TAG, "onComplete: ", e));

            //**addFavoriteButton when click on image if the image in favorites remove it and if image
            // not in favorites then add it to favorites
            binding.addFavoriteButton.setOnClickListener(view -> {

                if (isItemInFavority) {
                    isItemInFavority = false;
                    binding.itemProfileHeartImage.setImageResource(R.drawable.ic_baseline_favorite_border_24_white);
                    firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                            .collection("favorites").document(model.getFirebaseId()).delete().addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Log.i(TAG, "onComplete: ", task.getException());
                                }
                            });
                } else {
                    isItemInFavority = true;
                    binding.itemProfileHeartImage.setImageResource(R.drawable.ic_round_favorite_24);
                    firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                            .collection("favorites").document(model.getFirebaseId()).set(model).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Snackbar.make(view,  R.string.addToFav, Snackbar.LENGTH_SHORT)
                                            .setAction("done", null).show();
                                } else
                                    Log.i(TAG, "onComplete: ", task.getException());
                            });
                }
            });
            //**addFavoriteButton


            binding.addmore.setOnClickListener(view -> {
                Log.i(TAG, "onClick: remove null values");
                model.setQuantity(1);

                firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .collection("mycart").document(model.getFirebaseId()).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.getData() != null) {
                                Snackbar.make(view,  R.string.already, Snackbar.LENGTH_SHORT)
                                        .setAction("done", null).show();
                            } else {
                                firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                        .collection("mycart").document(model.getFirebaseId()).set(model).addOnSuccessListener(unused -> {
    //                            NavController navController = Navigation.findNavController(view);
    //                            navController.navigate(R.id.action_itemProfileFragment_to_nav_mycart);
                                            Snackbar.make(view,  R.string.added, Snackbar.LENGTH_SHORT)
                                                    .setAction("done", null).show();
                                        }).addOnFailureListener(e -> {
                                    Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "onFailure: ", e);
                                });
                            }
                        }).addOnFailureListener(e -> Log.i(TAG, "onFailure: ", e));


            });


        }


        return binding.getRoot();
    }

//    private Map<String, Object> removeNullValues(ItemModel userObject) {
//        Gson gson = new GsonBuilder().create();
//
//        Map<String, Object> map = new Gson().fromJson(
//                gson.toJson(userObject), new TypeToken<HashMap<String, Object>>() {
//                }.getType()
//        );
//
//        return map;
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}