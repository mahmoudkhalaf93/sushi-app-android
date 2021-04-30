package com.example.zalpia.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserData.class} ,version = 1 , exportSchema = false)
public abstract class DaoDatabases  extends RoomDatabase {

    private static DaoDatabases instance ;
    public static DaoDatabases getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,DaoDatabases.class, "daoDatabase")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract DaoDML daoDML();
}
