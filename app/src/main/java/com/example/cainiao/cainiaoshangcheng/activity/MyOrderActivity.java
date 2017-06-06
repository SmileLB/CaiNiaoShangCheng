package com.example.cainiao.cainiaoshangcheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.adapter.BaseAdapter;
import com.example.cainiao.cainiaoshangcheng.adapter.CardViewtemDecortion;
import com.example.cainiao.cainiaoshangcheng.adapter.MyOrderAdapter;
import com.example.cainiao.cainiaoshangcheng.bean.Order;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.http.OkHttpHelp;
import com.example.cainiao.cainiaoshangcheng.http.SpotsCallBack;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;


public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {


    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单

    @BindView(R.id.toolbar)
    CnToolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview;
    private int status = STATUS_ALL;


    private MyOrderAdapter mAdapter;


    private OkHttpHelp okHttpHelper = OkHttpHelp.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);


        initToolBar();
        initTab();


        getOrders();
    }


    private void initToolBar() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTab() {


        TabLayout.Tab tab = mTablayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTablayout.addTab(tab);


        tab = mTablayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTablayout.addTab(tab);

        tab = mTablayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);


    }

    private void getOrders() {


        Long userId = MyApplication.getInstance().getUser().getId();

        Map<String, String> params = new HashMap<>();

        params.put("user_id", userId+"");
        params.put("status", status+"");

        okHttpHelper.get(Constans.ORDER_LIST, params, new SpotsCallBack<List<Order>>(this) {

            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }


    private void showOrders(List<Order> orders) {

        if (mAdapter == null) {
            mAdapter = new MyOrderAdapter(this, orders);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());

            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    toDetailActivity(position);
                }
            });
        } else {
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }


    private void toDetailActivity(int position) {

        Intent intent = new Intent(this, OrderDetailActivity.class);

        Order order = mAdapter.getItem(position);
        intent.putExtra("order", order);
        startActivity(intent, true);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
