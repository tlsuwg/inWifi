package com.hxuehh.appCore.faceFramework.faceUI.androidWrap;

import android.os.Handler;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ViewKeyable;
import com.hxuehh.appCore.faceFramework.faceUI.fragments.FaceBaseFragment;

/**
 * Created by suwg on 2015/7/7.
 */
public class FaceContextWrapImp implements HandlerWrap, IsOnTop, ViewKeyable,FaceContextWrap {

    private FaceBaseActivity_1 mFaceBaseActivity_1;
    private FaceBaseFragment mFaceBaseFragment;

    public FaceContextWrapImp(FaceBaseActivity_1 mFaceBaseActivity_1) {
        this.mFaceBaseActivity_1 = mFaceBaseActivity_1;
    }

    public FaceContextWrapImp(FaceBaseFragment mFaceBaseFragment) {
        this.mFaceBaseFragment = mFaceBaseFragment;
        this.mFaceBaseActivity_1=mFaceBaseFragment.getFaceContext();
    }

    @Override
    public int getViewKey() {
        return mFaceBaseFragment != null ? mFaceBaseFragment.getViewKey() : mFaceBaseActivity_1.getViewKey();
    }

    @Override
    public void setViewKey(int key) {

    }

    @Override
    public Handler getHandler() {
        return mFaceBaseActivity_1.getHandler();
    }

    @Override
    public void setHandler(Handler mHandler) {
        if(mFaceBaseActivity_1!=null)mFaceBaseActivity_1.setHandler(mHandler);
    }

    @Override
    public boolean isOnTop() {
        return mFaceBaseFragment != null ? mFaceBaseFragment.isOnTop() : mFaceBaseActivity_1.isOnTop();
    }

    @Override
    public void setOnTop(boolean isOnTop) {
        if (mFaceBaseFragment != null) mFaceBaseFragment.setOnTop(isOnTop);
        else
            mFaceBaseActivity_1.setOnTop(isOnTop);
    }

    @Override
    public FaceBaseActivity_1 getFaceContext() {
         return mFaceBaseFragment != null ? mFaceBaseFragment.getFaceContext(): mFaceBaseActivity_1;
    }


}
