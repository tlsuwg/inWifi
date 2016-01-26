package com.hxuehh.rebirth.capacity.cameraFlashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
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
import java.util.List;

/**
 * Created by suwg on 2015/8/15.
 */
public class CameraFlashLight extends DeviceCapacityBase {
    transient Camera mCamera;
    transient boolean isOn;

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_FlashLight;
    }

   transient boolean is;
    @Override
    public boolean testHardware_SDK() {
         is = SuApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        return is;
    }

    @Override
    public boolean isHasTrueWorkerOnDuty() {
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
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Screen_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    @Override
    public void onCreat() {

    }

    @Override
    public void stop() {
        try {
            turnLightOff();
        } catch (FaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestry() {
        stop();
    }

    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if ( !(t instanceof FlashlightParameter)) throw new FaceException("参数类型出错");

        final FlashlightParameter tt = (FlashlightParameter) t;

        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult=new CommonDeviceCapacityOutResult(true);

        if (tt.isAskStatus()) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Device_status,getLightStatus()?"1":"0");
            return commonDeviceCapacityOutResult;
        } else {
            switch (tt.getType()){
                case FlashlightParameter.Type_ON:{
                    turnLightOn();
                    addDeviceStatus(new RunningStatus(RunningStatus.Running, tt.toString()));
//                    wareing  超时
//                    ThreadManager.getInstance().getNewThread("close pm", new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(tt.timeOut);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            if(tt.isTheSameTag(getLastTagTime())) {
//                                stop();
//                            }
//                        }
//                    }).start();
                }
                break;
                case FlashlightParameter.Type_Off:{
                    turnLightOff();
                }
                break;
            }



        }
        return commonDeviceCapacityOutResult;
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
      super.OnItemClickListener(mContext,view,position,allData,mLoadCursorSetting,t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_flashlight_ac, false);
    }



//    =================================

    private Camera getmCamera() throws FaceException {
        try {
            return Camera.open();
        } catch (Exception e) {
            throw new FaceException("设备占用,无法操作");
        }
    }

    @Override
    public boolean isShowStatus() {
        return false;
    }


    private void turnLightOn() throws FaceException {
        mCamera = getmCamera();
        if (mCamera == null) throw new FaceException("没有获取到控制对象");
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) throw new FaceException("没有获取到控制对象参数");
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) throw new FaceException("没有获取到控制对象参数");
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mLightSensorLockOneTimes();
                mCamera.setParameters(parameters);
                isOn = true;
            } else {

            }
        }
    }

    private boolean getLightStatus() throws FaceException {
        Camera cam;
        boolean isOpen = false;
        if (mCamera != null) {
            cam = mCamera;
        } else {
            cam = getmCamera();
            isOpen = true;
        }
        if (isOpen) {
            cam.release();
        }
        Camera.Parameters p = cam.getParameters();
        return Camera.Parameters.FLASH_MODE_TORCH.equals(p.getFlashMode());
    }


    private void turnLightOff() throws FaceException {
        if (isOn) {
            try {
                if (mCamera == null) throw new FaceException("没有获取到控制对象");
                Camera.Parameters parameters = mCamera.getParameters();
                if (parameters == null) throw new FaceException("没有获取到控制对象参数");
                List<String> flashModes = parameters.getSupportedFlashModes();
                if (flashModes == null) throw new FaceException("没有获取到控制对象参数");
                String flashMode = parameters.getFlashMode();
                if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
                    if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mLightSensorUnLock100();
                        mCamera.setParameters(parameters);
                    } else {

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mCamera.release();
                mCamera = null;
                isOn = false;
            }
        } else {
//            已经关闭
        }
    }



    public static class FlashlightParameter extends DeviceCapacityInParameter implements Serializable {
       public  static final int Type_ON =1;
        public  static final int Type_Off =2;

        public FlashlightParameter(int type) {
            super(type);
        }
    }


}
