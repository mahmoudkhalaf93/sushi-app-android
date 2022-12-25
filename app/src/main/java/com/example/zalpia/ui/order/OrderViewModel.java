package com.example.zalpia.ui.order;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.CategoryModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderViewModel extends ViewModel {
    private static final String TAG = "OrderViewModel1";
    FirebaseFirestore firestreo = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<CategoryModel>> catModelList;

    public OrderViewModel() {


        catModelList = new MutableLiveData<>();
    }


    public LiveData<List<CategoryModel>> getCatModelList() {
        return catModelList;
    }


    public void setCatModelList() {


        ArrayList<CategoryModel> offerList2 = new ArrayList<>();

        firestreo.collection("cat").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CategoryModel model = document.toObject(CategoryModel.class);
                    model.setFirebaseId(document.getId());
                    offerList2.add(model);
                }
                catModelList.setValue(offerList2);

            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });


    }

}