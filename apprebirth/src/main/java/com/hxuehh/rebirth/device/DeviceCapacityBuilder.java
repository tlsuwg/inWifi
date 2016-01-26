package com.hxuehh.rebirth.device;

import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
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
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suwg on 2015/8/15.
 */

//建造者模式
public class DeviceCapacityBuilder {

    private static DeviceCapacityBuilder instance = null;

    private static synchronized void syncInit() {
        instance = new DeviceCapacityBuilder();
    }

    public static DeviceCapacityBuilder getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private DeviceCapacityBuilder() {
    }

    private List<DeviceCapacityBase> mDeviceCapacitysAll;//全部的
    private List<DeviceCapacityBase> mDeviceCapacitysUsed;//现在使用的
    private List<DeviceCapacityBase> mDeviceCapacitysSupport;//支持的
    private Map<Integer, DeviceCapacityBase> mapCapacitys;//查找

    public void findHardware_SDK(final FaceCommCallBack faceCommCallBack) {
        ThreadManager.getInstance().submitUIThread(new Runnable() {
            @Override
            public void run() {
                testHardware_SDK(faceCommCallBack);
            }
        });
    }

    public void putDeviceCapacity(DeviceCapacityBase mDeviceCapacity) {
        if (mapCapacitys == null)
            mapCapacitys = new HashMap();
        mapCapacitys.put(mDeviceCapacity.getType(), mDeviceCapacity);
    }


    @Face_UnsolvedForDlp
//    这个是客户端设计支持的
//    开发
    public List<DeviceCapacityBase> getAllByRebirth() {
        if (mDeviceCapacitysAll == null) mDeviceCapacitysAll = new ArrayList();

        USBGPIO mUSBGPIO = null;
        if (mapCapacitys != null && mapCapacitys.containsKey(DeviceCapacityBase.Type_USB_GPIO)) {
            mUSBGPIO = (USBGPIO) mapCapacitys.get(DeviceCapacityBase.Type_USB_GPIO);
        } else {
            mUSBGPIO = new USBGPIO();
        }

        USBSerializ mUSBSerializ = null;
        if (mapCapacitys != null && mapCapacitys.containsKey(DeviceCapacityBase.Type_USB_Serializ)) {
            mUSBSerializ = (USBSerializ) mapCapacitys.get(DeviceCapacityBase.Type_USB_Serializ);
        } else {
            mUSBSerializ = new USBSerializ();
        }


        LockScreen mLockScreen = new LockScreen();
        Screen mScreen = new Screen(mLockScreen);

        VoiceSynthesisPlay voiceSynthesisPlay = new VoiceSynthesisPlay();
        SUMediaPlayer mSUMediaPlayer = new SUMediaPlayer();
        Battery mBattery = new Battery();
        USBLinkSenser mUSBLink = new USBLinkSenser();
        CameraFlashLight mCameraFlashLight=  new CameraFlashLight();

        Scene mScene = new Scene(mScreen, mUSBSerializ, mUSBGPIO,mCameraFlashLight,voiceSynthesisPlay);//场景  后面是需要的（动作和判断）
        LightSensor mLightSensor=  new LightSensor(mScene);
        AccelerationSensor mAccelerationSensor=new AccelerationSensor(mScene);

        PluginInfrareBodySensor mInfrareBodySensor=new PluginInfrareBodySensor(mScene);//红外传感
        PluginHumiditySensor  mPluginHumiditySensor=new PluginHumiditySensor(mScene);//湿度
        PluginTemperatureSensor mPluginTemperatureSensor=new PluginTemperatureSensor(mScene);//温度
        PluginIOInSensor mPluginIOInSensor=new PluginIOInSensor(mScene);//IO注入

        mUSBSerializ.setProductionSensor(mInfrareBodySensor,mPluginHumiditySensor,mPluginTemperatureSensor,mPluginIOInSensor);//设置可以生产的传感


        Vibration mVibration=   new Vibration();
        Photograph mPhotograph=  new Photograph(mBattery, mUSBLink,mLightSensor,mScreen);

//        需要添加传感，锁定动作互斥
        DeviceCapacityBase[] needAddSensors=new DeviceCapacityBase[]{mVibration,mUSBGPIO,mScreen,mUSBSerializ,voiceSynthesisPlay,mSUMediaPlayer,mPhotograph,mCameraFlashLight};
       for(DeviceCapacityBase mdd:needAddSensors){
           mdd.setNeedLockSensors(mLightSensor, mAccelerationSensor);
       }


        mDeviceCapacitysAll.add(mScene);
        mDeviceCapacitysAll.add(mUSBGPIO);

        mDeviceCapacitysAll.add(mUSBSerializ);
        mDeviceCapacitysAll.add(mInfrareBodySensor);
        mDeviceCapacitysAll.add(mPluginHumiditySensor);
        mDeviceCapacitysAll.add(mPluginTemperatureSensor);
        mDeviceCapacitysAll.add(mPluginIOInSensor);

        mDeviceCapacitysAll.add(voiceSynthesisPlay);
        mDeviceCapacitysAll.add(new VoiceRecognition(mUSBSerializ, voiceSynthesisPlay));//这个是特殊的
        mDeviceCapacitysAll.add(mLockScreen);
        mDeviceCapacitysAll.add(mScreen);
        mDeviceCapacitysAll.add(mSUMediaPlayer);
        mDeviceCapacitysAll.add(new SuVideoSense(mBattery, mUSBLink,mLightSensor,mScreen));
        mDeviceCapacitysAll.add(new Volume());
        mDeviceCapacitysAll.add(mPhotograph);
        mDeviceCapacitysAll.add(new Brightness(mBattery, mUSBLink,mScreen));
        mDeviceCapacitysAll.add(mLightSensor);
        mDeviceCapacitysAll.add(mVibration);
        mDeviceCapacitysAll.add(mCameraFlashLight);
        mDeviceCapacitysAll.add(new Storage());
        mDeviceCapacitysAll.add(new Memory());
        mDeviceCapacitysAll.add(new Record());
        mDeviceCapacitysAll.add(new HeadsetSenser(mSUMediaPlayer));
        mDeviceCapacitysAll.add(mBattery);
        mDeviceCapacitysAll.add(new Temperature());
        mDeviceCapacitysAll.add(new Humidity());
        mDeviceCapacitysAll.add(mUSBLink);
        mDeviceCapacitysAll.add(mAccelerationSensor);
        mDeviceCapacitysAll.add(new P2PPort());
        mDeviceCapacitysAll.add(new Loc_Geographic());


        return mDeviceCapacitysAll;
    }


    public DeviceCapacityBase getDeviceCapacityByKey(int kk) {
        if (mapCapacitys != null) return mapCapacitys.get(kk);
        return null;
    }

    public List<DeviceCapacityBase> getmDeviceCapacitysSupport() {
        return mDeviceCapacitysSupport;
    }//支持的

    public List<DeviceCapacityBase> getmDeviceCapacitysUsed() {
        return mDeviceCapacitysUsed;
    }//在使用的


    //    硬件支持
    private void testHardware_SDK(FaceCommCallBack faceCommCallBack) {
        if (!this.isInitOK) {
            if (mDeviceCapacitysUsed == null) {
                if (mDeviceCapacitysAll == null) getAllByRebirth();
                mDeviceCapacitysUsed = new ArrayList();
                mDeviceCapacitysSupport = new ArrayList<DeviceCapacityBase>();
                mapCapacitys = new HashMap();
                for (DeviceCapacityBase mDeviceCapacity : mDeviceCapacitysAll) {
                    boolean istestHardware_SDK = false;
                    try {
                        istestHardware_SDK = mDeviceCapacity.testHardware_SDK();
                    } catch (Exception e) {
                        continue;
                    }
                    if (istestHardware_SDK) {
                        mDeviceCapacitysSupport.add(mDeviceCapacity);
                        mapCapacitys.put(mDeviceCapacity.getType(), mDeviceCapacity);
                        mDeviceCapacity.getUserSettingEnable();
                        mDeviceCapacity.getUserSettingSenserEnable();
                        try {
                            mDeviceCapacity.onCreat();
                            if (mDeviceCapacity.isUserSettingEnable() && mDeviceCapacity.isDevEnable()) {
                                mDeviceCapacitysUsed.add(mDeviceCapacity);
                            }
                        } catch (FaceException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            setInitOK(true);
        }

        if (faceCommCallBack != null)
            faceCommCallBack.callBack(mDeviceCapacitysSupport);
    }

    boolean isInitOK;

    public boolean isInitOK() {
        return isInitOK;
    }

    public void setInitOK(boolean isInitOK) {
        this.isInitOK = isInitOK;
    }


    public void onDestroy() {
        if(mDeviceCapacitysAll!=null){
            for(DeviceCapacityBase ba:mDeviceCapacitysAll){
                if(ba!=null)
                ba.onDestry();
            }
        }
    }
}
