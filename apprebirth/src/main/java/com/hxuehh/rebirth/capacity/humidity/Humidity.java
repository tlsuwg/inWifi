package com.hxuehh.rebirth.capacity.humidity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
//湿度
public class Humidity extends DeviceCapacityBase {
    @Override
    public int getType() {
        return DeviceCapacityBase.Type_humidity;
    }

    transient Sensor mSensor;
    transient SensorManager mSensorManager;
    transient float humidityValue ;
    transient SensorEventListener mSensorEventListener;

    @Override
    public boolean testHardware_SDK() throws FaceException {
        getBase();
        boolean is = mSensor != null;
        return is;
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
        if ( !(t instanceof HumidityParameter)) throw new FaceException("参数类型出错");

        final HumidityParameter tt = (HumidityParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.isAskStatus()) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, humidityValue + "");
            return commonDeviceCapacityOutResult;
        }
        return commonDeviceCapacityOutResult;
    }
    @Override
    public boolean isShowStatus() {
        return true;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Temperature_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    @Override
    public void onCreat() throws FaceException {
        if (mSensorManager == null) {
            getBase();
        }
         mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                humidityValue = event.values[0];
                addDeviceStatus(new RunningStatus(RunningStatus.Running, humidityValue + ""));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stop() {
        if (mSensorManager != null && mSensorEventListener != null)
            mSensorManager.unregisterListener(mSensorEventListener);
    }

    @Override
    public void onDestry() {
        stop();
        mSensorManager = null;
        mSensorEventListener = null;
        mSensor = null;
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_humidity_ac, false);
    }

    public void getBase() throws FaceException {
        mSensorManager = (SensorManager) SuApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) throw new FaceException("没有传感器管理者");
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mSensor == null) throw new FaceException("没有湿度传感器");
    }


    public static class HumidityParameter extends DeviceCapacityInParameter implements Serializable {
        public HumidityParameter(boolean isAskOn) {
            super(isAskOn);
        }
    }


}
