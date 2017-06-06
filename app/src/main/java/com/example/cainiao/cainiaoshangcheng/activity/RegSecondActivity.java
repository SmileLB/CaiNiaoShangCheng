package com.example.cainiao.cainiaoshangcheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cainiao.cainiaoshangcheng.MainActivity;
import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.bean.LoginRespMsg;
import com.example.cainiao.cainiaoshangcheng.bean.User;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.http.OkHttpHelp;
import com.example.cainiao.cainiaoshangcheng.http.SpotsCallBack;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;
import com.example.cainiao.cainiaoshangcheng.utils.DESUtil;
import com.example.cainiao.cainiaoshangcheng.utils.ToastUtils;
import com.example.cainiao.cainiaoshangcheng.widget.ClearEditText;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;
import com.example.cainiao.cainiaoshangcheng.widget.CountTimerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

public class RegSecondActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    CnToolbar mToolBar;
    @BindView(R.id.txtTip)
    TextView mTxtTip;
    @BindView(R.id.edittxt_code)
    ClearEditText mEtCode;
    @BindView(R.id.btn_reSend)
    Button mBtnResend;

    private String phone;
    private String pwd;
    private String countryCode;

    private CountTimerView countTimerView;

    private SpotsDialog dialog;

    private OkHttpHelp okHttpHelper = OkHttpHelp.getInstance();

    private SMSEvenHanlder evenHanlder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ButterKnife.bind(this);

        initToolBar();

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);

        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this, "正在校验验证码");
    }


    private void initToolBar() {

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegSecondActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view) {
        SMSSDK.getVerificationCode("+" + countryCode, phone);
        countTimerView = new CountTimerView(mBtnResend, R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }

    /**
     * 分割电话号码
     */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }


    private void submitCode() {
        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        dialog.show();
    }


    private void doReg() {

        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Constans.DES_KEY, pwd));

        okHttpHelper.post(Constans.REG, params, new SpotsCallBack<LoginRespMsg<User>>(this) {
            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    ToastUtils.show(RegSecondActivity.this, "注册失败:" + userLoginRespMsg.getMessage());
                    return;
                }
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this, MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    class SMSEvenHanlder extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }


}
