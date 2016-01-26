package com.hxuehh.rebirth.all.domain;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Parseable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by suwg on 2015/8/31.
 */

//<GetOOForParse_Base,GetOOForParse_Container,GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV,InViewShow_Params,InViewHit_Params>

public class DeviceInfoInsinuate extends BaseAllImp<MidMessage, MidMessage, List, Object, LoadCursorSetting, String, Object, Object,Object> {

    private DeviceInfo mDeviceInfo;

    public DeviceInfo getmDeviceInfo() {
        return mDeviceInfo;
    }

    public void setmDeviceInfo(DeviceInfo mDeviceInfo) {
        this.mDeviceInfo = mDeviceInfo;
    }


    public DeviceInfoInsinuate(int viewKey) {
        super(viewKey);
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext,
                                    View view, int position, List allData, com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadCursorSetting,Object oo[]) {

    }


    @Override
    public String inToCache(LoadCursorSetting key, String value) {
        return null;
    }

    @Override
    public String getValueByKey(LoadCursorSetting key) {
        return null;
    }

    @Override
    public String removeFromCacheByKey(LoadCursorSetting key1) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Intent hit(FaceBaseActivity_1 mContext, Object... objs) {
        return null;
    }

    @Override
    public View getView(Activity mContext, int i, View view, ViewGroup viewGroup, LayoutInflater inflater, int viewItemKey, Object... params) throws Exception {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public Future loadInWhich_Thread_Source(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack) {
        return null;
    }

    @Override
    public boolean loadTrueByMCache(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        return false;
    }

    @Override
    public boolean loadTrueByDBCache(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        return false;
    }

    @Override
    public boolean loadTrueByFileCache(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        return false;
    }

    @Override
    public boolean loadTrueByHttp(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        return false;
    }

    @Override
    public void loadSetting(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, List data, FaceLoadCallBack mFaceCallBack, Object json) {

    }

    @Override
    public void parseAll(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, MidMessage jsonString, long key) {

    }

    @Override
    public Parseable getItemValue(com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting mLoadSetting, MidMessage mJsonObject) throws Exception {
        return null;
    }
}
