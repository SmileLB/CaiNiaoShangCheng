package com.example.cainiao.cainiaoshangcheng.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class OkHttpHelp {

    public static final int TOKEN_MISSING=401;// token 丢失
    public static final int TOKEN_ERROR=402; // token 错误
    public static final int TOKEN_EXPIRE=403; // token 过期

    private static OkHttpClient okHttpClient;

    private  static  OkHttpHelp mInstance;

    private Gson gson;

    private Handler mHandler;

    static {
        mInstance = new OkHttpHelp();
    }

    private OkHttpHelp(){
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    };

    public static OkHttpHelp getInstance() {
        return mInstance;
    }

    public void get(String url, BaseCallBack callBack) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        doRequest(request, callBack);
    }

    public void get(String url,Map<String,String> param,BaseCallBack callback){

        Request request = buildGetRequest(url,param);

        doRequest(request,callback);

    }

    private  Request buildGetRequest(String url,Map<String,String> param){

        return  buildRequest(url,param,HttpMethodType.GET);
    }





    public void post(String url, Map<String, String> params, BaseCallBack callBack) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callBack);
    }

    public void doRequest(final Request request, final BaseCallBack callBack) {

        callBack.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                callBack.onResponse(response);

                if (response.isSuccessful()) {

                    String resultStr = response.body().string();

                    if (callBack.mType == String.class) {
                        callbackSuccess(callBack,response,resultStr);
                    }else{

                        try{
                            Object obj=gson.fromJson(resultStr,callBack.mType);
                            callbackSuccess(callBack,response,obj);

                        }catch (JsonParseException e){
                            callBack.onError(response, response.code(), e);
                        }
                    }

                }
                else if(response.code() == TOKEN_ERROR||response.code() == TOKEN_EXPIRE ||response.code() == TOKEN_MISSING ){

                    callbackTokenError(callBack,response);
                }

                else {
                    callbackError(callBack,response,null);
                }
            }
        });
    }

    private void callbackTokenError(final  BaseCallBack callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response,response.code());
            }
        });
    }

    private void callbackSuccess(final  BaseCallBack callback , final Response response, final Object obj ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }


    private void callbackError(final  BaseCallBack callback , final Response response, final Exception e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }

    private Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType) {
        Request.Builder build = new Request.Builder();
        build.url(url);
        if (methodType == HttpMethodType.GET) {
            url = buildUrlParams(url,params);
            build.url(url);
            build.get();

        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildFormData(params);

            build.post(body);
        }
        return build.build();
    }

    public RequestBody buildFormData(Map<String, String> params) {
        FormBody.Builder build = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                build.add(entry.getKey(), entry.getValue());
            }
            String token = MyApplication.getInstance().getToken();
            if(!TextUtils.isEmpty(token))
                build.add("token", token);
        }
        return build.build();
    }

    private   String buildUrlParams(String url ,Map<String,String> params) {

        if(params == null)
            params = new HashMap<>(1);

        String token = MyApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token))
            params.put("token",token);


        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if(url.indexOf("?")>0){
            url = url +"&"+s;
        }else{
            url = url +"?"+s;
        }

        return url;
    }

    enum HttpMethodType {
        GET,
        POST
    }
}
