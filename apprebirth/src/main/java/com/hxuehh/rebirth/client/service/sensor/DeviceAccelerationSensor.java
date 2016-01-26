package com.hxuehh.rebirth.client.service.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.capacity.accelerationSensor.AccelerationSensor;
import com.hxuehh.rebirth.client.service.sensor.interfaces.ClientSensor;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by suwg on 2015/9/24.
 */
public class DeviceAccelerationSensor extends ClientSensor {
    FaceCommCallBack faceCommCallBack;

    public DeviceAccelerationSensor(FaceCommCallBack faceCommCallBack) {
        this.faceCommCallBack = faceCommCallBack;
    }

    AccelerationSensor mAccelerationSensor;
    SensorEventListener mSensorEventListener;

    @Override
    public void startLinstener() {
        mAccelerationSensor = new AccelerationSensor();
        try {
            boolean is = mAccelerationSensor.testHardware_SDK();
            if (!is) {
                mAccelerationSensor = null;
                return;
            }

            mSensorEventListener = new SensorEventListener() {
                List<Long> list = new LinkedList();
                int thisX = Integer.MAX_VALUE;
                boolean toBeHighting;//
                int max;
                boolean isMaXOnce;

                @Override
                public void onSensorChanged(SensorEvent event) {
//                        float x = event.values[SensorManager.DATA_X];
//                        float y = event.values[SensorManager.DATA_Y];
//                        float z = event.values[SensorManager.DATA_Z];
                    int x = (int) event.values[SensorManager.DATA_X];
                    if (thisX == x) return;
                    if (!isMaXOnce) {
                        max = x;
                        isMaXOnce = true;
                    }
                    if (x == 0) {
                        if (toBeHighting && Math.abs(max) > 2) {
                            test3(new Date().getTime());
                            max = x;
                            toBeHighting = false;
                        }
                        toBeHighting = true;
                    }
                    thisX = x;
                    if (Math.abs(x) > Math.abs(max)) {
                        max = x;
                    }
                }

                private void test3(long time) {
                    if (list.size() > 0) {
                        if (time - list.get(0) > AppStaticSetting.XYZ3TimesTime) {
                            list.remove(0);
                        }
                    }
                    list.add(time);
                    if (list.size() >= AppStaticSetting.XYZ3Times) {
                        list.clear();
                        callBack();
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            mAccelerationSensor.getmSensorManager().registerListener(mSensorEventListener, mAccelerationSensor.getmSensor(), SensorManager.SENSOR_DELAY_NORMAL);

        } catch (FaceException e) {
            e.printStackTrace();
            mAccelerationSensor = null;
        }
    }

    @Override
    public void endLinstener() {
        if (mAccelerationSensor == null) return;
        mAccelerationSensor.getmSensorManager().unregisterListener(mSensorEventListener);
    }

    private void callBack() {


        if (faceCommCallBack != null) faceCommCallBack.callBack(getType());
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Client_AccelerationSensor;
    }
}
