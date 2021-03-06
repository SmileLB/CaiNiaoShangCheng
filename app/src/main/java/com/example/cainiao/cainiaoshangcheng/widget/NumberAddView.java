package com.example.cainiao.cainiaoshangcheng.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cainiao.cainiaoshangcheng.R;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class NumberAddView extends LinearLayout implements View.OnClickListener{

    private LayoutInflater mInflater;

    private Button mBtnAdd;
    private Button mBtnSub;
    private TextView mTextNum;

    private int value;
    private int minValue;
    private int maxValue=1000;

    private  OnButtonClickListener mOnButtonClickListener;

    public NumberAddView(Context context) {
        this(context,null);
    }

    public NumberAddView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberAddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater=LayoutInflater.from(context);

        initView();

        if(attrs!=null){
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context,attrs,R.styleable.NumberAddSubView,defStyleAttr,0);

            int val =  a.getInt(R.styleable.NumberAddSubView_value,0);
            setValue(val);

            int minVal =  a.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMinValue(minVal);

            int maxVal =  a.getInt(R.styleable.NumberAddSubView_manValue,0);
            setMaxValue(maxVal);

            Drawable drawableBtnAdd =a.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
            Drawable drawableBtnSub =a.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
            Drawable drawableTextView =a.getDrawable(R.styleable.NumberAddSubView_textViewBackground);

            setButtonAddBackgroud(drawableBtnAdd);
            setButtonSubBackgroud(drawableBtnSub);
            setTexViewtBackground(drawableTextView);

            a.recycle();
        }
    }

    public void setTexViewtBackground(Drawable drawable){

        mTextNum.setBackgroundDrawable(drawable);

    }

    public void setTextViewBackground(int drawableId){
        setTexViewtBackground(getResources().getDrawable(drawableId));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud){
        this.mBtnAdd.setBackground(backgroud);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud){
        this.mBtnSub.setBackground(backgroud);
    }

    public void initView(){
        View view=mInflater.inflate(R.layout.wieght_number_add_sub,this,true);

        mBtnAdd = (Button) view.findViewById(R.id.btn_add);
        mBtnSub = (Button) view.findViewById(R.id.btn_sub);
        mTextNum = (TextView) view.findViewById(R.id.text_num);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    public int getValue() {
        String val =  mTextNum.getText().toString();
        if(val !=null && !"".equals(val))
            this.value = Integer.parseInt(val);

        return value;
    }

    public void setValue(int value) {
        mTextNum.setText(value+"");
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {

            numAdd();
            if (mOnButtonClickListener != null) {

                mOnButtonClickListener.onButtonAddClick(v,this.value);
            }

        } else if (v.getId() == R.id.btn_sub) {

            numSub();

            if (mOnButtonClickListener != null) {

                mOnButtonClickListener.onButtonSubClick(v,this.value);
            }
        }
    }

    private void numAdd(){
        getValue();
        if(this.value<maxValue){
            this.value=this.value+1;
        }
        Log.i("+++++++++", "numAdd: "+this.value);
        mTextNum.setText(value+"");
    }


    private void numSub(){
        getValue();

        if(this.value>minValue)
            this.value=this.value-1;

        Log.i("---------", "numSub: "+this.value);

        mTextNum.setText(value+"");
    }


    public interface  OnButtonClickListener{
        void onButtonAddClick(View view,int value);
        void onButtonSubClick(View view,int value);
    }
}
