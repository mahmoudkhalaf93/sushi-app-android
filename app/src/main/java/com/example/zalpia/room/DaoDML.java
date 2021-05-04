package com.example.zalpia.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zalpia.ui.favorites.Favorites;

import java.util.List;

@Dao
public interface DaoDML {
    @Insert
    void addToFavorites(Favorites userdata);
    @Update
    void UpdateFavorites(Favorites userdata);
    @Delete
    void DeleteFavorites(Favorites userdata);
    @Query("SELECT * FROM Favorites")
    List<Favorites> getFavorites();
}
