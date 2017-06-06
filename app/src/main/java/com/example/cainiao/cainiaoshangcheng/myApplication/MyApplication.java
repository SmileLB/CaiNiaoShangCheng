package com.example.cainiao.cainiaoshangcheng.myApplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.cainiao.cainiaoshangcheng.bean.User;
import com.example.cainiao.cainiaoshangcheng.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class MyApplication extends Application {

    private User user;

    private static  MyApplication mInstance;

    public static  MyApplication getInstance(){

        return  mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();
        Fresco.initialize(this);
        SMSSDK.initSDK(this, "1e2193bc06926", "ec8a27d8a4b29cff2619a06bb7dd1460");
    }


    private void initUser(){
        this.user = UserLocalData.getUser(this);
    }

    public User getUser(){
        return user;
    }

    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken(){
        return  UserLocalData.getToken(this);
    }

    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }
}
