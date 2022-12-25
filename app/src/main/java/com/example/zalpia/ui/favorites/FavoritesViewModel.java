package com.example.zalpia.ui.favorites;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.ItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesViewModel extends ViewModel {
    private static final String TAG = "FavoritesViewModel1";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<ItemModel>> favoritesList;

    public FavoritesViewModel() {
        favoritesList = new MutableLiveData<>();
    }

    public LiveData<List<ItemModel>> getFavoritesList() {

        return favoritesList;
    }

    public void setFavoritesValue() {
        ArrayList<ItemModel> itemModels = new ArrayList<>();
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("favorites").get().addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ItemModel model = document.toObject(ItemModel.class);
                            model.setFirebaseId(document.getId());
                            itemModels.add(model);
                        }

                        favoritesList.setValue(itemModels);
                    }
                }).addOnFailureListener(e -> Log.i(TAG, "onFailure: ",e));
    }

}
