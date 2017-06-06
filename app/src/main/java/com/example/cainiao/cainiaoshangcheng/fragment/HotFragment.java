package com.example.cainiao.cainiaoshangcheng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.activity.WareDetailActivity;
import com.example.cainiao.cainiaoshangcheng.adapter.BaseAdapter;
import com.example.cainiao.cainiaoshangcheng.adapter.HWAdatper;
import com.example.cainiao.cainiaoshangcheng.bean.Page;
import com.example.cainiao.cainiaoshangcheng.bean.Wares;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.http.OkHttpHelp;
import com.example.cainiao.cainiaoshangcheng.utils.Pager;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Ivan on 15/9/22.
 * 热门
 */
public class HotFragment extends BaseFragment implements Pager.OnPageListener<Wares>{

    private OkHttpHelp httpHelper = OkHttpHelp.getInstance();
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_view)
    MaterialRefreshLayout mRefreshLaout;
    Unbinder unbinder;

    private HWAdatper mAdatper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, view);

        //初始化数据
        intDatas();

        return view;
    }

    public void intDatas() {
        Pager pager = Pager.newBuilder()
                .setUrl(Constans.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLaout)
                .build(getContext(), new TypeToken<Page<Wares>>() {}.getType());
        pager.request();
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mAdatper = new HWAdatper(getContext(),datas);

        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Wares wares = mAdatper.getItem(position);

                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Constans.WARE,wares);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdatper);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdatper.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mAdatper.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
