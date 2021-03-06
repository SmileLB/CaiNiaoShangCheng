package com.example.cainiao.cainiaoshangcheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.cainiao.cainiaoshangcheng.bean.User;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent,boolean isNeedLogin){

        if(isNeedLogin){

            User user = MyApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }
        else{
            super.startActivity(intent);
        }
    }
}
