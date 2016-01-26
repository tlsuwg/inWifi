package com.hxuehh.rebirth.capacity.headsetSenser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.mediaPlayer.SUMediaPlayer;
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
public class HeadsetSenser extends DeviceCapacityBase {


    public HeadsetSenser(SUMediaPlayer mSUMediaPlayer) {
        this.mSUMediaPlayer = mSUMediaPlayer;
    }

    transient SUMediaPlayer mSUMediaPlayer;
    transient SUMediaPlayer.MediaPlayerParameter screenParameter = new SUMediaPlayer.MediaPlayerParameter(SUMediaPlayer.MediaPlayerParameter.TypePlay_Pause);
    transient FaceCommCallBack faceCommCallBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            try {
                mSUMediaPlayer.doChangeStatus(screenParameter);
            } catch (FaceException e) {
                e.printStackTrace();
                addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "因为" + getSelfTypeName() + "产生驱动，出错" + e.getMessage()));
            }
            return false;
        }
    };

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Headset;
    }

    transient boolean isLink;


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


    @Face_UnsolvedForDlp
    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {

        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if ( !(t instanceof HeadsetParameter)) throw new FaceException("参数类型出错");
        final HeadsetParameter tt = (HeadsetParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.getType() == HeadsetParameter.TypeGetStatus) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, (isLink ? "1" : "0"));
        }
        return commonDeviceCapacityOutResult;
    }

    @Override
    public boolean isShowStatus() {
        return true;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.LightSensor_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    transient BroadcastReceiver headsetPlugReceiver;

    @Override
    public void onCreat() {
        headsetPlugReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        isLink = false;
                        if(!isUserSettingSenserEnable())return;
//                        callBack();
                    } else if (intent.getIntExtra("state", 0) == 1) {
                        isLink = true;
                        if(!isUserSettingSenserEnable())return;
                        callBack();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        SuApplication.getInstance().registerReceiver(headsetPlugReceiver, filter);

    }

    private void callBack() {
        if (faceCommCallBack != null) {
            faceCommCallBack.callBack();
            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "因为" + getSelfTypeName() + "产生驱动"));
        }
    }

    @Override
    public void stop() {
        if (headsetPlugReceiver != null)
            SuApplication.getInstance().unregisterReceiver(headsetPlugReceiver);
    }

    @Override
    public void onDestry() {
        stop();
        headsetPlugReceiver = null;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_headset_ac, false);
    }

    @Face_UnsolvedForDlp
    public static class HeadsetParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int TypeGetStatus = 1;
        public static final int TypeSetCallBack = 2;

        public HeadsetParameter(int type) {
            super(type);
        }
    }

}
