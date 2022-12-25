package com.example.zalpia.ui.menu;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.CategoryModel;
import com.example.zalpia.room.ItemModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MenuViewModel extends ViewModel {
    private static final String TAG = "MenuViewModel1";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<ArrayList<CategoryModel>> catArray;
    private final MutableLiveData<ArrayList<ItemModel>> itemsArray;

    public MenuViewModel() {
        itemsArray = new MutableLiveData<>();
        catArray = new MutableLiveData<>();

    }

    public LiveData<ArrayList<CategoryModel>> getCatList() {
        return catArray;
    }

    public void setCatArray() {
        ArrayList<CategoryModel> categoryModels = new ArrayList<>();

        firestore.collection("cat").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    CategoryModel model = document.toObject(CategoryModel.class);
                    model.setFirebaseId(document.getId());
                    categoryModels.add(model);
                }
                catArray.setValue(categoryModels);
            }
        }).addOnFailureListener(e -> Log.i(TAG, "onComplete: ", e));

    }

    public LiveData<ArrayList<ItemModel>> getListOfItemsInCatLive() {

        return itemsArray;
    }

    public void setListOfItemsInCat(String catId) {
        ArrayList<ItemModel> itemModels = new ArrayList<>();
        firestore.collection("cat").document(catId).collection("items").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ItemModel model = document.toObject(ItemModel.class);
                            model.setFirebaseId(document.getId());
                            itemModels.add(model);
                        }
                        itemsArray.setValue(itemModels);

                    }
                }).addOnFailureListener(e -> Log.i(TAG, "onComplete: ",e));


    }

}