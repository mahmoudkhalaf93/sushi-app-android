package com.example.zalpia.ui.itemprofilefav;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zalpia.R;
import com.example.zalpia.databinding.FragmentItemProfileFavBinding;
import com.example.zalpia.room.ItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ItemProfileFavFragment extends Fragment {
    private static final String TAG = "ItemProfileFragment1";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
// ItemProfileFavModelView viewModel;
FragmentItemProfileFavBinding binding;
boolean isInFavorites = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  viewModel =  new ViewModelProvider(this).get(ItemProfileFavModelView.class);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_item_profile_fav, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {

            ItemProfileFavFragmentArgs fragmentArgs = ItemProfileFavFragmentArgs.fromBundle(getArguments());
            ItemModel model = fragmentArgs.getItemModel();


            if(Locale.getDefault().getDisplayLanguage().equals("العربية"))
            {
                binding.itemProfileNamefav.setText(model.getNameAr());
                binding.itemProfileDescripfav.setText(model.getDescriptionAr());
            }
            else {
                binding.itemProfileNamefav.setText(model.getName());
                binding.itemProfileDescripfav.setText(model.getDescription());
            }

            binding.itemProfilePricefav.setText(model.getPrice() + getString(R.string.EGP));
            binding.itemProfileImagefavprofile.setImageURI(Uri.parse(model.getImage()));

            binding.removeFromFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isInFavorites){
                        isInFavorites= false;
                        binding.itemProfileHeartImagefav.setImageResource(R.drawable.ic_baseline_favorite_border_24_orang);
                        firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                .collection("favorites").document(model.getFirebaseId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                } else {
                                    Log.i(TAG, "onComplete: ", task.getException());
                                }
                            }
                        });
                    }
                    else {
                        isInFavorites= true;
                        binding.itemProfileHeartImagefav.setImageResource(R.drawable.ic_baseline_favorite_24);
                        firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                .collection("favorites").document(model.getFirebaseId()).set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(view,  R.string.added_to_Fav, Snackbar.LENGTH_SHORT)
                                            .setAction("done", null).show();
                                } else
                                    Log.i(TAG, "onComplete: ", task.getException());
                            }
                        });
                    }

                }
            });




        binding.addmoreFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: remove null values");
                model.setQuantity(1);

                firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .collection("mycart").document(model.getFirebaseId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getData() != null) {
                            Snackbar.make(view, R.string.already, Snackbar.LENGTH_SHORT)
                                    .setAction("done", null).show();
                        } else {
                            firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                                    .collection("mycart").document(model.getFirebaseId()).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
//                            NavController navController = Navigation.findNavController(view);
//                            navController.navigate(R.id.action_itemProfileFragment_to_nav_mycart);
                                    Snackbar.make(view, R.string.added, Snackbar.LENGTH_SHORT)
                                            .setAction("done", null).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "onFailure: ", e);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i(TAG, "onFailure: ", e);
                    }
                });


            }
        });
        }



    }
}