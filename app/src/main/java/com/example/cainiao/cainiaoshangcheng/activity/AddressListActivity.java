package com.example.cainiao.cainiaoshangcheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.adapter.AddressAdapter;
import com.example.cainiao.cainiaoshangcheng.adapter.DividerItemDecoration;
import com.example.cainiao.cainiaoshangcheng.bean.Address;
import com.example.cainiao.cainiaoshangcheng.bean.BaseRespMsg;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.http.OkHttpHelp;
import com.example.cainiao.cainiaoshangcheng.http.SpotsCallBack;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;


public class AddressListActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    CnToolbar mToolBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview;

    private AddressAdapter mAdapter;

    private OkHttpHelp mHttpHelper = OkHttpHelp.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);

        initToolbar();

        initAddress();
    }


    private void initToolbar() {

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddActivity();
            }
        });

    }


    private void toAddActivity() {

        Intent intent = new Intent(this, AddressAddActivity.class);
        startActivityForResult(intent, Constans.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();

    }

    private void initAddress() {

        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MyApplication.getInstance().getUser().getId());

        mHttpHelper.get(Constans.ADDRESS_LIST, new SpotsCallBack<List<Address>>(this) {

            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                showAddress(addresses);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }


    private void showAddress(List<Address> addresses) {

        Collections.sort(addresses);
        if (mAdapter == null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressLisneter() {
                @Override
                public void setDefault(Address address) {

                    updateAddress(address);

                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        } else {
            mAdapter.refreshData(addresses);
            mRecyclerview.setAdapter(mAdapter);
        }

    }


    public void updateAddress(Address address) {

        Map<String, String> params = new HashMap<>(1);
        params.put("id", address.getId()+"");
        params.put("consignee", address.getConsignee());
        params.put("phone", address.getPhone());
        params.put("addr", address.getAddr());
        params.put("zip_code", address.getZipCode());
        params.put("is_default", address.getIsDefault()+"");

        mHttpHelper.post(Constans.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }
}
