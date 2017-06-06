package com.example.cainiao.cainiaoshangcheng.http;

import android.content.Context;
import android.content.Intent;

import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.activity.LoginActivity;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;
import com.example.cainiao.cainiaoshangcheng.utils.ToastUtils;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public  class SimpleCallback<T> extends BaseCallBack<T> {

    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onRequestBefore(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onSuccess(Response response, T t) {

    }

    @Override
    public void onError(Response response, int code, Exception e) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyApplication.getInstance().clearUser();
    }
}
