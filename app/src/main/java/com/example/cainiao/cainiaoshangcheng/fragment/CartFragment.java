package com.example.cainiao.cainiaoshangcheng.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cainiao.cainiaoshangcheng.MainActivity;
import com.example.cainiao.cainiaoshangcheng.R;
import com.example.cainiao.cainiaoshangcheng.activity.NewOrderActivity;
import com.example.cainiao.cainiaoshangcheng.adapter.CartAdapter;
import com.example.cainiao.cainiaoshangcheng.adapter.DividerItemDecoration;
import com.example.cainiao.cainiaoshangcheng.bean.ShoppingCart;
import com.example.cainiao.cainiaoshangcheng.utils.CartProvider;
import com.example.cainiao.cainiaoshangcheng.widget.CnToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Ivan on 15/9/22.
 * 购物车
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkbox_all)
    CheckBox mCheckBox;
    @BindView(R.id.txt_total)
    TextView mTextTotal;
    @BindView(R.id.btn_order)
    Button mBtnOrder;
    @BindView(R.id.btn_del)
    Button mBtnDel;
    Unbinder unbinder;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;

    private CnToolbar mToolbar;

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);

        cartProvider = new CartProvider(getContext());

        showData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {

            MainActivity activity = (MainActivity) context;

            mToolbar = (CnToolbar) activity.findViewById(R.id.toolbar);

            changeToolbar();
        }
    }

    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    public void refData() {
        mAdapter.clear();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    public void changeToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    private void hideDelControl() {
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);

        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    @OnClick({R.id.btn_order, R.id.btn_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order:
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent, true);
                break;
            case R.id.btn_del:
                mAdapter.delCart();
                break;
            default:
                int action = (int) view.getTag();
                if (ACTION_EDIT == action) {
                    showDelControl();
                } else if (ACTION_CAMPLATE == action) {

                    hideDelControl();
                }
        }
    }
}
