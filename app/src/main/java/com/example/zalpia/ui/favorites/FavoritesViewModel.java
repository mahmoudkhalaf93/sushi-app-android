package com.example.zalpia.ui.favorites;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.zalpia.Products;
import com.example.zalpia.room.DaoDatabases;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends ViewModel {

  private   MutableLiveData<List<Favorites>> favoritesList;

    public FavoritesViewModel() {
        favoritesList=new MutableLiveData<>();

    }
   public void setFavoritesValue(Context context){
        favoritesList.setValue(DaoDatabases.getInstance(context).daoDML().getFavorites());
    }

public LiveData<List<Favorites>> getFavoritesList(){

    return favoritesList;
}


}
