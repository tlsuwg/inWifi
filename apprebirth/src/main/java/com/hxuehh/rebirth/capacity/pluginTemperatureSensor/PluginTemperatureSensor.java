package com.hxuehh.rebirth.capacity.pluginTemperatureSensor;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.USBcommunication.USBcommunicationKeys;
import com.hxuehh.rebirth.capacity.scene.Scene;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.DeviceCapacitySensorBase;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by suwg on 2015/8/15.
 */
public class PluginTemperatureSensor extends DeviceCapacitySensorBase {

    transient Scene mScene;
    transient Scene.SceneParameter sceneParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeCMDFrom, getType());
    public PluginTemperatureSensor(Scene mScene) {
        this.mScene = mScene;
    }

    byte mbyte;//当前状态

    @Override
    public void onUnused() {
        super.onUnused();
        mbyte=0;
    }

    @Override
    public void onSensorDataChange(int type,byte... bytes) {
        mbyte=bytes[0];
        addDeviceStatus(new RunningStatus(RunningStatus.Running, "状态:" + mbyte +";启用"+(isUserSettingEnable()?"1":"0")));
        if(!isUserSettingSenserEnable())return;
//        if(mbyte==1)
//        callBack();
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
        return DeviceCapacityBase.Type_Add_Pluginy_Temperature;
    }


    @Override
    public boolean testHardware_SDK() throws FaceException {
        try {
            UsbManager   mUsbManager = (UsbManager) SuApplication.getInstance().getSystemService(Context.USB_SERVICE);
        } catch (Exception e) {
            return false;
        }
        return true;
    }





    @Face_UnsolvedForDlp
    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (!(t instanceof PluginTemperatureSensorParameter)) throw new FaceException("参数类型出错");
        final PluginTemperatureSensorParameter tt = (PluginTemperatureSensorParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.getType() == PluginTemperatureSensorParameter.TypeGetStatus) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "当前："+ mbyte);
        }
        return commonDeviceCapacityOutResult;
    }

    @Override
    public boolean getAdaptiveByFatherUSBSuUsbAccessoryKey(int fatherUSBSuUsbAccessoryKey) {
        if(fatherUSBSuUsbAccessoryKey== USBcommunicationKeys.WCH_Light_Machine)return true;
        return false;
    }


    @Override
    public boolean isShowStatus() {
        return true;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.PluginTemperatureSensor_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {
    }



    @Override
    public void onCreat() throws FaceException {

    }


    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {
        stop();
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_add_temperature_sensor_ac, false);
    }




    @Face_UnsolvedForDlp
    public static class PluginTemperatureSensorParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int TypeGetStatus = 1;
        public PluginTemperatureSensorParameter(int type) {
            super(type);
        }
    }

}
