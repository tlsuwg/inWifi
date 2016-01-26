package com.hxuehh.rebirth.capacity.lightSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.scene.Scene;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by suwg on 2015/8/15.
 */
public class LightSensor extends DeviceCapacityBase {

    transient Scene mScene;
    transient Scene.SceneParameter sceneParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeCMDFrom, getType());
    public LightSensor(Scene mScene) {
        this.mScene = mScene;
    }

    private void callBack() {
        if(this.getmLockDeviceCapacity()!=null){
            if((this.light_lock_time-new Date().getTime())>=0) {
                addDeviceStatus(new RunningStatus(RunningStatus.Running, "因为" + getSelfTypeName() + "产生驱动,但是被" + this.getmLockDeviceCapacity().getSelfTypeName() + "锁闭，放弃"));
                return;
            }else{
                unLockNull();
            }
        }

        try {
            mScene.doChangeStatus(sceneParameter);
        } catch (FaceException e) {
            e.printStackTrace();
            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "因为" + getSelfTypeName() + "产生驱动，出错" + e.getMessage()));
        }
    }


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_LightSensor;
    }

    transient Sensor mSensor;
    transient SensorManager mSensorManager;
    transient float mValue;
    transient int mValue_min;
    transient int mValue_max;

    public float getmValue() {
        return mValue;
    }

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


    @Face_UnsolvedForDlp
    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (!(t instanceof TemperatureParameter)) throw new FaceException("参数类型出错");
        final TemperatureParameter tt = (TemperatureParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.getType() == TemperatureParameter.TypeGetStatus) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "当前：" + this.mValue + ";统计最大" + this.mValue_max + ";统计最小" + this.mValue_min);
        } else if (tt.getType() == TemperatureParameter.TypeSetMin) {
            if (tt.set < 0) throw new FaceException("不能设置负数");
            mValue_min = (int) tt.set;
            SharedPreferencesUtils.putInteger(SharedPreferencesKeys.LightSensor_MIN, mValue_min);
            addDeviceStatus(new RunningStatus(RunningStatus.Running, "手动设置下限" + mValue_min));
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
        return DeviceCapacitySetting.LightSensor_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {
    }

    transient SensorEventListener mSensorEventListener;

    @Override
    public void onCreat() throws FaceException {
        if (mSensorManager == null) {
            getBase();
        }
        int min = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.LightSensor_MIN);
        int max = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.LightSensor_MAN);
        if (min >= 0) {
            mValue_min = min;
        } else {
            mValue_min = 1000000;
        }

        if (max < 0) {
            mValue_max = 0;
        } else {
            mValue_max = max;
        }


        if (mSensorEventListener == null) {
            mSensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(!isUserSettingSenserEnable())return;
                    mValue = event.values[0];
                    addDeviceStatus(new RunningStatus(RunningStatus.Running, mValue + ""));
                    if (mValue_min > mValue) {
                        mValue_min = (int) mValue;
                        SharedPreferencesUtils.putInteger(SharedPreferencesKeys.LightSensor_MIN, mValue_min);
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, "设置下限" + mValue_min));
                        callBack();
                    } else if (mValue_min == (int) mValue) {
                        callBack();
                    }
                    if (mValue_max < (int) mValue) {
                        mValue_max = (int) mValue;
                        SharedPreferencesUtils.putInteger(SharedPreferencesKeys.LightSensor_MAN, mValue_max);
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, "设置上限" + mValue_max));
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
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
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_ligth_sensor_ac, false);
    }

    public void getBase() throws FaceException {
        mSensorManager = (SensorManager) SuApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) throw new FaceException("没有传感器管理者");
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mSensor == null) throw new FaceException("没有光传感器");
    }


    public boolean isMinValue() {
       return mValue==mValue_min;
    }

    @Face_UnsolvedForDlp
    public static class TemperatureParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int TypeGetStatus = 1;
        public static final int TypeSetMin = 2;
        int set;

        public TemperatureParameter(int type, int set) {
            super(type);
            this.set = set;
        }
    }

}
