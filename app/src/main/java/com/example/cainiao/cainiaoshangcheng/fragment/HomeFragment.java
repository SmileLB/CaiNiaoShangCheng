package com.example.cainiao.cainiaoshangcheng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.activity.WareListActivity;
import com.example.cainiao.cainiaoshangcheng.adapter.DividerItemDecortion;
import com.example.cainiao.cainiaoshangcheng.adapter.HomeCatgoryAdapter;
import com.example.cainiao.cainiaoshangcheng.bean.Banner;
import com.example.cainiao.cainiaoshangcheng.bean.Campaign;
import com.example.cainiao.cainiaoshangcheng.bean.HomeCampaign;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.http.BaseCallBack;
import com.example.cainiao.cainiaoshangcheng.http.OkHttpHelp;
import com.example.cainiao.cainiaoshangcheng.http.SpotsCallBack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


/**
 * Created by Ivan on 15/9/25.
 * 主页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.slider)
    SliderLayout mSlider;
    Unbinder unbinder;
    @BindView(R.id.custom_indicator)
    PagerIndicator mCustomIndicator;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private HomeCatgoryAdapter mAdatper;

    private Gson gson=new Gson();
    private List<Banner> banners=new ArrayList<>();

    private OkHttpHelp httpHelper = OkHttpHelp.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        requestSlidImages();

        initRecyclerView(view);

        return view;
    }

    /**
     * 请求滚动条的图片
     */
    public void requestSlidImages() {

        httpHelper.get(Constans.SLIDE_IMAGE_URL, new SpotsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banner) {
                banners = banner;
                //初始化滚动菜单
                initSliderLayout();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    /**
     * 初始化RecyclerView
     *
     * @param view
     */
    private void initRecyclerView(View view) {
        httpHelper.get(Constans.CAMPAIGN_HOME, new BaseCallBack<List<HomeCampaign>>() {

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
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                mAdatper = new HomeCatgoryAdapter(homeCampaigns,getActivity());

                mAdatper.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
                    @Override
                    public void onClick(View view, Campaign campaign) {
                        Intent intent = new Intent(getActivity(), WareListActivity.class);
                        intent.putExtra(Constans.COMPAINGAIN_ID,campaign.getId());

                        startActivity(intent);
                    }
                });

                mRecyclerView.setAdapter(mAdatper);

                mRecyclerView.addItemDecoration(new DividerItemDecortion());

                mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeFragment.this.getActivity()));


            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    /**
     * 初始化滚动菜单
     */
    public void initSliderLayout() {

        if(banners !=null){
            for (Banner banner : banners){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSlider.addSlider(textSliderView);
            }
        }

//        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomIndicator(mCustomIndicator);
//        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSlider.setDuration(3000);

        mSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG, "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d(TAG, "onPageScrollStateChanged");
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
