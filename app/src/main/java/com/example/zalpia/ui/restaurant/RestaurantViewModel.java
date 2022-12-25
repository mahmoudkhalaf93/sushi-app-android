package com.example.zalpia.ui.restaurant;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.RestaurantsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RestaurantViewModel extends ViewModel {
    MutableLiveData<RestaurantsModel> mutableRestaurant;
    private static final String TAG = "RestaurantViewModel1";
    RestaurantsModel model;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    public RestaurantViewModel() {
        mutableRestaurant =new MutableLiveData<>();
    }

    public LiveData<RestaurantsModel> getMutableRestaurant(){
        return mutableRestaurant;
    }
    public void setMutableRestaurant(){
         model = new RestaurantsModel();
        firestore.collection("restaurants").document("sushir12").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.getData().isEmpty()) {
                    model = documentSnapshot.toObject(RestaurantsModel.class);
                    mutableRestaurant.setValue(model);
                    //get list of branch location
//                    firestore.collection("restaurants").document("sushir").collection("branch").get()
//                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    if(!queryDocumentSnapshots.getDocuments().isEmpty()){
//                                        model.setGeoPoints();
//                                    }
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull @NotNull Exception e) {
//                            Log.i(TAG, "onFailure: ",e);
//                        }
//                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i(TAG, "onFailure: ",e);
            }
        });
    }
}
