package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.brandpulltorefresh;

import android.content.Context;
import android.hxuehh.com.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullToRefreshBase_1;

//下拉刷新上下
public class PullLoadingLayout_brand extends FrameLayout {

    private static final int DEFAULT_ROTATION_ANIMATION_DURATION = 400;

    private ImageView headerImage;
    private ImageView sloganImage;
    private ImageView refreshSession;
    private ProgressBar headerProgress;
    private TextView headerText;
    private TextView refreshingText;

    private Animation rotateAnimation, resetRotateAnimation;

    private String pullLabel;
    private String refreshingLabel;
    private String releaseLabel;
    private boolean isShowHeader;

    public PullLoadingLayout_brand(Context context, final int mode, String releaseLabel, String pullLabel, String refreshingLabel, boolean isShowHeader) {
        super(context);
        ViewGroup header;
        if (!isShowHeader) {
            header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refreshs_header, this);
        } else {
            header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, this);

            headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
            headerImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
            refreshSession = (ImageView) header.findViewById(R.id.pull_refresh_session_image);
            sloganImage = (ImageView) header.findViewById(R.id.img_slogan);
            headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);

            refreshingText = (TextView) header.findViewById(R.id.tv_refresh_text);

            final Interpolator interpolator = new LinearInterpolator();
            rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(interpolator);
            rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
            rotateAnimation.setFillAfter(true);

            resetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            resetRotateAnimation.setInterpolator(interpolator);
            resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
            resetRotateAnimation.setFillAfter(true);

            this.releaseLabel = releaseLabel;
            this.pullLabel = pullLabel;
            this.refreshingLabel = refreshingLabel;
            this.headerImage.setImageResource(R.mipmap.list_action_down);
            this.refreshSession.setImageResource(R.mipmap.app_refresh_success);
            this.isShowHeader = isShowHeader;


            switch (mode) {
                case PullToRefreshBase_1.MODE_PULL_UP_TO_REFRESH:
                    headerImage.setImageResource(R.mipmap.list_action_up);
                    break;

                default:
                case PullToRefreshBase_1.MODE_PULL_DOWN_TO_REFRESH:
                    headerImage.setImageResource(R.mipmap.list_action_down);
                    break;
            }
        }
    }

    private void goneView() {
        headerText.setVisibility(View.GONE);
        headerImage.setVisibility(View.GONE);
        refreshSession.setVisibility(View.GONE);
        sloganImage.setVisibility(View.GONE);
        headerProgress.setVisibility(View.GONE);
    }

    public void reset() {
        if (!isShowHeader) {
            return;
        }
        headerText.setText(pullLabel);
        headerText.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.VISIBLE);
        sloganImage.setVisibility(View.VISIBLE);
        headerProgress.setVisibility(View.GONE);
        refreshSession.setVisibility(View.GONE);
        refreshingText.setVisibility(View.GONE);
    }

    public void releaseToRefresh() {
        if (!isShowHeader) {
            return;
        }
        headerText.setText(releaseLabel);
        headerText.setVisibility(View.VISIBLE);
        headerImage.clearAnimation();
        headerImage.startAnimation(rotateAnimation);
        sloganImage.setVisibility(View.VISIBLE);
        refreshSession.setVisibility(View.GONE);
    }

    public void refreshing() {
        if (!isShowHeader) {
            return;
        }
        headerText.setVisibility(View.INVISIBLE);
        refreshingText.setVisibility(VISIBLE);
        headerImage.clearAnimation();
        headerImage.setVisibility(View.INVISIBLE);
        refreshSession.setVisibility(View.GONE);
        sloganImage.setVisibility(View.INVISIBLE);
        headerProgress.setVisibility(View.VISIBLE);
    }

    public void pullToRefresh() {
        if (!isShowHeader) {
            return;
        }
        headerText.setText(pullLabel);
        headerText.setVisibility(View.VISIBLE);
        headerImage.clearAnimation();
        refreshSession.setVisibility(View.GONE);
        sloganImage.setVisibility(View.VISIBLE);
        headerImage.startAnimation(resetRotateAnimation);
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