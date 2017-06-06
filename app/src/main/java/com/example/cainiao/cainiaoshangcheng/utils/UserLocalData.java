package com.example.cainiao.cainiaoshangcheng.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.cainiao.cainiaoshangcheng.bean.User;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;


public class UserLocalData {

    public static void putUser(Context context,User user){
        String user_json =  JSONUtil.toJSON(user);
        PreferencesUtils.putString(context, Constans.USER_JSON,user_json);
    }

    public static void putToken(Context context,String token){

        PreferencesUtils.putString(context, Constans.TOKEN,token);
    }


    public static User getUser(Context context){

        String user_json= PreferencesUtils.getString(context,Constans.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){

            return  JSONUtil.fromJson(user_json,User.class);
        }
        return  null;
    }

    public static  String getToken(Context context){

        return  PreferencesUtils.getString( context,Constans.TOKEN);

    }


    public static void clearUser(Context context){
        PreferencesUtils.putString(context, Constans.USER_JSON,"");

    }

    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Constans.TOKEN,"");
    }



}
