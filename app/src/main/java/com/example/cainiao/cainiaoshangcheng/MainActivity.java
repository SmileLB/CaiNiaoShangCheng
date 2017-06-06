package com.example.cainiao.cainiaoshangcheng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.cainiao.cainiaoshangcheng.bean.Tab;
import com.example.cainiao.cainiaoshangcheng.fragment.CartFragment;
import com.example.cainiao.cainiaoshangcheng.fragment.CategoryFragment;
import com.example.cainiao.cainiaoshangcheng.fragment.HomeFragment;
import com.example.cainiao.cainiaoshangcheng.fragment.HotFragment;
import com.example.cainiao.cainiaoshangcheng.fragment.MineFragment;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;
import com.example.cainiao.cainiaoshangcheng.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.realtabcontent)
    FrameLayout mRealtabcontent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabhost;

    private CnToolbar mToolbar;

    private CartFragment cartFragment;


    private List<Tab> mTabs = new ArrayList<>(5);
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);

        //初始化资源（图标、文字）
        initTab();
        //初始化TabHost
        initTabHost();

    }

    /**
     * 初始化资源（图标、文字）
     */
    private void initTab() {
        Tab tab_home = new Tab(HomeFragment.class, R.string.home, R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotFragment.class, R.string.hot, R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class, R.string.catagory, R.drawable.selector_icon_category);
        Tab tab_cart = new Tab(CartFragment.class, R.string.cart, R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class, R.string.mine, R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

    }

    /**
     * 初始化TabHost
     */
    private void initTabHost() {
        mInflater = LayoutInflater.from(this);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabhost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId == getString(R.string.cart)) {
                    refData();
                } else if(tabId==getString(R.string.mine)){
                    mToolbar.hideSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);

                }else if(tabId==getString(R.string.home)){
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }else if(tabId==getString(R.string.hot)){
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }else if(tabId==getString(R.string.catagory)){
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });

        //去除分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //默认选中当前的第一个tab
        mTabhost.setCurrentTab(0);
    }

    private void refData(){

        if(cartFragment ==null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));

            if (fragment != null) {

                cartFragment = (CartFragment) fragment;

                cartFragment.refData();
                cartFragment.changeToolbar();
            }
        }
        else
        {
            cartFragment.refData();
            cartFragment.changeToolbar();
        }
    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }
}
