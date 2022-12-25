package com.example.zalpia.ui.items;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.ItemModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class ItemsViewModel extends ViewModel {
    private static final String TAG = "ItemsViewModel1";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    MutableLiveData<ArrayList<ItemModel>> mutabItemModel;

    public ItemsViewModel() {
        mutabItemModel = new MutableLiveData<>();
    }

    public LiveData<ArrayList<ItemModel>> getItemModelList() {

        return mutabItemModel;
    }

    public void setItemModelList(String firbaseId, String offersOrCats) {
        ArrayList<ItemModel> modelArrayList = new ArrayList<>();

        firestore.collection(offersOrCats).document(firbaseId).collection("items").get().
                addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.i(TAG, "onComplete: " + document.toString());
                            ItemModel model = document.toObject(ItemModel.class);
                            model.setFirebaseId(document.getId());
                            modelArrayList.add(model);
                        }
                    mutabItemModel.setValue(modelArrayList);
                }
                }).addOnFailureListener(e -> Log.i(TAG, "onComplete: ",e));




    }



}
