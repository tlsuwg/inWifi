package com.hxuehh.rebirth.capacity.pluginIOInSensor;

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
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.capacity.scene.Scene;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.DeviceCapacitySensorBase;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by suwg on 2015/8/15.
 */
public class PluginIOInSensor extends DeviceCapacitySensorBase {

    transient Scene mScene;
    transient Scene.SceneParameter sceneParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeCMDFrom, getType());

    public PluginIOInSensor(Scene mScene) {
        this.mScene = mScene;
    }

    transient USBSerializ.USBSerializParameter mUSBSerializParameter;

    transient  byte mBinaryInNew[], mBinaryInOld[];//当前状态
    transient   byte changedBinary[];


    @Override
    public boolean getAdaptiveByFatherUSBSuUsbAccessoryKey(int fatherUSBSuUsbAccessoryKey) {
        if (fatherUSBSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO ||
                fatherUSBSuUsbAccessoryKey == USBcommunicationKeys.WCH_120IO) return true;
        return false;
    }

    @Override
    public void onUsed(int fatherUSBSuUsbAccessoryKey, DeviceCapacityBase mDeviceCapacityBase) {
        super.onUsed(fatherUSBSuUsbAccessoryKey, mDeviceCapacityBase);
        USBSerializ mUSBSerializ = (USBSerializ) mDeviceCapacityBase;

        mBinaryInNew = MathUtil.bytesTobit(mUSBSerializ.getIOInBytes40_120());
        changedBinary = mBinaryInNew;
    }


    byte[] allsend;

    @Override
    public void onSensorDataChange(int type, byte... bytes) {
        byte[] byteOut40_120 = null;
        if (mDeviceCapacityBase == null) {
            return;
        }
        USBSerializ mUSBSerializ = (USBSerializ) mDeviceCapacityBase;
        byteOut40_120 = mUSBSerializ.getIOOutBinary40_120().clone();//二进制
        boolean isChangeed = false;

        mBinaryInOld = mBinaryInNew;
        mBinaryInNew = MathUtil.bytesTobit(bytes);
        for (int i = 0; i < mBinaryInNew.length; i++) {
            if (mBinaryInOld[i] == mBinaryInNew[i]) {
                changedBinary[i] = 0;
            } else {
                changedBinary[i] = 1;//变化
                Boolean isON=mBinaryInNew[i]==1;//必须是上电的时候
                if (isON&&byteOut40_120 != null && byteOut40_120.length > i) {
                    isChangeed = true;
                    int key = byteOut40_120[i];
                    if (key == 0) {
                        byteOut40_120[i] = 1;
                    } else {
                        byteOut40_120[i] = 0;
                    }
                }
            }
        }

        if (isChangeed) {
            allsend = new byte[byteOut40_120.length / 8 + 1];
            try {
                byte[] corebytes = MathUtil.bitToBytes(byteOut40_120);
                allsend[0] = 0x22;
                System.arraycopy(corebytes, 0, allsend, 1, corebytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            allsend=null;
        }

        addDeviceStatus(new RunningStatus(RunningStatus.Running, "data in:" + StringUtil.getByteString(mBinaryInNew)  + ";启用:" + (isUserSettingEnable() ? "1" : "0")+";"
                +StringUtil.N+isChangeed
                +StringUtil.N +"变化："+ StringUtil.getByteString(changedBinary)
                +StringUtil.N + "发送："+StringUtil.getByteString(allsend)
                +StringUtil.N + "120out:"+StringUtil.getByteString(mUSBSerializ.getIOOutBinary40_120())
                +StringUtil.N + "120set:"+StringUtil.getByteString(byteOut40_120)
        ));
        if (!isUserSettingSenserEnable()) return;

        callBack(type);
    }

    @Override
    public void onUnused() {
        super.onUnused();
        mBinaryInNew = null;
        mBinaryInOld = null;
        changedBinary = null;
    }

    private void callBack(int type) {
        if (this.getmLockDeviceCapacity() != null) {
            if ((this.light_lock_time - new Date().getTime()) >= 0) {
                addDeviceStatus(new RunningStatus(RunningStatus.Running, "因为" + getSelfTypeName() + "产生驱动,但是被" + this.getmLockDeviceCapacity().getSelfTypeName() + "锁闭，放弃"));
                return;
            } else {
                unLockNull();
            }
        }

        if (type == USBcommunicationKeys.FT_40IO) {
            mUSBSerializParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_Set40IO , allsend);
        } else {
            mUSBSerializParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_Set120IO, allsend);
        }

        if (mDeviceCapacityBase != null) {
            try {
                mDeviceCapacityBase.doChangeStatus(mUSBSerializParameter);
            } catch (FaceException e) {
                e.printStackTrace();
                addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "因为" + getSelfTypeName() + "产生驱动，出错" + e.getMessage()));
            }
        }

//        try {
//            mScene.doChangeStatus(sceneParameter);
//        } catch (FaceException e) {
//            e.printStackTrace();
//            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "因为" + getSelfTypeName() + "产生驱动，出错" + e.getMessage()));
//        }
    }


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Add_Pluginy_IOIn;
    }


    @Override
    public boolean testHardware_SDK() throws FaceException {
        try {
            UsbManager mUsbManager = (UsbManager) SuApplication.getInstance().getSystemService(Context.USB_SERVICE);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Face_UnsolvedForDlp
    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3 = super.doChangeStatus(t);
        if (mDeviceCapacityOutResult_3 != null) return mDeviceCapacityOutResult_3;
        if (!(t instanceof PluginIOInSensorParameter)) throw new FaceException("参数类型出错");
        final PluginIOInSensorParameter tt = (PluginIOInSensorParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.getType() == PluginIOInSensorParameter.TypeGetStatus) {
//            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "当前：" + mBinaryInNew);
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "最后输入：" + StringUtil.getByteString(mBinaryInNew));
        }else if (tt.getType() == PluginIOInSensorParameter.Type_GetDeviceOutBytes) {
            if (mDeviceCapacityBase == null) {
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_ErrInfo,"设备未插入");
                return commonDeviceCapacityOutResult;
            }
            USBSerializ mUSBSerializ = (USBSerializ) mDeviceCapacityBase;
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "当前out：" + StringUtil.getByteString(mUSBSerializ.getIOOutBinary40_120()));
        }
        return commonDeviceCapacityOutResult;
    }

    @Override
    public boolean isShowStatus() {
        return true;
    }

    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.PluginIOInSensor_his_max;
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
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_add_ioin_sensor_ac, false);
    }


    @Face_UnsolvedForDlp
    public static class PluginIOInSensorParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int TypeGetStatus = 1;
        public static final int Type_GetDeviceOutBytes=2;

        public PluginIOInSensorParameter(int type) {
            super(type);
        }
    }

}
