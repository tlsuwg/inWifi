package com.hxuehh.rebirth.device.domain.imp;

import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;

/**
 * Created by suwg on 2015/9/11.
 */

//只是为了转化一下类型
public class AgencyCapacity extends DeviceCapacityBase
{

    @Override
    public int getType() {
        return type ;
    }

    @Override
    public boolean testHardware_SDK() {
        return false;
    }

    @Override
    public boolean isDevEnable() {
        return false;
    }

    @Override
    public String getDevUnEnableInfo() {
        return null;
    }

    @Override
    public int getMAXHistorySize() {
        return 0;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    @Override
    public void onCreat() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {

    }

    @Override
    public boolean isShowStatus() {
        return true;
    }

    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        return null;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting,Object[] oos) throws RemoteException ,FaceException{

    }
}
