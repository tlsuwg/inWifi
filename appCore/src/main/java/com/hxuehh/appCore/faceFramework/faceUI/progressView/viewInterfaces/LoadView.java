package com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;

import java.util.List;

/**
 * Created by kaizen on 14-6-30.
 */
public abstract class LoadView {


    private boolean isMustLoadNextFromTop;//是不是从头来加载下一页
    public boolean isMustLoadNextFromTop() {
        return isMustLoadNextFromTop;
    }

    public void setIsMustLoadNextFromTop(boolean isMustLoadNextFromTop) {
        this.isMustLoadNextFromTop = isMustLoadNextFromTop;
    }

    protected ViewGroup mHeaderViewContainer;
    protected boolean isAutoLoadData = true;

    public abstract void loading(boolean isHasMoreToLoad, boolean isMustNeedEndTag);

    public abstract void loaded(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToLoad, List data);

    public abstract void loadErr(int errKey, int errTimes, boolean isReload, Exception... err);

    protected View mainView;

    public View getMainView() {
        return mainView;
    }


    protected View headView;

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
        if (mHeaderViewContainer != null) {
            int childCount = mHeaderViewContainer.getChildCount();
            if (childCount > 0) {
                mHeaderViewContainer.removeViews(0, childCount - 1);
            }

            mHeaderViewContainer.addView(headView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

//            mHeaderViewContainer.addView(headView,
//                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            mHeaderViewContainer.addView(headView);
        }
    }

    public abstract void backTopView();

    public abstract void setLast(int i);

    public abstract void setSelectIndex(int index);

    public abstract void setBackToTopAndRefresh();

    public abstract void setSelectIndexByScroll(int index);

    public void setAutoLoadData(boolean autoLoadData) {
        this.isAutoLoadData = autoLoadData;
    }

    public boolean isAutoLoadData() {
        return isAutoLoadData;
    }

    public abstract void setPullToRefreshMode(int mode);

    public void startLoadNextPageUI() {

    }
    public void endLoadNextPageUI() {

    }


    protected FaceCommCallBack faceCommCallBackForShowTopView;//比例使用
    protected FaceCommCallBack faceCommCallBackTabClick;

    public void setFaceCommCallBackForShowTopView(FaceCommCallBack faceCommCallBackForShowTopView) {
        this.faceCommCallBackForShowTopView = faceCommCallBackForShowTopView;
    }
    public void setFaceCommCallBackTabClick(FaceCommCallBack faceCommCallBackTabClick){
        this.faceCommCallBackTabClick=faceCommCallBackTabClick;
    }

    // 母婴返回顶部检测
    protected FaceCommCallBack faceCommCallBackMuyingBackTop;
    public void setFaceCommCallBackMuyingBackTop(FaceCommCallBack faceMuyingBackTop){
        faceCommCallBackMuyingBackTop = faceMuyingBackTop;
    }


    public abstract int getColumnsNum();

    public abstract void callReportScrollStateChange();//主要是为了唤醒sc滑动

    public void hideFootView(){};
}
