package com.example.zalpia.ui.home;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.room.Branches;
import com.example.zalpia.room.RestaurantsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel1";
    private final MutableLiveData<ArrayList<OfferModel>> offerListMutable;
    FirebaseFirestore firestorm = FirebaseFirestore.getInstance();

    public HomeViewModel() {


        offerListMutable = new MutableLiveData<>();

    }


    public LiveData<ArrayList<OfferModel>> getOfferList() {
        return offerListMutable;
    }

    public void setOfferList() {
//        RestaurantsModel modl=new RestaurantsModel();
//        ArrayList<GeoPoint> loctino=new ArrayList<>();
//        loctino.add(new GeoPoint(30.043158042861652,31.200589226642364));
//        loctino.add(new GeoPoint(30.056828126532665,31.21101878037916));
//        loctino.add(new GeoPoint(30.048544447739943, 31.189602855701136));
//        loctino.add(new GeoPoint(30.03264411666857, 31.190504062964823));
//        ArrayList<Branches> branches=new ArrayList<>();
//        branches.add(new Branches(loctino.get(0),"doki","open"));
//        branches.add(new Branches(loctino.get(1),"giza","open"));
//        branches.add(new Branches(loctino.get(2),"cairo","close"));
//        branches.add(new Branches(loctino.get(3),"el mohandsen","open"));
//        modl.setBranches(branches);
//        modl.setName("mody");
//        modl.setPhone("01064587878");
//        firestorm.collection("restaurants").document("sushir12").set(modl).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.i(TAG, "onSuccess: uploaded location XXXXXXXXXXX");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull @NotNull Exception e) {
//                Log.i(TAG, "Faild: not uploaded location JJJJJJJJJJJJJJ");
//            }
//        });


        ArrayList<OfferModel> offerList2 = new ArrayList<>();

        firestorm.collection("offers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {

            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {


                OfferModel offerModel = document.toObject(OfferModel.class);
                offerModel.setFirebaseId(document.getId());
                Log.i(TAG, "setOfferList: tstxX " + offerModel.toString());
                if (offerModel.getNameline2().isEmpty()) {
                    offerModel.setNameline2(offerModel.getNameline1());
                    offerModel.setNameline1("");
                }

                offerList2.add(offerModel);


            }
            offerListMutable.setValue(offerList2);
        }

        }).addOnFailureListener(e -> Log.w(TAG, "Error getting documents.", e));


    }
}