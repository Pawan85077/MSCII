package com.codinghelper.mscii;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    Context context;

    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }

    public String getEmail() {
        email=sharedPreferences.getString("userdata","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("userdata",email).commit();
    }

    private  String email;
    SharedPreferences sharedPreferences;
    public User(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
    }
}
