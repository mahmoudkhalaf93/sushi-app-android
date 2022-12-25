package com.example.zalpia.ui.mycart;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.ItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MyCartViewModel extends ViewModel {
    private static final String TAG = "MyCartViewModel1";
    FirebaseFirestore firestore= FirebaseFirestore.getInstance();
    MutableLiveData<ArrayList<ItemModel>> mutableItemModel ;

    public MyCartViewModel() {
        mutableItemModel =new MutableLiveData<>();
    }

    public LiveData<ArrayList<ItemModel>> getMyCartList(){
        return mutableItemModel;
    }

    public void setMyCartList(){
        ArrayList<ItemModel> listModel = new ArrayList<>();
        firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("mycart").get().addOnCompleteListener(task -> {
               if(task.isSuccessful()&&!task.getResult().isEmpty()){
                   for (QueryDocumentSnapshot document :task.getResult()) {
                       ItemModel model =document.toObject(ItemModel.class);
                       model.setFirebaseId(document.getId());
                       listModel.add(model);
                   }
                   mutableItemModel.setValue(listModel);
               }
               else {
                   Log.i(TAG, "onComplete: ", task.getException());
               }
                });

    }


}
