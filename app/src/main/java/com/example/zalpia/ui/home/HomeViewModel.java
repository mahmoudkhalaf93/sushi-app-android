package com.example.zalpia.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.R;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<OfferModel>> offerList;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        offerList = new MutableLiveData<>();
        offerList.setValue(setOfferList());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public  LiveData<ArrayList<OfferModel>> getOfferList()
    {
        return offerList;
    }

    private ArrayList<OfferModel> setOfferList() {
        ArrayList<OfferModel> offerList2 = new ArrayList<>();
        OfferModel offerModel0 = new OfferModel("Sunday", "",
                "Free lyrimShrimp", R.drawable.cat1, true);
        offerList2.add(offerModel0);
        OfferModel offerModel1 = new OfferModel("15:00 to 23:30", "50%",
                "discount on prtof", R.drawable.cat2, false);
        offerList2.add(offerModel1);
        OfferModel offerModel2 = new OfferModel("12:00 to 22:00", "75%",
                "discount on prtof", R.drawable.cat3, false);
        offerList2.add(offerModel2);
        OfferModel offerModel3 = new OfferModel("Sunday", "",
                "Free lyrimShrimp", R.drawable.catdef, true);
        offerList2.add(offerModel3);
        OfferModel offerModel4 = new OfferModel("Sunday", "",
                "Free lyrimShrimp", R.drawable.catdef, true);
        offerList2.add(offerModel4);
        OfferModel offerModel5 = new OfferModel("Sunday", "",
                "Free lyrimShrimp", R.drawable.catdef, true);
        offerList2.add(offerModel5);
        OfferModel offerModel6 = new OfferModel("Sunday", "",
                "Free lyrimShrimp", R.drawable.catdef, true);
        offerList2.add(offerModel6);
        OfferModel offerModel7 = new OfferModel("Sunday", "",
                "Free lyrimShrimp", R.drawable.catdef, true);
        offerList2.add(offerModel7);
        return offerList2;
    }
}