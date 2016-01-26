package com.hxuehh.rebirth.capacity.scene;

import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO.USBGPIO;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.capacity.cameraFlashlight.CameraFlashLight;
import com.hxuehh.rebirth.capacity.screen.Screen;
import com.hxuehh.rebirth.capacity.voiceSynthesis.VoiceSynthesisPlay;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
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
public class Scene extends DeviceCapacityBase {
    transient long TimeForSensorkeep = AppStaticSetting.SensorWakelockTime;
    transient  long TimeForScreenkeep = AppStaticSetting.ScreenWakelockTime;

    @Override
    public void onCreat() throws FaceException {
        long time = SharedPreferencesUtils.getLong(SharedPreferencesKeys.Scene_Keep_sensor_Time);
        if (time > 0) {
            TimeForSensorkeep = time;
        }
         time = SharedPreferencesUtils.getLong(SharedPreferencesKeys.Scene_Keep_screen_Time);
        if (time > 0) {
            TimeForScreenkeep = time;
        }

        for(DeviceCapacityInParameter mDeviceCapacityInParameter:mDeviceCapacityInParameters){
            mDeviceCapacityInParameter.setCmdLevel(DeviceCapacityInParameter.DeviceCapacityInParameter_SensorCmdLevel);
        }

    }

    public Scene(Screen mScreen, USBSerializ mUSBSerializ, USBGPIO mUSBGPIO, CameraFlashLight mCameraFlashLight, VoiceSynthesisPlay mVoiceSynthesis) {
        this.mScreen = mScreen;
        this.mUSBSerializ = mUSBSerializ;
        this.mUSBGPIO = mUSBGPIO;
        this.mVoiceSynthesis = mVoiceSynthesis;
        this.mCameraFlashLight=mCameraFlashLight;
    }

    transient VoiceSynthesisPlay mVoiceSynthesis;

    transient Screen mScreen;
    transient Screen.ScreenParameter screenParameterON = new Screen.ScreenParameter(Screen.ScreenParameter.TypeScreenON);
    transient Screen.ScreenParameter screenParameterOFF = new Screen.ScreenParameter(Screen.ScreenParameter.TypeScreenOff);

    transient USBSerializ mUSBSerializ;
    transient USBSerializ.USBSerializParameter USBSerializParameterON = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_ON);
    transient USBSerializ.USBSerializParameter USBSerializParameterOff = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_OFF);

    transient USBGPIO mUSBGPIO;
    transient USBGPIO.USBGPIOParameter USBGPIOParameterON = new USBGPIO.USBGPIOParameter(USBGPIO.USBGPIOParameter.Type_ON);
    transient USBGPIO.USBGPIOParameter USBGPIOParameterOff = new USBGPIO.USBGPIOParameter(USBGPIO.USBGPIOParameter.Type_OFF);


    transient CameraFlashLight mCameraFlashLight;
    transient CameraFlashLight.FlashlightParameter mFlashlightParameterOn=new CameraFlashLight.FlashlightParameter(CameraFlashLight.FlashlightParameter.Type_ON);
    transient CameraFlashLight.FlashlightParameter mFlashlightParameterOFF=new CameraFlashLight.FlashlightParameter(CameraFlashLight.FlashlightParameter.Type_Off);


    transient DeviceCapacityInParameter[] mDeviceCapacityInParameters=new DeviceCapacityInParameter[] {
        screenParameterON,screenParameterOFF,USBSerializParameterON,USBSerializParameterOff,USBGPIOParameterON,USBGPIOParameterOff,mFlashlightParameterOn
        ,mFlashlightParameterOFF
    };




    //    灯光反馈 只能执行一个 优先执行
    transient FaceCommCallBack faceCommCallBackForLight = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
             final int fromkey = (int) t[0];
            final String from = DeviceCapacityBase.getTypeNameByTypeID(fromkey);
            final long tag = new Date().getTime();

            boolean isHandle = false;
            try {
                isHandle = doOn(mUSBSerializ, USBSerializParameterON, tag, from+"驱动");
                if (!isHandle) {
                    isHandle = doOn(mUSBGPIO, USBGPIOParameterON, tag, from+"驱动");
                }
                if (!isHandle) {
                    isHandle = doOn(mCameraFlashLight, mFlashlightParameterOn, tag, from+"驱动");
                }
                if (!isHandle) {
                    isHandle = doOn(mScreen, screenParameterON, tag, from+"驱动");
                }
            } catch (FaceException e) {
//                e.printStackTrace();
            }

            if(isHandle) {
                mVoiceSynthesisPlay(from + "驱动");

                ThreadManager.getInstance().getNewThread("end USBSerializ by AccelerationSensor ", new Runnable() {
                    @Override
                    public void run() {
                        long time= TimeForSensorkeep;
                        if(fromkey==  DeviceCapacityBase.Type_AccelerationSensor){
                            time=TimeForScreenkeep;
                        }else{
                            time= TimeForSensorkeep;
                        }
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        boolean isHandleBck = false;
                        try {
                            isHandleBck = doOff(mUSBSerializ, USBSerializParameterOff,tag, from + "回滚");
                            if (!isHandleBck) {
                                isHandleBck = doOff(mUSBGPIO, USBGPIOParameterOff, tag,from + "回滚");
                            }
                            if (!isHandleBck) {
                                isHandleBck = doOff(mCameraFlashLight, mFlashlightParameterOFF, tag,from + "回滚");
                            }
                            if (!isHandleBck) {
                                isHandleBck = doOff(mScreen, screenParameterOFF, tag,from + "回滚");
                            }
                            if(isHandleBck)
                            mVoiceSynthesisPlay("回滚");
                        } catch (FaceException e) {
//                            e.printStackTrace();
                        }
                    }
                }).start();
            }


            return false;
        }
    };

    void mVoiceSynthesisPlay(String info) {
        try {
            if (!"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mVoiceSynthesis.getType()))) {
                mVoiceSynthesis.startOnce(info);
            }
        } catch (FaceException e) {
            e.printStackTrace();
        }
    }

    private boolean doOff(DeviceCapacityBase mDeviceCapacity, DeviceCapacityInParameter usbSerializParameterOff,long tag, String s) throws FaceException {

        if (!"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mDeviceCapacity.getType())))
            if (mDeviceCapacity.getLastTagTime()==tag
                    && mDeviceCapacity.isHasTrueWorkerOnDuty()
                    &&mDeviceCapacity.isUserSettingEnable()) {
                try {
                    mDeviceCapacity.doChangeStatus(usbSerializParameterOff);
                    return true;
                } catch (FaceException e) {
                    if (e.sensorErrType != 0) {
                        if (e.sensorErrType == FaceException.sensorErrType_SameCMD) {
                            String info = e.getMessage();
                            mVoiceSynthesisPlay(s + ";" + info);
                            throw new FaceException("");
                        }
                    }
                    e.printStackTrace();
                    addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, s + "运行出错by" + mDeviceCapacity.getSelfTypeName() + ";e=" + e.getMessage()));
                }
            }

        return false;

    }

    private boolean doOn(DeviceCapacityBase mDeviceCapacity, DeviceCapacityInParameter screenParameterON, long tag, String from) throws FaceException {
        try {
            if (!"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mDeviceCapacity.getType()))
                    && mDeviceCapacity.isHasTrueWorkerOnDuty()
                    &&mDeviceCapacity.isUserSettingEnable()
                    ) {
                screenParameterON.setTagTime(tag);
                mDeviceCapacity.doChangeStatus(screenParameterON);
                return true;
            }
        } catch (FaceException e) {
            e.printStackTrace();
            if (e.sensorErrType != 0) {
                if (e.sensorErrType == FaceException.sensorErrType_SameCMD) {
                    String info = e.getMessage();
                    mVoiceSynthesisPlay(from + ";" + info);
                    throw new FaceException("");

                }
            }

            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, from + "运行出错by" + mDeviceCapacity.getSelfTypeName() + ";e=" + e.getMessage()));
        }
        return false;
    }


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Scene;
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


    @Face_UnsolvedForDlp
    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3 = super.doChangeStatus(t);
        if (mDeviceCapacityOutResult_3 != null) return mDeviceCapacityOutResult_3;
        if (!(t instanceof SceneParameter)) throw new FaceException("参数类型出错");
        final SceneParameter tt = (SceneParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        switch (tt.getType()) {
            case SceneParameter.TypeCMDFrom: {
                doSensorType(tt);
            }
            break;
            case DeviceCapacityInParameter.Type_AskStatus:
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res,
                        " mScreen status" + (mScreen.getLastDeviceStatus() == null ? "null" : mScreen.getLastDeviceStatus().toStatusString())
                                + "     mUSBSerializ=" + (mUSBSerializ.getmUSBLinker() != null ? mUSBSerializ.getmUSBLinker().getStatusName() : "null")
                                + "     mUSBGPIO=" + (mUSBGPIO.getmUSBLinker() != null ? mUSBGPIO.getmUSBLinker().getStatusName() : "null")
                                + "     mCameraFlashLight status" + (mScreen.getLastDeviceStatus() == null ? "null" : mScreen.getLastDeviceStatus().toStatusString())
                                + StringUtil.N + "（能有）设置：" + StringUtil.N
                                + "Screen=" + SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mScreen.getType()) + StringUtil.N
                                + "USBSerializ=" + SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mUSBSerializ.getType()) + StringUtil.N
                                + "CameraFlashLight=" + SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mCameraFlashLight.getType()) + StringUtil.N
                                + "USBGPIO=" + SharedPreferencesUtils.getString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + mUSBGPIO.getType())
                                + StringUtil.N + "传感时间"+this. TimeForSensorkeep+"屏幕"+TimeForScreenkeep

                );
                break;
            case SceneParameter.TypeSetting_USED: {
                SharedPreferencesUtils.putString(SharedPreferencesKeys.Scene_DeviceCapacity_ID + tt.deviceCapacityID, tt.isUSED ? "1" : "0");
            }
            break;
            case SceneParameter.TypeSetting_SensorKeepTime: {
                if (tt.keepTime <= 0) throw new FaceException("必须大于0");
                SharedPreferencesUtils.putLong(SharedPreferencesKeys.Scene_Keep_sensor_Time, tt.keepTime);
                this.TimeForSensorkeep = tt.keepTime;
            }

            break;

            case SceneParameter.TypeSetting_ScreenKeepTime: {
                if (tt.keepTime <= 0) throw new FaceException("必须大于0");
                SharedPreferencesUtils.putLong(SharedPreferencesKeys.Scene_Keep_screen_Time, tt.keepTime);
                this.TimeForScreenkeep = tt.keepTime;
            }

            break;
        }
        return commonDeviceCapacityOutResult;
    }

    private void doSensorType(SceneParameter tt) {
        switch (tt.sensorType) {
            case DeviceCapacityBase.Type_Client_AccelerationSensor:
            case DeviceCapacityBase.Type_LightSensor:
            case DeviceCapacityBase.Type_Add_PluginInfraredBody:
            case DeviceCapacityBase.Type_AccelerationSensor: {
                callBackForLight(tt.sensorType);
            }
            break;
        }
    }

    @Override
    public boolean isShowStatus() {
        return true;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Scene_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    private void callBackForLight(int fromkey) {
        final String from = DeviceCapacityBase.getTypeNameByTypeID(fromkey);
        if (faceCommCallBackForLight != null) {
            addDeviceStatus(new RunningStatus(RunningStatus.Running, from + "驱动，动作"));
            faceCommCallBackForLight.callBack(fromkey);
        } else {
            addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, "执行" + from + " 动作,没有执行器"));
        }
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
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_ligth_scene_ac, false);
    }


    @Face_UnsolvedForDlp
    public static class SceneParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int TypeCMDFrom = 1;//

        public static final int TypeSetting_USED = 2;
        public static final int TypeSetting_SensorKeepTime = 3;
        public static final int TypeSetting_ScreenKeepTime = 4;


        int sensorType;//传感类似

        public SceneParameter(int type, int sensorType) {
            super(type);
            this.sensorType = sensorType;
        }


        public SceneParameter(int type) {
            super(type);
        }

        long keepTime;

        public void setKeepTime(long keepTime) {
            this.keepTime = keepTime;
        }

        int deviceCapacityID;//设备功能
        boolean isUSED;//能不能使用

        public SceneParameter(int type, int deviceCapacityID, boolean isUSED) {
            super(type);
            this.deviceCapacityID = deviceCapacityID;
            this.isUSED = isUSED;
        }

    }

}
