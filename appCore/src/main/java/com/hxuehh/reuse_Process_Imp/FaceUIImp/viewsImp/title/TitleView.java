package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title;

import android.content.Context;
import android.hxuehh.com.R;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;



/**
 * Created by suwg on 2014/6/24.
 */

public class TitleView extends FaceGetMainViewImp {

    private ImageView mBackImg;
    private TextView mTitleView;
    private ImageView img_right;
    //    private TabPageIndicator mMidLayout;
    private LinearLayout mCenterLayout;
    private Context mContext;
    private RelativeLayout mRlyContainer;
    private TextView bottom_line;
   private BackCallBack mBackCallBack;
    private TextView tv_mymessage;
    public TitleView(Context context, BackCallBack backCallBack) {
        super(context);
        mContext = context;
        mBackCallBack=backCallBack;
        initView();
    }
    public TitleView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    @Override
    public void initView() {
        mainView = LayoutInflater.from(getContext()).inflate(R.layout.include_title, null);
        img_right= (ImageView) findViewById(R.id.img_right);
        mBackImg = (ImageView) findViewById(R.id.img_back);
        mTitleView = (TextView) findViewById(R.id.tv_title_name);
        mCenterLayout = (LinearLayout) findViewById(R.id.v_select_bar);
        mRlyContainer = (RelativeLayout) findViewById(R.id.rly_title_view_container);
        bottom_line= (TextView) findViewById(R.id.bottom_line);

        tv_mymessage= (TextView) this.findViewById(R.id.tv_mymessage);

//        mCenterLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        mMidLayout = (TabPageIndicator) findViewById(R.id.indicator);

    }

    public TitleView setTitle(String title) {
        mTitleView.setText(title);
        return this;
    }
    public void setRightTextViewVisible(){
        tv_mymessage.setVisibility(View.VISIBLE);
    }

    public TitleView setTitle(int titleResId) {
        mTitleView.setText(titleResId);
        return this;
    }

    public void setTitle(String title, int imgResId) {
        setTitle(title);
        mTitleView.setCompoundDrawablesWithIntrinsicBounds(imgResId, 0, 0, 0);
        mTitleView.setCompoundDrawablePadding(40);
    }

    public void setTitle(int titleResId, int imgResId) {
        setTitle(titleResId);
        mTitleView.setCompoundDrawablesWithIntrinsicBounds(imgResId, 0, 0, 0);
        mTitleView.setCompoundDrawablePadding(40);
    }

    public void addRightView(View view) {
        mCenterLayout.addView(view);
    }

//    public void setMidLayoutVisible() {
//        mMidLayout.setVisibility(VISIBLE);
//    }
//
//    public void setViewPager(ViewPager mViewPager) {
//        mMidLayout.setViewPager(mViewPager);
//        mMidLayout.setVisibility(VISIBLE);
//    }
//
//    public void setViewPager(ViewPager mViewPager,int currentIndex) {
//        mMidLayout.setViewPager(mViewPager,currentIndex);
//        mMidLayout.setVisibility(VISIBLE);
//    }

    public void setBackBtnVisible() {
        mBackImg.setVisibility(VISIBLE);
    }
    public void setBackResource(int resId) {
        mBackImg.setVisibility(VISIBLE);
        mBackImg.setImageResource(resId);
    }
    public void setBackListener() {
        mBackImg.setVisibility(VISIBLE);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackCallBack.callback();
            }
        });
    }
    public void setImg_rightListener(final RightCallBack rightListener) {
        img_right.setVisibility(VISIBLE);
        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightListener.callback();
            }
        });
    }
    public void setImg_rightResource(int resId) {
        img_right.setVisibility(VISIBLE);
        img_right.setImageResource(resId);
    }
    public void setImg_rightGone() {
        img_right.setVisibility(View.GONE);
    }
    public void setBottom_lineGone() {
        bottom_line.setVisibility(View.GONE);
    }
    public void setBackBtnImg(int resId) {
        mBackImg.setBackgroundResource(resId);
    }

    public void setOnBackListener(View.OnClickListener listener) {
        mBackImg.setOnClickListener(listener);
    }

    public void updateTitleName(String name){
        mTitleView.setText(name);
    }


    public void setBackgroundColor(int color){
        mRlyContainer.setBackgroundColor(color);
    }

    public void setTitleTextColor(int color){
        mTitleView.setTextColor(color);
    }

    public void setTitleSize(int sizeId){
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(sizeId));
    }

    public interface BackCallBack {
        public abstract void callback();
    }
    public interface RightCallBack {
        public abstract void callback();
    }

}

