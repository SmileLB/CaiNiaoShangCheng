package com.example.cainiao.cainiaoshangcheng.activity;

import android.os.Bundle;
import android.widget.Button;

import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class PayResultActivity2 extends BaseActivity {

    @BindView(R.id.back)
    Button mBack;
    @BindView(R.id.toolbar)
    CnToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        ButterKnife.bind(this);
    }


    @Override
    public void onBackPressed() {

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
