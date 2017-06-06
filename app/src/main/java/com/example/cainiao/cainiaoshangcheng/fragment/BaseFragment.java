package com.example.cainiao.cainiaoshangcheng.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.cainiao.cainiaoshangcheng.activity.LoginActivity;
import com.example.cainiao.cainiaoshangcheng.bean.User;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;

public abstract class BaseFragment extends Fragment {

    public void startActivity(Intent intent,boolean isNeedLogin){

        if(isNeedLogin){

            User user = MyApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }

        }
        else{
            super.startActivity(intent);
        }
    }
}
