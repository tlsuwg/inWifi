package com.hxuehh.rebirth.capacity.memory;

import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Memory extends DeviceCapacityBase {

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_memory;
    }

    @Override
    public boolean testHardware_SDK() {
        return true;
    }

    @Override
    public boolean isDevEnable() {
        return true;
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
        return false;
    }


    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if ( !(t instanceof MemoryParameter)) throw new FaceException("参数类型出错");

        final MemoryParameter tt = (MemoryParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.isAskStatus()) {
            String info = "系统："+SuApplication.getInstance().getOSAllMemory()/(1024*1024) + "MB；剩余" + SuApplication.getInstance().getOSFreeMemory()/(1024*1024)+"MB";
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, info);
            return commonDeviceCapacityOutResult;
        }
        return commonDeviceCapacityOutResult;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_memory_ac, false);
    }


    public static class MemoryParameter extends DeviceCapacityInParameter implements Serializable {
        public MemoryParameter(boolean isAskOn) {
            super(isAskOn);
        }
    }


}
