package com.example.cainiao.cainiaoshangcheng.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cainiao.cainiaoshangcheng.R;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class MyToolbar extends Toolbar {

    private LayoutInflater mInflater;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightImageButton;
    private View mView;

    public MyToolbar(Context context) {
        super(context);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10,10);

        if(attrs !=null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView,false);

            if(isShowSearchView){

                showSearchView();
                hideTitleView();

            }
            a.recycle();
        }
    }

    private void initView() {

        if(mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightImageButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }
    }

    public Button getRightButton(){
        return this.mRightImageButton;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void  setRightButtonIcon(Drawable icon){

        if(mRightImageButton !=null){

            mRightImageButton.setBackground(icon);
            mRightImageButton.setVisibility(VISIBLE);
        }

    }


    public  void setRightButtonOnClickListener(OnClickListener li){

        mRightImageButton.setOnClickListener(li);
    }

    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if(mTextTitle !=null) {
            mTextTitle.setText(title);
            showTitleView();
        }





    }

    public  void showSearchView(){

        if(mSearchView !=null)
            mSearchView.setVisibility(VISIBLE);

    }

    public void hideSearchView(){
        if(mSearchView !=null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }


}
