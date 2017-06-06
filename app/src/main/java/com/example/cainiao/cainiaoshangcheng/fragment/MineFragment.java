package com.example.cainiao.cainiaoshangcheng.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cainiao.cainiaoshangcheng.MainActivity;
import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.activity.AddressListActivity;
import com.example.cainiao.cainiaoshangcheng.activity.LoginActivity;
import com.example.cainiao.cainiaoshangcheng.activity.MyOrderActivity;
import com.example.cainiao.cainiaoshangcheng.bean.User;
import com.example.cainiao.cainiaoshangcheng.constans.Constans;
import com.example.cainiao.cainiaoshangcheng.myApplication.MyApplication;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Ivan on 15/9/22.
 * 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.img_head)
    CircleImageView mImageHead;
    @BindView(R.id.txt_username)
    TextView mTxtUsername;
    @BindView(R.id.txt_my_orders)
    TextView mTxtMyOrders;
    @BindView(R.id.btn_logout)
    Button mbtnLogout;
    Unbinder unbinder;

    private CnToolbar mToolbar;
    private User mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        mUser = MyApplication.getInstance().getUser();
        showUser(mUser);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {

            MainActivity activity = (MainActivity) context;

            mToolbar = (CnToolbar) activity.findViewById(R.id.toolbar);

            mToolbar.hideSearchView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_head, R.id.txt_username, R.id.btn_logout,R.id.txt_my_orders, R.id.txt_my_favorite, R.id.txt_my_address})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_head:
                if (mUser == null) {
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, Constans.REQUEST_CODE);
                }
                break;
            case R.id.txt_username:
                if (mUser == null) {
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, Constans.REQUEST_CODE);
                }
                break;
            case R.id.btn_logout:
                MyApplication.getInstance().clearUser();
                showUser(null);
                break;
            case R.id.txt_my_orders:
                startActivity(new Intent(getActivity(), MyOrderActivity.class),true);

                break;
            case R.id.txt_my_favorite:
                //startActivity(new Intent(getActivity(), MyFavoriteActivity.class),true);

                break;
            case R.id.txt_my_address:
                startActivity(new Intent(getActivity(), AddressListActivity.class),true);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user = MyApplication.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user) {
        mUser = user;
        if (user != null) {
            if (!TextUtils.isEmpty(user.getLogo_url()))
                showHeadImage(user.getLogo_url());

            mTxtUsername.setText(user.getUsername());

            mbtnLogout.setVisibility(View.VISIBLE);
        } else {

            mTxtUsername.setText(R.string.to_login);
            mbtnLogout.setVisibility(View.GONE);
        }
    }

    private void showHeadImage(String url) {
        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

}
