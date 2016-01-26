package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content;

import android.app.Activity;
import android.hxuehh.com.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.LoadForViewManager;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.staggered.PullToRefreshStaggeredGridView_2;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.staggered.StaggeredGridView;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.LoadView;
import com.hxuehh.appCore.faceFramework.faceUI.viewAdapter.FaceListAdapter;
import com.hxuehh.appCore.develop.FaceEventCallBackINforDlp;
import com.hxuehh.appCore.develop.FaceEventInforDlp;


import java.util.List;

/**
 * Created by kaizen on 14-6-30.
 */
public class LoadStaggeredGridView extends LoadView {
    private static final String TAG = LoadStaggeredGridView.class.getSimpleName();

    private Activity mContext;

    private boolean isAddTopBanner;//只是为了回调一次
    private PullToRefreshStaggeredGridView_2 mPullStaggerGridView; // 下拉刷新瀑布流
    private StaggeredGridView mWaterGridView; // 瀑布流

    boolean isaddAll;
    private View mStaggerAllFootView; // 包括下面两个

    private LinearLayout mLoadingLayer;//正在加载
    private LinearLayout mToButtomNoDataLayer;//到达底部

    private LoadForViewManager mLoadForViewManager;
    private FaceListAdapter mDealSwipeListAdapter;

    public LoadStaggeredGridView(Activity context, LoadForViewManager manager, FaceListAdapter adapter) {

        this.mContext = context;
        this.mLoadForViewManager = manager;
        this.mDealSwipeListAdapter = adapter;
        initViews();
        registerListeners();
    }

    private void initViews() {
        mainView=  LayoutInflater.from(mContext).inflate(R.layout.layer_zhideals_e, null);

        mPullStaggerGridView = (PullToRefreshStaggeredGridView_2) mainView.findViewById(R.id.list_stag_e);
        mWaterGridView = mPullStaggerGridView.getRefreshableView();

        mStaggerAllFootView = View.inflate(mContext, R.layout.include_stag_list_footer, null);
        mLoadingLayer = (LinearLayout) mStaggerAllFootView.findViewById(R.id.layer_pro_tip);
        mToButtomNoDataLayer = (LinearLayout) mStaggerAllFootView.findViewById(R.id.layer_no_data);
        if (mStaggerAllFootView != null) {
            mWaterGridView.setFooterView(mStaggerAllFootView);
        }

        mWaterGridView.setAdapter(mDealSwipeListAdapter);
        mHeaderViewContainer = new FrameLayout(mContext);
        mWaterGridView.setHeaderView(mHeaderViewContainer);
    }


    @FaceEventCallBackINforDlp
    private void registerListeners() {
        PullToRefreshStaggeredGridView_2.OnRefreshListener onRefreshListener = new PullToRefreshStaggeredGridView_2.OnRefreshListener() {
            @Override
            public void onRefresh(boolean is) {
                //                if (isLoading()) {
//                    return;
//                }
                mLoadForViewManager.reLoadFromStart();
                isAddTopBanner = false;
            }
        };

        mPullStaggerGridView.setOnRefreshListener(onRefreshListener);


        StaggeredGridView.OnLoadmoreListener onLoadmoreListener = new StaggeredGridView.OnLoadmoreListener() {

            @Override
            public void onLoadmore() {
//                if (isLoading()) {
//                    return;
//                }
            mLoadForViewManager.loadNextPage(true);

            }
        };
        mWaterGridView.setOnLoadmoreListener(onLoadmoreListener);

        StaggeredGridView.OnFlingListener onFlingListener = new StaggeredGridView.OnFlingListener() {

            boolean isLoadPa=false;
            @Override
            public void onTouchFling() {
                if(isLoadPa)return;
                mLoadForViewManager.startLoadImage();
                isLoadPa=true;
            }

            @Override
            public void onFling() {
                if(!isLoadPa)return;
                mLoadForViewManager.pauseLoadImage();
                isLoadPa=false;
            }

            @Override
            public void endFling() {
                if(isLoadPa)return;
                mLoadForViewManager.startLoadImage();
                isLoadPa=true;
            }
        };
        mWaterGridView.setOnFlingListener(onFlingListener);
    }


    @FaceEventInforDlp
    @Override
    public void loading(boolean isHasMoreToLoad, boolean isMustNeedEndTag) {
        // 加载第一屏，即下拉刷新。
        if (mLoadForViewManager.isReloadFromStart()) {
            removeLoadingFooterView();
        }

        if (!isHasMoreToLoad) {
            if (isMustNeedEndTag) {
                addLoadingFooterView();
            } else {

            }
            return;
        }

        if (mWaterGridView != null) {
            mStaggerAllFootView.setVisibility(View.VISIBLE);
            mLoadingLayer.setVisibility(View.VISIBLE);
            mToButtomNoDataLayer.setVisibility(View.GONE);
        }

    }
    @FaceEventInforDlp
    @Override
    public void loaded(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToLoad, List data) {
        mPullStaggerGridView.onRefreshComplete();
        removeFooterView();
    }

    @FaceEventInforDlp
    @Override
    public void loadErr(int errKey, int errTimes, boolean isReload, Exception... err) {
        mPullStaggerGridView.onRefreshComplete();
        removeFooterView();
    }

    @Override
    public void backTopView() {
        mWaterGridView.setSelectionToTop();
    }

    @Override
    public void setLast(int i) {

    }

    @Override
    public void setSelectIndex(int index) {

    }

    @Override
    public void setBackToTopAndRefresh() {

    }

    @Override
    public void setSelectIndexByScroll(int index) {

    }

    @Override
    public void setPullToRefreshMode(int mode) {
        mPullStaggerGridView.setMode(mode);
    }

    @Override
    public int getColumnsNum() {
        return 2;
    }

    @Override
    public void callReportScrollStateChange() {

    }


    private void removeFooterView() {
        if (mWaterGridView != null) {
            mStaggerAllFootView.setVisibility(View.GONE);
        }
    }

    private void removeLoadingFooterView() {
        if (mWaterGridView != null) {
            mStaggerAllFootView.setVisibility(View.GONE);
            mLoadingLayer.setVisibility(View.VISIBLE);
            mToButtomNoDataLayer.setVisibility(View.GONE);
        }
    }

    private void addLoadingFooterView() {

        if (mWaterGridView != null) {
            mStaggerAllFootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStaggerAllFootView.setVisibility(View.VISIBLE);
                    mLoadingLayer.setVisibility(View.GONE);
                    mToButtomNoDataLayer.setVisibility(View.VISIBLE);
            
                }
            }, 50);
        }
    }


}
