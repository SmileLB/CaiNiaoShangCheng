package com.example.cainiao.cainiaoshangcheng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.adapter.DividerItemDecoration;
import com.example.cainiao.cainiaoshangcheng.adapter.HWAdatper;
import com.example.cainiao.cainiaoshangcheng.bean.Page;
import com.example.cainiao.cainiaoshangcheng.bean.Wares;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.utils.Pager;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class WareListActivity extends BaseActivity implements Pager.OnPageListener<Wares>,TabLayout.OnTabSelectedListener,View.OnClickListener{

    @BindView(R.id.toolbar)
    CnToolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.txt_summary)
    TextView mTxtSummary;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview_wares;
    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout mRefreshLayout;

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    public static final int ACTION_LIST=1;
    public static final int ACTION_GIRD=2;

    private int orderBy = 0;
    private long campaignId = 0;

    private HWAdatper mWaresAdapter;

    private Pager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ButterKnife.bind(this);
        initToolBar();

        campaignId=getIntent().getLongExtra(Constans.COMPAINGAIN_ID,0);

        initTab();

        getData();

    }

    private void getData(){
        pager= Pager.newBuilder().setUrl(Constans.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wares>>(){}.getType());

        pager.request();

    }

    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);

        mToolbar.getRightButton().setVisibility(View.GONE);

        mToolbar.setRightButtonOnClickListener(this);
    }

    private void initTab(){

        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);

        mTablayout.addTab(tab);

        mTablayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy",orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();

        if(ACTION_LIST == action){

            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            mToolbar.getRightButton().setTag(ACTION_GIRD);

            mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this,2));
            mWaresAdapter.resetLayout(R.layout.template_grid_wares);

        } else if(ACTION_GIRD == action){

            mToolbar.setRightButtonIcon(R.mipmap.icon_grid_32);
            mToolbar.getRightButton().setTag(ACTION_LIST);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));

            mWaresAdapter.resetLayout(R.layout.template_hot_wares);

        }
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mTxtSummary.setText("共有"+totalCount+"件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HWAdatper(this, datas);
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }
}
