package com.example.zalpia;

import android.content.Context;
import android.content.SharedPreferences;

public class MyShared {
    private static SharedPreferences instance;
private  String token;
    public SharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        }
        return instance;
    }
    public void setToken(String token1,Context context){
getInstance(context).edit().putString(token,token1).apply();
    }

    public String getToken(Context context){
       return getInstance(context).getString(token,"");
    }
}
