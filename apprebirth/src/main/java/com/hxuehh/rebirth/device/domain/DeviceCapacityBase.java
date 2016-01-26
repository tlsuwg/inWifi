package com.hxuehh.rebirth.device.domain;

import android.app.Activity;
import android.graphics.Color;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.TypeIDable;
import com.hxuehh.rebirth.R;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.AdapterViewOnItemClickListener;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.InViewGroupWithAdaper;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.capacity.P2PPort.P2PPort;
import com.hxuehh.rebirth.capacity.USBLinkSenser.USBLinkSenser;
import com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO.USBGPIO;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.capacity.accelerationSensor.AccelerationSensor;
import com.hxuehh.rebirth.capacity.battery.Battery;
import com.hxuehh.rebirth.capacity.brightness.Brightness;
import com.hxuehh.rebirth.capacity.cameraFlashlight.CameraFlashLight;
import com.hxuehh.rebirth.capacity.headsetSenser.HeadsetSenser;
import com.hxuehh.rebirth.capacity.humidity.Humidity;
import com.hxuehh.rebirth.capacity.pluginHumiditySensor.PluginHumiditySensor;
import com.hxuehh.rebirth.capacity.pluginIOInSensor.PluginIOInSensor;
import com.hxuehh.rebirth.capacity.pluginInfrareBodySensor.PluginInfrareBodySensor;
import com.hxuehh.rebirth.capacity.lightSensor.LightSensor;
import com.hxuehh.rebirth.capacity.location.Loc_Geographic;
import com.hxuehh.rebirth.capacity.mediaPlayer.SUMediaPlayer;
import com.hxuehh.rebirth.capacity.memory.Memory;
import com.hxuehh.rebirth.capacity.photograph.Photograph;
import com.hxuehh.rebirth.capacity.pluginTemperatureSensor.PluginTemperatureSensor;
import com.hxuehh.rebirth.capacity.record.Record;
import com.hxuehh.rebirth.capacity.scene.Scene;
import com.hxuehh.rebirth.capacity.screen.LockScreen;
import com.hxuehh.rebirth.capacity.screen.Screen;
import com.hxuehh.rebirth.capacity.storage.Storage;
import com.hxuehh.rebirth.capacity.temperature.Temperature;
import com.hxuehh.rebirth.capacity.vibration.Vibration;
import com.hxuehh.rebirth.capacity.video.SuVideoSense;
import com.hxuehh.rebirth.capacity.voiceRecognition.VoiceRecognition;
import com.hxuehh.rebirth.capacity.voiceSynthesis.VoiceSynthesisPlay;
import com.hxuehh.rebirth.capacity.volume.Volume;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.device.service.DeviceService_TCPLongLink_;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.SerializeUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by suwg on 2015/8/15.
 */

//功能列表

public abstract class DeviceCapacityBase<F> implements Serializable, TypeIDable, InViewGroupWithAdaper, AdapterViewOnItemClickListener {

    public static final int Type_USB_GPIO = 1;//USB GP设备
    public static final int Type_Loc_Geographic = 2;//地理位置采集
    public static final int Type_Screen = 3;//屏幕展示
    public static final int Type_MediaPlayer = 4;//语音播放
    public static final int Type_RecordFile = 5;//采集声音
    public static final int Type_Photograph = 6;//拍照
    @Deprecated
    public static final int Type_VideoFile = 7;//录像 合并到11
    public static final int Type_P2P_Sound_Chat = 8;//在线聊天
    public static final int Type_P2P_Video_Chat = 9;//在线视频聊天
    @Deprecated
    public static final int Type_Video_Surveillance_File = 10;//本地监控  合并到11
    public static final int Type_Video_Surveillance_Sense = 11;//录像 ，本地移动侦测
    @Deprecated
    public static final int Type_Loc_Geographic_auto = 12;//位置变化采集
    public static final int Type_FlashLight = 13;//闪光灯
    public static final int Type_Vibration = 14;//震动
    public static final int Type_USB_Serializ = 15;//USB拓展设备

    public static final int Type_Temperature = 16;//温度
    public static final int Type_humidity = 17;//湿度
    public static final int Type_Battery = 18;//电池
    public static final int Type_Brightness = 19;//屏幕亮度
    public static final int Type_Volume = 20;//声音大小


    public static final int Type_storage = 21;//存储
    public static final int Type_memory = 22;//内存
    public static final int Type_USB_Link = 23;//插拔
    public static final int Type_LightSensor = 24;//光感
    public static final int Type_Headset = 25;//耳机
    //
    public static final int Type_P2PForPort = 26;//穿透能力
    public static final int Type_VoiceRecognition = 27;//语音识别
    public static final int Type_VoiceSynthesis = 28;//语音合成
    public static final int Type_AccelerationSensor = 29;//加速度
    public static final int Type_Scene = 30;//场景

    //
    public static final int Type_LockScreen = 31;//锁屏
    public static final int Type_Add_PluginInfraredBody = 32;//人体红外探测
    public static final int Type_Add_Pluginy_Temperature = 33;//变化
    public static final int Type_Add_Pluginy_Humidity = 34;//变化
    public static final int Type_Add_Pluginy_IOIn = 35;//IO变化


//    //
//    public static final int Type_LockScreen = 31;//锁屏
//    public static final int Type_Add_PluginInfraredBody = 32;//人体红外探测
//    public static final int Type_Add_Pluginy_Temperature  = 33;//变化
//    public static final int Type_Add_Pluginy_Humidity  = 34;//变化
//    public static final int Type_Add_IOIn  = 35;//IO变化


    public static final int Type_Client_AccelerationSensor = 1000;

    static final String[] allName = new String[]{
//            1-5
            "USB GPIO设备", "地理位置反馈上报", "屏幕展示", "语音播放", "录音",
//            6-10
            "拍照", "录像", "时时语音", "时时视频", "本地监控",
//            11-15
            "本地移动侦测，录像", "位置变化采集", "闪光灯", "震动", "USB拓展设备",
//            16-20
            "温度", "湿度", "电池", "屏幕亮度", "声音大小",
//            21=25
            "存储及文件", "内存占用", "USB插拔", "光感", "耳机插拔",
//            26-30
            "P2P基础能力", "语音识别", "语音合成", "加速度", "场景",
//            31=35
            "锁屏", "（USB插件）人体红外", "(USB插件)温度传感", "(USB插件)湿度传感", "(USB插件)输入",
//          "下载" ,

            "未知设备"
    };

    public static String USBLinkAction = "com.su.DeviceCapacityBase.usb.linkUSB.key.";
    public static String USBLinkLog = "com.su.DeviceCapacityBase.usb.linkUSB.log";
    public static String USBLinkDateChange = "com.su.DeviceCapacityBase.usb.linkUSB.change";


    public static Class<?> getClass(int type) {
        switch (type) {
//            "USB GPIO设备", "地理位置反馈上报", "屏幕展示", "语音播放", "录音",
            case 1:
                return USBGPIO.class;
            case 2:
                return Loc_Geographic.class;
            case 3:
                return Screen.class;
            case 4:
                return SUMediaPlayer.class;
            case 5:
                return Record.class;
//            "拍照", "录像", "时时语音", "时时视频", "本地监控",
            case 6:
                return Photograph.class;
            case 7:
                return null;
            case 8:
                return USBSerializ.class;
            case 9:
                return USBSerializ.class;
            case 10:
                return null;
//            "本地移动侦测", "位置变化采集", "闪光灯", "震动", "USB拓展设备",
            case 11:
                return SuVideoSense.class;
            case 12:
                return null;
            case 13:
                return CameraFlashLight.class;
            case 14:
                return Vibration.class;
            case 15:
                return USBSerializ.class;
//           "温度", "湿度", "电池", "屏幕亮度", "声音大小",
            case 16:
                return Temperature.class;
            case 17:
                return Humidity.class;
            case 18:
                return Battery.class;
            case 19:
                return Brightness.class;
            case 20:
                return Volume.class;
//            "存储空间", "内存占用","USB插拔","光感",
            case 21:
                return Storage.class;
            case 22:
                return Memory.class;
            case 23:
                return USBLinkSenser.class;
            case 24:
                return LightSensor.class;
            case 25:
                return HeadsetSenser.class;
            case 26:
                return P2PPort.class;
            case 27:
                return VoiceRecognition.class;
            case 28:
                return VoiceSynthesisPlay.class;
            case 29:
                return AccelerationSensor.class;
            case 30:
                return Scene.class;
            case 31:
                return LockScreen.class;
            case 32:
                return PluginInfrareBodySensor.class;
            case 33:
                return PluginTemperatureSensor.class;
            case 34:
                return PluginHumiditySensor.class;
            case 35:
                return PluginIOInSensor.class;

        }
        return null;
    }


    public boolean isShowInClient() {
        return true;
    }


    public static String getTypeNameByTypeID(int type) {
        int kk = type - 1;
        if (kk >= 0 && kk < allName.length) {
            return allName[kk];
        } else {
            switch (type) {
                case Type_Client_AccelerationSensor:
                    return "客户端加速度";
            }
            return allName[allName.length - 1];
        }
    }


    public String getSelfTypeName() {
        int kk = getType() - 1;
        if (kk >= 0 && kk < allName.length) {
            return allName[kk];
        } else {
            switch (getType()) {
                case Type_Client_AccelerationSensor:
                    return "客户端加速度";
            }
            return allName[allName.length - 1];
        }
    }


    //==============================================================
    public DeviceCapacityBase() {
        setType(getType());
    }

    protected int type;

    public abstract int getType();

    public final void setType(int type) {
        this.type = type;
    }

    transient List<RunningStatus> his = new ArrayList<RunningStatus>();

    public void addDeviceStatus(RunningStatus mDeviceStatus) {
        int size = getMAXHistorySize();
        if (size < 1) size = 1;
        if (his.size() > size) {
            his.remove(0);
        }
        his.add(mDeviceStatus);
    }

    public RunningStatus getLastDeviceStatus() {
        if (his.size() == 0) {
            return null;
        } else {
            return his.get(his.size() - 1);
        }
    }

    public  void clearHistoryDeviceStatus(){
        his.clear();
    }

    //获取最后状态
    public List<RunningStatus> getHistoryDeviceStatus() {
        return his;
    }


    //获取全部历史
    public abstract boolean testHardware_SDK() throws FaceException;//检测能力




    String getKey() {
        return DeviceInfo.getInstens().getSU_UUID() + this.getType();
    }


//    =====================================================================
    protected boolean isUserSettingEnable;
    public void getUserSettingEnable() {
        isUserSettingEnable = !"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.DeviceCapacityBaseUserSettingEnable + getKey()));
    }

    public void setUserSettingEnable(boolean is) {
        isUserSettingEnable = is;
        if (is) {
            SharedPreferencesUtils.putString(SharedPreferencesKeys.DeviceCapacityBaseUserSettingEnable + getKey(), "1");
        } else {
            SharedPreferencesUtils.putString(SharedPreferencesKeys.DeviceCapacityBaseUserSettingEnable + getKey(), "0");
        }
    }
    public boolean isUserSettingEnable() {
        return isUserSettingEnable;
    }
    //    =====================================================================

    //    =====================================================================
    protected boolean isUserSettingSenserEnable;
    public void getUserSettingSenserEnable() {
        isUserSettingSenserEnable = !"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.DeviceCapacityBaseUserSettingSenseEnable + getKey()));
    }

    public void setUserSettingSenserEnable(boolean is) {
        isUserSettingSenserEnable = is;
        if (is) {
            SharedPreferencesUtils.putString(SharedPreferencesKeys.DeviceCapacityBaseUserSettingSenseEnable + getKey(), "1");
        } else {
            SharedPreferencesUtils.putString(SharedPreferencesKeys.DeviceCapacityBaseUserSettingSenseEnable + getKey(), "0");
        }
    }
    public boolean isUserSettingSenserEnable() {
        return isUserSettingSenserEnable;
    }
    //    =====================================================================

//    开发设置  限制USB链接



    public abstract boolean isDevEnable();//是不是可用  设置可以使用

    public abstract String getDevUnEnableInfo();//这个是不能使用的原因

    //    =====================================================================

    public abstract int getMAXHistorySize();//设置容量

    public abstract void activeReportOfEvent(F... f) throws FaceException;//主动上报


    public abstract void onCreat() throws FaceException;//初始化能力

    public abstract void stop();//

    public abstract void onDestry();//

    public abstract boolean isShowStatus();//

    private transient FaceLoadCallBack mFaceLoadCallBack;

    protected boolean iSActiveReportOfEvent() {
        //是不是主动上报
        if (mFaceLoadCallBack == null)
            return false;
        return true;
    }

    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        if (t == null) {
            throw new FaceException("没有设置参数");
        }
        setmLastDeviceCapacityInParameter(t);
        switch (t.getType()) {
            case DeviceCapacityInParameter.Type_ShowHis: {
                if (his == null || his.size() == 0) throw new FaceException("没有历史");
                CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, his);
                return commonDeviceCapacityOutResult;
            }
            case DeviceCapacityInParameter.Type_ShowLastHis: {
                RunningStatus mRunningStatus = getLastDeviceStatus();
                if (mRunningStatus == null) throw new FaceException("没有历史");
                CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, mRunningStatus);
                return commonDeviceCapacityOutResult;
            }

            case DeviceCapacityInParameter.Type_ClearHis: {
               clearHistoryDeviceStatus();
                CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "清除完成");
                return commonDeviceCapacityOutResult;
            }


            case DeviceCapacityInParameter.Type_SetDeviceCapacityBaseSenserUsed_0: {
                CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
                setUserSettingSenserEnable(false);
                return commonDeviceCapacityOutResult;
            }
            case DeviceCapacityInParameter.Type_SetDeviceCapacityBaseSenserUsed_1: {
                CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
                setUserSettingSenserEnable(true);
                return commonDeviceCapacityOutResult;
            }

        }
        return null;
    }

    ;


    @Override
    public View getView(Activity mContext, int i, View view, ViewGroup viewGroup, LayoutInflater inflater, int viewItemKey, Object[] params) throws Exception {
        View view1 = inflater.inflate(R.layout.layout_device_capacity_item, null);
        TextView textView = (TextView) view1.findViewById(R.id.device_capacity_name);
        TextView device_capacity_info = (TextView) view1.findViewById(R.id.device_capacity_info);
        textView.setText(this.getSelfTypeName());
        if (!this.isDevEnable() || !this.isUserSettingEnable()) {
            view1.setBackgroundColor(Color.parseColor("#CDC5BF"));
        }

        if (!isDevEnable()) {
            device_capacity_info.setVisibility(View.VISIBLE);
            device_capacity_info.setText("" + getDevUnEnableInfo());
        } else if (!this.isUserSettingEnable()) {
            device_capacity_info.setVisibility(View.VISIBLE);
            device_capacity_info.setText("禁止使用");
        }
        return view1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }


    public boolean isHasTrueWorkerOnDuty() {//是不是存在真正的操作对象，比如GPIO USB等，存在，状态也要对
        return true;
    }

    ;


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        if (!isDevEnable()) {
            throw new FaceException(getDevUnEnableInfo());
        }
        if (!isUserSettingEnable) {
            throw new FaceException("已经设置不可用");
        }
        List list = new ArrayList();
        list.add(t[0]);
        list.add(this);
        BytesClassAidl mBytesClassAidl = new BytesClassAidl(AidlCacheKeys.Provisionality, BytesClassAidl.To_Me, (Serializable) list);
        SuApplication.getInstance().putAidlValue(mBytesClassAidl);
    }


    //
    public void waitThis() throws InterruptedException {
        synchronized (DeviceCapacityBase.this) {
            DeviceCapacityBase.this.wait();
        }
    }

    public void notifyThis() {
        synchronized (DeviceCapacityBase.this) {
            DeviceCapacityBase.this.notify();
        }
    }

//================================================

    protected DeviceService_TCPLongLink_ getLinkShow() {
        DeviceService_TCPLongLink_ mDeviceService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_DeviceService();
        if (mDeviceService_TCPLongLink_ == null) {
            return null;
        }

        if (mDeviceService_TCPLongLink_.getRunStatus()[2] != null) {
            return null;
        }

        return mDeviceService_TCPLongLink_;
    }


    public void notifyStatusChange() {
        DeviceService_TCPLongLink_ mDeviceService_TCPLongLink_ = getLinkShow();
        if (mDeviceService_TCPLongLink_ == null) return;
        MidMessageOrder_2 mMidMessageOrder_2 = new MidMessageOrder_2(MidMessageCMDKeys.MidMessageCMD_Client_DeviceCapacity_Change);
        mMidMessageOrder_2.putKeyValue(MidMessage.Key_DeviceToClientNotification_level, MidMessage.Key_DeviceToClientNotification_level_1);
        mMidMessageOrder_2.putKeyValue(MidMessage.Key_DeviceID, DeviceInfo.getInstens().getSU_UUID());
        mMidMessageOrder_2.setBytes(SerializeUtil.serialize(this));
        try {
            mDeviceService_TCPLongLink_.sendSyncAddCache(mMidMessageOrder_2);
        } catch (FaceException e) {
            e.printStackTrace();
        }
    }

//================================================

//    transient static DeviceCapacityInParameter mNowDeviceCapacityInParameter, mLastDeviceCapacityInParameter;
//public static DeviceCapacityInParameter getContext_LastDeviceCapacityInParameter() {
//    return mLastDeviceCapacityInParameter;
//}

    transient DeviceCapacityInParameter thismNowDeviceCapacityInParameter, thismLastDeviceCapacityInParameter;


    public DeviceCapacityInParameter get_this_LastDeviceCapacityInParameter() {
        return thismLastDeviceCapacityInParameter;
    }

    public boolean setmLastDeviceCapacityInParameter(DeviceCapacityInParameter mNowDeviceCapacityInParameter) throws FaceException {

        if (thismNowDeviceCapacityInParameter != null &&
                this.thismNowDeviceCapacityInParameter.getType() == mNowDeviceCapacityInParameter.getType()
                && this.thismNowDeviceCapacityInParameter.getCmdLevel() > mNowDeviceCapacityInParameter.getCmdLevel()) {

            throw new FaceException("正在执行用户指令，传感器指令失效", FaceException.sensorErrType_SameCMD);
        }

//        this.mLastDeviceCapacityInParameter = this.mNowDeviceCapacityInParameter;
//        this.mNowDeviceCapacityInParameter = mNowDeviceCapacityInParameter;

        this.thismLastDeviceCapacityInParameter = thismNowDeviceCapacityInParameter;
        this.thismNowDeviceCapacityInParameter = mNowDeviceCapacityInParameter;

        return true;
    }

    public long getLastTagTime() {
        return thismNowDeviceCapacityInParameter == null ? -1 : thismNowDeviceCapacityInParameter.getTagTime();
    }


//======================================================锁闭区域  动作影响

    protected transient DeviceCapacityBase mLockDeviceCapacity;
    protected long light_lock_time = -1;

    public DeviceCapacityBase getmLockDeviceCapacity() {
        return mLockDeviceCapacity;
    }

    public void lockByAllAlong(DeviceCapacityBase mLockDeviceCapacity) {
        this.mLockDeviceCapacity = mLockDeviceCapacity;
        this.light_lock_time = new Date().getTime() + Long.MAX_VALUE;
    }

    public void unLockNull() {
        this.mLockDeviceCapacity = null;
        light_lock_time = -1;
    }

    public void unLock100() {
        light_lock_time = new Date().getTime() + AppStaticSetting.UnLcokLastTime;
    }


    public void lockByInTime(DeviceCapacityBase mDeviceCapacity, long light_lock_time) {
        this.mLockDeviceCapacity = mDeviceCapacity;
        this.light_lock_time = new Date().getTime() + light_lock_time;
    }


    //    =============================================传感器 可以锁闭的
    private transient LightSensor mLightSensorLock;
    protected transient AccelerationSensor mAccelerationSensorLock;

    public void setNeedLockSensors(LightSensor mLightSensor, AccelerationSensor mAccelerationSensor) {
        this.mLightSensorLock = mLightSensor;
        this.mAccelerationSensorLock = mAccelerationSensor;
    }

    protected void mAccelerationSensorUnLock100() {
        if (mAccelerationSensorLock != null) {
            if (mAccelerationSensorLock.getmLockDeviceCapacity() == this) {
                mAccelerationSensorLock.unLock100();
            }
        }
    }

    protected void mAccelerationSensorLockLong() {
        if (mAccelerationSensorLock != null) {
            if (mAccelerationSensorLock != null) {
                mAccelerationSensorLock.lockByAllAlong(this);
            }
        }
    }

    protected void mLightSensorLockOneTimes() {
        if (mLightSensorLock != null) {
            mLightSensorLock.lockByInTime(this, AppStaticSetting.Light_Lock_Time);
        }
    }

    protected void mLightSensorLockLong() {
        if (mLightSensorLock != null) {
            mLightSensorLock.lockByAllAlong(this);
        }
    }

    protected void mLightSensorUnLock100() {
        if (mLightSensorLock != null) {
            if (mLightSensorLock.getmLockDeviceCapacity() == this) {
                mLightSensorLock.unLock100();
            }
        }
    }

}
