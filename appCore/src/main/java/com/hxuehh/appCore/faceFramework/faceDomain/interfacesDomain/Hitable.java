package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import android.content.Intent;

import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;

/**
 * Created by suwg on 2014/8/13.
 * 可以实现命中的
 * banner
 * push
 *
 *
 */
public interface Hitable<InViewHit_Params> {
    Intent hit(FaceBaseActivity_1 mContext, InViewHit_Params... objs);//命中跳转
}
