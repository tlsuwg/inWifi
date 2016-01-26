package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.brandpulltorefresh;

import android.content.Context;
import android.hxuehh.com.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * Created by yubaojian on 14-8-25.
 */
public class PullLoadingLayoutfooter_brand extends FrameLayout {
    private static final int DEFAULT_ROTATION_ANIMATION_DURATION = 400;

    private ImageView headerImage;
    private ImageView sloganImage;
    private ImageView refreshSession;
    private ProgressBar headerProgress;
    private TextView headerText;

    private Animation rotateAnimation, resetRotateAnimation;

    private String pullLabel;
    private String refreshingLabel;
    private String releaseLabel;
    private boolean isShowHeader;
    private boolean footer;
    ViewGroup footers;
    private LinearLayout mLoadingLayer;//正在加载
    private LinearLayout mToButtomNoDataLayer;//到达底部

    public PullLoadingLayoutfooter_brand(Context context, final int mode, String releaseLabel, String pullLabel, String refreshingLabel, boolean isShowHeader) {
        super(context);
        ViewGroup header;

        if (!isShowHeader) {
            footers = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.include_stag_list_footer, this);
        } else {
            footers = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.include_stag_list_footer, this);

        }
        this.footer=footer;

        mLoadingLayer = (LinearLayout) footers.findViewById(R.id.layer_pro_tip);
        mToButtomNoDataLayer = (LinearLayout) footers.findViewById(R.id.layer_no_data);
    }





    private void goneView() {

            footers.setVisibility(View.GONE);
            mLoadingLayer.setVisibility(View.VISIBLE);
            mToButtomNoDataLayer.setVisibility(View.GONE);


    }
    public void gone() {


        footers.setVisibility(View.INVISIBLE);
        mLoadingLayer.setVisibility(View.GONE);
        mToButtomNoDataLayer.setVisibility(View.INVISIBLE);


    }
    public void reset() {
             if (!isShowHeader) {
            return;
        }

            footers.setVisibility(View.VISIBLE);
            mLoadingLayer.setVisibility(View.GONE);
            mToButtomNoDataLayer.setVisibility(View.VISIBLE);


    }

    public void releaseToRefresh() {
        if (!isShowHeader) {
            return;
        }

            footers.setVisibility(View.VISIBLE);
            mLoadingLayer.setVisibility(View.GONE);
            mToButtomNoDataLayer.setVisibility(View.VISIBLE);


    }

    public void refreshing() {
        if (!isShowHeader) {
            return;
        }

            footers.setVisibility(View.VISIBLE);
            mLoadingLayer.setVisibility(View.GONE);
            mToButtomNoDataLayer.setVisibility(View.VISIBLE);




    }

    public void pullToRefresh() {
        if (!isShowHeader) {
            return;
        }

            footers.setVisibility(View.VISIBLE);
            mLoadingLayer.setVisibility(View.GONE);
            mToButtomNoDataLayer.setVisibility(View.VISIBLE);



    }

    public void setRefreshingLabel(String refreshingLabel) {
        this.refreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(String releaseLabel) {
        this.releaseLabel = releaseLabel;
    }

    public void setPullLabel(String pullLabel) {
        this.pullLabel = pullLabel;
    }
}
