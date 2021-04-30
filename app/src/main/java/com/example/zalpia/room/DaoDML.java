package com.example.zalpia.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoDML {
    @Insert
    void addUserDate(UserData userdata);
    @Update
    void UpdateUserDate(UserData userdata);
    @Delete
    void DeleteUserDate(UserData userdata);
    @Query("SELECT * FROM userdata")
    List<UserData> getUserData();
}
