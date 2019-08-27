package com.example.windows.gymapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.gson.Gson;

/**
 * Created by windows on 27/08/2019.
 */

public class StorageSP {
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public StorageSP(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences = mContext.getSharedPreferences(Constants.GYMAPP_PREF,Context.MODE_PRIVATE);
    }

    public void saveUser(User user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(Constants.USER_PREF,userJson);
        editor.commit();
    }

    public User getUser(){
        User user;
        Gson gson = new Gson();
        String userJson = sharedPreferences.getString(Constants.USER_PREF, null);
        if(userJson==null){
           user = null;
        }else {
            user = gson.fromJson(userJson, User.class);
        }
        return user;
    }

    public void deleteStorage(){
        sharedPreferences.edit().clear().commit();
    }
}
