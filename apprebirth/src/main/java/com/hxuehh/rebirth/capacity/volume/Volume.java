package com.hxuehh.rebirth.capacity.volume;

import android.content.Context;
import android.media.AudioManager;
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
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
//音量
public class Volume extends DeviceCapacityBase {

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Volume;
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
        return DeviceCapacitySetting.Volume_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }


    transient int maxVolume;
    transient AudioManager mAudioManager;
    @Override
    public void onCreat() {
         mAudioManager = (AudioManager) SuApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
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
        if ( !(t instanceof VolumeParameter)) throw new FaceException("参数类型出错");
        final VolumeParameter tt = (VolumeParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);

        if (tt.isAskStatus()) {
//            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Device_status, getLightStatus() ? "1" : "0");
            return commonDeviceCapacityOutResult;
        } else {
            switch (tt.getType()) {
                case VolumeParameter.Type_addition: {
                    int now = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (now == maxVolume) {
                        throw new FaceException("已经是最大");
                    }
                    int set = now + AppStaticSetting.VolumeSetting;
                    if (set > maxVolume) set = maxVolume;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, set, 0);
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/"+maxVolume);
                }
                break;
                case VolumeParameter.Type_addition_max: {
                    int now = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (now == maxVolume) {
                        throw new FaceException("已经是最大");
                    }
                    int set = maxVolume;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, set, 0);
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/"+maxVolume);
                }
                break;
                case VolumeParameter.Type_subtraction: {
                    int now = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (now == 0) {
                        throw new FaceException("已经是最小");
                    }
                    int set = now - AppStaticSetting.VolumeSetting;
                    if (set <0) set = 0;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, set, 0);
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/"+maxVolume);
                }
                break;
                case VolumeParameter.Type_subtraction_mid: {
                    int now = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (now == 0) {
                        throw new FaceException("已经是最小");
                    }
                    int set =0;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, set, 0);
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, set + "/"+maxVolume);
                }
                break;


                default:
                    throw new FaceException("未知参数");
            }
        }

        return commonDeviceCapacityOutResult;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_volume_ac, false);
    }


    public static class VolumeParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int Type_addition = 1;
        public static final int Type_subtraction = 2;
        public static final int Type_addition_max = 3;
        public static final int Type_subtraction_mid = 4;

        public VolumeParameter(boolean isAskOn) {
            super(isAskOn);
        }
        public VolumeParameter(int type) {
            super(type);
        }
    }





}
