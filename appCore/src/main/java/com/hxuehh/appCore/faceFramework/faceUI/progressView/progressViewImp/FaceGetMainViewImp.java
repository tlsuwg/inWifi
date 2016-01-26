package com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp;

import android.content.Context;

import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrap;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;


/**
 * Created by suwg on 2015/07/09.
 */

public abstract class FaceGetMainViewImp extends GetMainView implements FaceContextWrap {

    private FaceContextWrapImp mContextWrap;

    @Deprecated
    public FaceGetMainViewImp(Context context) {
        super(context);
        this.mContextWrap = new FaceContextWrapImp((FaceBaseActivity_1)context);
    }

    @Deprecated
    protected FaceGetMainViewImp(FaceContextWrapImp context) {
        super(context.getFaceContext());
        this.mContextWrap = context;
    }

    public FaceGetMainViewImp(FaceContextWrapImp context, int Viewkey) {
        super(context.getFaceContext());
        this.mContextWrap = context;
        setViewKey(Viewkey);
    }


    @Override
    public FaceBaseActivity_1 getFaceContext() {
        return mContextWrap.getFaceContext();
    }



}
