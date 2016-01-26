package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;

/**
 * Created by suwg on 2014/6/4.
 */
public interface AdapterViewOnItemClickListener<GetOOForDomain_Container,ClickInMainView> {

    void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, GetOOForDomain_Container allData, LoadCursorSetting mLoadCursorSetting, ClickInMainView... t) throws RemoteException, FaceException;
}

