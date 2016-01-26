package com.hxuehh.rebirth.capacity.USBLinkSenser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class USBLinkSenser extends DeviceCapacityBase {


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_USB_Link;
    }

    @Override
    public boolean testHardware_SDK() throws FaceException {
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
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if ( !(t instanceof USBLinkParameter)) throw new FaceException("参数类型出错");
        final USBLinkParameter tt = (USBLinkParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.isAskStatus()) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, isLink() ? "1" : "0");
            return commonDeviceCapacityOutResult;
        }
        return commonDeviceCapacityOutResult;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.USBLink_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }


    @Override
    public void onCreat() throws FaceException {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_STATE");
        SuApplication.getInstance().registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {
        stop();
    }

    @Override
    public boolean isShowStatus() {
        return true;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_usb_link_ac, false);
    }


    transient boolean isLink;

    public boolean isLink() {
        return isLink;
    }

    private transient BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.hardware.usb.action.USB_STATE")) {
                isLink = intent.getExtras().getBoolean("connected");
                addDeviceStatus(new RunningStatus(RunningStatus.Running, isLink() ? "1" : "0"));
            }
        }

    };

    public static class USBLinkParameter extends DeviceCapacityInParameter implements Serializable {
        public USBLinkParameter(boolean isAskOn) {
            super(isAskOn);
        }
    }


}
