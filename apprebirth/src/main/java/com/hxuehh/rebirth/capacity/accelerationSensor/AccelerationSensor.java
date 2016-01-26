package com.hxuehh.rebirth.capacity.accelerationSensor;

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
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by suwg on 2015/8/15.
 */
public class AccelerationSensor extends DeviceCapacityBase {

    transient Scene mScene;
    transient Scene.SceneParameter sceneParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeCMDFrom, getType());


    public AccelerationSensor(Scene mScene) {
        this.mScene = mScene;
    }

    private void callBack() {
        if (this.getmLockDeviceCapacity() != null) {
            if ((this.light_lock_time - new Date().getTime()) >= 0) {
                addDeviceStatus(new RunningStatus(RunningStatus.Running, "因为" + getSelfTypeName() + "产生驱动,但是被" + this.getmLockDeviceCapacity().getSelfTypeName() + "锁闭，放弃"));
                return;
            } else {
                unLockNull();

            }
        }

        try {
            unLock100();
            mScene.doChangeStatus(sceneParameter);

        } catch (FaceException e) {
            e.printStackTrace();
            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "因为" + getSelfTypeName() + "产生驱动，出错" + e.getMessage()));
        }
    }

    public AccelerationSensor() {
    }


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_AccelerationSensor;
    }

    transient Sensor mSensor;
    transient SensorManager mSensorManager;

    transient int AccelerationSensorAmplification = 1;//放大倍数
    transient int Device_AccelerationSensor_Y = AppStaticSetting.Device_AccelerationSensor_Y,
            Device_AccelerationSensor_X = AppStaticSetting.Device_AccelerationSensor_X,
            Device_AccelerationSensor_Z = AppStaticSetting.Device_AccelerationSensor_Z;


    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public Sensor getmSensor() {
        return mSensor;
    }

    @Override
    public boolean testHardware_SDK() throws FaceException {
        getBase();
        boolean is = mSensor != null;

        if (is) {
            int iii = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.AccelerationSensorAmplification);
            if (iii > 1) {
                AccelerationSensorAmplification = iii;
            }
            int mDevice_AccelerationSensor_X = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.Device_AccelerationSensor_X);
            if (mDevice_AccelerationSensor_X > 0) {
                this.Device_AccelerationSensor_X = mDevice_AccelerationSensor_X;
            }
            int mDevice_AccelerationSensor_Y = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.Device_AccelerationSensor_Y);
            if (mDevice_AccelerationSensor_Y > 0) {
                this.Device_AccelerationSensor_Y = mDevice_AccelerationSensor_Y;
            }
            int mDevice_AccelerationSensor_Z = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.Device_AccelerationSensor_Z);
            if (mDevice_AccelerationSensor_Z > 0) {
                this.Device_AccelerationSensor_Z = mDevice_AccelerationSensor_Z;
            }

        }
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
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3 = super.doChangeStatus(t);
        if (mDeviceCapacityOutResult_3 != null) return mDeviceCapacityOutResult_3;
        if (!(t instanceof AccelerationSensorParameter)) throw new FaceException("参数类型出错");
        if (mSensor == null) new FaceException("不存在该传感器");
        final AccelerationSensorParameter tt = (AccelerationSensorParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);

        switch (tt.getType()) {
            case AccelerationSensorParameter.Type_AskStatus: {
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "当前位置：X" + this.thisX + " Y" + this.thisY + " Z" + this.thisZ
                                + StringUtil.N + " 变化倍数扩展：" + this.AccelerationSensorAmplification + "；波动范围0-"+AccelerationSensorAmplification*10
                                + StringUtil.N + " 敏感度（最低变化量触发）X：" + this.Device_AccelerationSensor_X + " Y：" + this.Device_AccelerationSensor_Y + " Z：" + this.Device_AccelerationSensor_Z
                );
            }
            break;

            case AccelerationSensorParameter.Type_Amplification: {
                if (tt.getmAmplification() <= 0) throw new FaceException("必须是大于0的扩展倍数");
                double d = tt.getmAmplification() / AccelerationSensorAmplification;
                AccelerationSensorAmplification = tt.getmAmplification();
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.AccelerationSensorAmplification, AccelerationSensorAmplification);

                setXYZ((int) (this.Device_AccelerationSensor_X * d),
                        (int) (this.Device_AccelerationSensor_Y * d),
                        (int) (this.Device_AccelerationSensor_Z * d));

                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "倍数设置:" + tt.getmAmplification() + "波动范围0-"+tt.getmAmplification()*10
                                + "敏感度（最低变化量触发）ZYX "
                                + this.Device_AccelerationSensor_X
                                + " " + this.Device_AccelerationSensor_Y
                                + " " + this.Device_AccelerationSensor_Z
                );

            }
            break;
            case AccelerationSensorParameter.Type_Setting_X: {
                if (tt.xyz < 0) {
                    throw new FaceException("数值必须大于0");
                }
                setXYZ(tt.xyz, -1, -1);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "数值较大，灵敏度较低,浮动范围0--" + this.AccelerationSensorAmplification * 10 + "您设置" + tt.xyz);
            }
            break;

            case AccelerationSensorParameter.Type_Setting_Y: {
                if (tt.xyz < 0) {
                    throw new FaceException("数值必须大于0");
                }
                setXYZ(-1, tt.xyz, -1);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "数值较大，灵敏度较低,浮动范围0--" + this.AccelerationSensorAmplification * 10 + "您设置" + tt.xyz);
            }
            break;

            case AccelerationSensorParameter.Type_Setting_Z: {
                if (tt.xyz < 0) {
                    throw new FaceException("数值必须大于0");
                }
                setXYZ(-1, -1, tt.xyz);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "数值较大，灵敏度较低,浮动范围0--" + this.AccelerationSensorAmplification * 10 + "您设置" + tt.xyz);
            }
            break;


            case AccelerationSensorParameter.Type_Setting_Init: {
                Device_AccelerationSensor_Y = AppStaticSetting.Device_AccelerationSensor_Y;
                Device_AccelerationSensor_X = AppStaticSetting.Device_AccelerationSensor_X;
                Device_AccelerationSensor_Z = AppStaticSetting.Device_AccelerationSensor_Z;
                setXYZ(Device_AccelerationSensor_X, Device_AccelerationSensor_Y, Device_AccelerationSensor_Z);
                AccelerationSensorAmplification=1;
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.AccelerationSensorAmplification, AccelerationSensorAmplification);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "数值较大，灵敏度较低,浮动范围0--" + this.AccelerationSensorAmplification * 10 + "您设置" + tt.xyz);
            }
            break;

        }

        return commonDeviceCapacityOutResult;
    }

    private void setXYZ(int x, int y, int z) {

        if (x > 0) {
            this.Device_AccelerationSensor_X = x;
            SharedPreferencesUtils.putInteger(SharedPreferencesKeys.Device_AccelerationSensor_X, x);
        }

        if (y > 0) {
            this.Device_AccelerationSensor_Y = y;
            SharedPreferencesUtils.putInteger(SharedPreferencesKeys.Device_AccelerationSensor_Y, y);
        }

        if (z > 0) {
            this.Device_AccelerationSensor_Z = z;
            SharedPreferencesUtils.putInteger(SharedPreferencesKeys.Device_AccelerationSensor_Z, z);
        }
    }

    @Override
    public boolean isShowStatus() {
        return true;
    }

    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Type_AccelerationSensor_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {
    }

    transient SensorEventListener mSensorEventListener;
    transient int thisX = Integer.MAX_VALUE, thisY = Integer.MAX_VALUE, thisZ = Integer.MAX_VALUE;

    @Override
    public void onCreat() throws FaceException {
        if (mSensorManager == null) {
            getBase();
        }
        if (mSensorEventListener == null) {
            mSensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if(!isUserSettingSenserEnable())return;

                    float x = event.values[SensorManager.DATA_X];
                    float y = event.values[SensorManager.DATA_Y];
                    float z = event.values[SensorManager.DATA_Z];
                    boolean isChange = isChangeX((int) (x * AccelerationSensorAmplification));
                    isChange = isChange || isChangeY((int) (y * AccelerationSensorAmplification));
                    isChange = isChange || isChangeZ((int) (z * AccelerationSensorAmplification));

                    if (isChange) {
//                        long lastChangeTime=DeviceCapacityBase.getStaticLastTagTime();
//                        long now=new Date().getTime();
//                        boolean isOtherTimeOut=now-lastChangeTime>=onOtherChangeAccuracyChangedTimeOut;
//                        if(isOtherTimeOut)
                        callBack();
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private boolean isChangeZ(int z) {
        if (thisZ != Integer.MAX_VALUE) {
            if (thisZ == z) return false;
            boolean is = Math.abs((Math.abs(thisZ) - Math.abs(z))) >= Device_AccelerationSensor_Z;
            this.thisZ = (int) z;
            return is;
        }
        this.thisZ = z;
        return false;
    }

    private boolean isChangeY(int y) {
        if (thisY != Integer.MAX_VALUE) {
            if (thisY == y) return false;
            boolean is = Math.abs((Math.abs(thisY) - Math.abs(y))) >= Device_AccelerationSensor_Y;
            this.thisY = y;
            return is;
        }
        this.thisY = y;
        return false;
    }

    private boolean isChangeX(int x) {
        if (thisX != Integer.MAX_VALUE) {
            if (thisX == x) return false;
            boolean is = Math.abs((Math.abs(thisX) - Math.abs(x))) >= Device_AccelerationSensor_X;
            this.thisX = (x);
            return is;
        }
        this.thisX = x;
        return false;
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
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_acceleration_sensor_ac, false);
    }

    public void getBase() throws FaceException {
        mSensorManager = (SensorManager) SuApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) throw new FaceException("没有传感器管理者");
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mSensor == null) throw new FaceException("没有" + getSelfTypeName() + "感器");
    }


    @Face_UnsolvedForDlp
    public static class AccelerationSensorParameter extends DeviceCapacityInParameter implements Serializable {
        public AccelerationSensorParameter(int type) {
            super(type);
        }

        public static final int Type_Amplification = 1;//放大倍数
        public static final int Type_Setting_X = 2;
        public static final int Type_Setting_Y = 3;
        public static final int Type_Setting_Z = 4;

        public static final int Type_Setting_Init = 5;

        private int mAmplification;

        int xyz;

        public int getXyz() {
            return xyz;
        }

        public void setXyz(int xyz) {
            this.xyz = xyz;
        }

        public int getmAmplification() {
            return mAmplification;
        }

        public void setmAmplification(int mAmplification) {
            this.mAmplification = mAmplification;
        }
    }

}
