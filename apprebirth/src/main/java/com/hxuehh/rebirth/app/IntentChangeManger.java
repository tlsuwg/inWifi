package com.hxuehh.rebirth.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.rebirth.all.FaceAc.SelectDeviceTypeAc;
import com.hxuehh.rebirth.all.FaceAc.ServerDeviceServiceStatusAc;
import com.hxuehh.rebirth.capacity.OnTopActivity;
import com.hxuehh.rebirth.capacity.USBLinkSenser.USBLinkSenserClientAc;
import com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO.USBGPIOClientAc;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializClientAc;
import com.hxuehh.rebirth.capacity.accelerationSensor.AccelerationSensorClientAc;
import com.hxuehh.rebirth.capacity.battery.BatteryClientAc;
import com.hxuehh.rebirth.capacity.brightness.BrightnessClientAc;
import com.hxuehh.rebirth.capacity.cameraFlashlight.CameraFlashLightClientAc;
import com.hxuehh.rebirth.capacity.headsetSenser.HeadsetSenserClientAc;
import com.hxuehh.rebirth.capacity.humidity.HumidityClientAc;
import com.hxuehh.rebirth.capacity.pluginHumiditySensor.PluginHumiditySensorClientAc;
import com.hxuehh.rebirth.capacity.pluginIOInSensor.PluginIOInSensorClientAc;
import com.hxuehh.rebirth.capacity.pluginInfrareBodySensor.PluginInfrareBodySensorClientAc;
import com.hxuehh.rebirth.capacity.lightSensor.LightSensorClientAc;
import com.hxuehh.rebirth.capacity.location.Loc_GeographicClientAc;
import com.hxuehh.rebirth.capacity.mediaPlayer.SUMediaPlayerClientAc;
import com.hxuehh.rebirth.capacity.memory.MemoryClientAc;
import com.hxuehh.rebirth.capacity.photograph.PhotographClientAc;
import com.hxuehh.rebirth.capacity.pluginTemperatureSensor.PluginTemperatureSensorClientAc;
import com.hxuehh.rebirth.capacity.record.RecordClientAc;
import com.hxuehh.rebirth.capacity.scene.SceneClientAc;
import com.hxuehh.rebirth.capacity.screen.ScreenClientAc;
import com.hxuehh.rebirth.capacity.storage.StorageClientAc;
import com.hxuehh.rebirth.capacity.temperature.TemperatureClientAc;
import com.hxuehh.rebirth.capacity.vibration.VibrationClientAc;
import com.hxuehh.rebirth.capacity.video.SuVideoSenseClientAc;
import com.hxuehh.rebirth.capacity.voiceRecognition.VoiceRecognitionClientAc;
import com.hxuehh.rebirth.capacity.voiceSynthesis.VoiceSynthesisPlayClientAc;
import com.hxuehh.rebirth.capacity.volume.VolumeClientAc;
import com.hxuehh.rebirth.client.faceAc.Client_AccelerationSetting;
import com.hxuehh.rebirth.client.faceAc.Client_InitAc;
import com.hxuehh.rebirth.client.faceAc.Client_StatusAc;
import com.hxuehh.rebirth.client.faceAc.ShowDeviceCapacityAc;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.faceAc.AccessoryTestAc;
import com.hxuehh.rebirth.device.service.DeviceService_TCPLongLink_;
import com.hxuehh.rebirth.push.MiPush.MiPushMainActivity;
import com.hxuehh.rebirth.server.FaceAc.ServerOrDevice_InitAc;
import com.hxuehh.rebirth.server.Services.MainService_UDPreviceback_TCPLongLink;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.IntentKeys;

/**
 * Created by suwg on 2015/8/13.
 */
public class IntentChangeManger<T> {


    public static final int Main_Service_UDPreviceback_TCPLongLink = 10000;
    public static final int Device_Service_UDPreviceback_TCPLongLink = 10001;
    public static final int Client_Service_UDPreviceback_TCPLongLink = 10002;

    public static final int flag_device_used_type_noSet = 5;
    public static final int flag_device_only = 1;
    public static final int flag_server_Device = 2;
    public static final int flag_client_only = 3;
    public static final int flag_server_only = 4;


    public static final int flag_select_client_init = 14;//初始化
    public static final int flag_select_serverordevice_init = 15;
    public static final int flag_test_usb_dev = 16;

    public static final int flag_show_device_capacitys = 17;//展示远端设备能力
    public static final int flag_vibration_ac = 18;
    public static final int flag_screen_ac = 19;
    public static final int flag_flashlight_ac = 20;
    public static final int flag_loc_geographic_ac = 21;

    public static final int flag_usb_gpio_ac = 22;
    public static final int flag_temperature_ac = 23;
    public static final int flag_humidity_ac = 24;
    public static final int flag_photograph_ac = 25;
    public static final int flag_battery_ac = 26;
    public static final int flag_usb_link_ac = 27;
    public static final int flag_brightness_ac = 28;
    public static final int flag_volume_ac = 29;
    public static final int flag_storage_ac = 30;
    public static final int flag_memory_ac = 31;
    public static final int flag_record_ac=32;
    public static final int flag_ligth_sensor_ac=33;
    public static final int flag_SUMediaPlayer_ac=34;
    public static final int flag_headset_ac=35;
    public static final int flag_usb_serializ_ac=36;
    public static final int flag_voice_recognition_ac=37;
    public static final int flag_voice_synthesis_ac=38;
    public static final int flag_acceleration_sensor_ac=39;
    public static final int flag_ligth_scene_ac=40;
    public static final int flag_usb_GPIO_ac=41;
    public static final int flag_video_ac=42;
    public static final int flag_device_mi_test=43;
    public static final int flag_device_acceleration_setting=44;
    public static final int flag_infrare_body_sensor_ac=45;
    public static final int flag_add_temperature_sensor_ac=46;
    public static final int flag_add_humidity_sensor_ac=47;
    public static final int flag_add_ioin_sensor_ac=48;


    public static void jumpTo(Activity thisCon, int flag, boolean b) {
        Intent in = null;
        switch (flag) {
            case flag_device_used_type_noSet: {//设置身份
                in = new Intent(thisCon, SelectDeviceTypeAc.class);
            }
            break;
            case flag_server_Device:
            case flag_server_only:
            case flag_device_only: {
                in = new Intent(thisCon, ServerDeviceServiceStatusAc.class);
            }
            break;
            case flag_client_only: {
                in = new Intent(thisCon, Client_StatusAc.class);
            }
            break;
            case flag_select_client_init: {//初始化
                in = new Intent(thisCon, Client_InitAc.class);//作为客户端使用
            }
            break;
            case flag_select_serverordevice_init: {
                in = new Intent(thisCon, ServerOrDevice_InitAc.class);//作为服务器或者设备
            }
            break;
            case flag_test_usb_dev: {
                    in = new Intent(thisCon, AccessoryTestAc.class);//测试使用
            }
            break;
            case flag_show_device_capacitys: {
                in = new Intent(thisCon, ShowDeviceCapacityAc.class);
            }
            break;

            case flag_vibration_ac: {
                in = new Intent(thisCon, VibrationClientAc.class);
            }
            break;

            case flag_screen_ac: {
                in = new Intent(thisCon, ScreenClientAc.class);
            }
            break;

            case flag_flashlight_ac: {
                in = new Intent(thisCon, CameraFlashLightClientAc.class);
            }
            break;
            case flag_loc_geographic_ac: {
                in = new Intent(thisCon, Loc_GeographicClientAc.class);
            }
            break;
//            @Face_UnsolvedForDlp
            case flag_usb_gpio_ac: {
                in = new Intent(thisCon, Loc_GeographicClientAc.class);
            }
            break;
            case flag_temperature_ac: {
                in = new Intent(thisCon, TemperatureClientAc.class);
            }
            break;
            case flag_humidity_ac: {
                in = new Intent(thisCon, HumidityClientAc.class);
            }
            break;
            case flag_photograph_ac: {
                in = new Intent(thisCon, PhotographClientAc.class);
            }
            break;

            case flag_battery_ac: {
                in = new Intent(thisCon, BatteryClientAc.class);
            }
            break;
            case flag_usb_link_ac: {
                in = new Intent(thisCon, USBLinkSenserClientAc.class);
            }
            break;
            case flag_brightness_ac: {
                in = new Intent(thisCon, BrightnessClientAc.class);
            }
            break;
            case flag_volume_ac: {
                in = new Intent(thisCon, VolumeClientAc.class);
            }
            break;
            case flag_storage_ac: {
                in = new Intent(thisCon, StorageClientAc.class);
            }
            break;
            case flag_memory_ac: {
                in = new Intent(thisCon, MemoryClientAc.class);
            }
            break;
            case flag_record_ac: {
                in = new Intent(thisCon, RecordClientAc.class);
            }
            break;
            case flag_ligth_sensor_ac: {
                in = new Intent(thisCon, LightSensorClientAc.class);
            }
            break;
            case flag_SUMediaPlayer_ac: {
                in = new Intent(thisCon, SUMediaPlayerClientAc.class);
            }
            break;
            case flag_headset_ac: {
                in = new Intent(thisCon, HeadsetSenserClientAc.class);
            }
            break;
            case flag_usb_serializ_ac: {
                in = new Intent(thisCon, USBSerializClientAc.class);
            }
            break;
            case flag_voice_recognition_ac: {
                in = new Intent(thisCon, VoiceRecognitionClientAc.class);
            }
            break;
            case flag_voice_synthesis_ac: {
                in = new Intent(thisCon, VoiceSynthesisPlayClientAc.class);
            }
            break;
            case flag_acceleration_sensor_ac: {
                in = new Intent(thisCon, AccelerationSensorClientAc.class);
            }
            break;
            case flag_ligth_scene_ac: {
                in = new Intent(thisCon, SceneClientAc.class);
            }
            break;
            case flag_usb_GPIO_ac: {
                in = new Intent(thisCon, USBGPIOClientAc.class);
            }
            break;
            case flag_video_ac: {
                in = new Intent(thisCon, SuVideoSenseClientAc.class);
            }
            break;
            case flag_device_mi_test: {
                in = new Intent(thisCon, MiPushMainActivity.class);
            }
            break;
            case flag_device_acceleration_setting: {
                in = new Intent(thisCon,Client_AccelerationSetting.class);
            }
            break;

                       case flag_infrare_body_sensor_ac: {
                in = new Intent(thisCon,PluginInfrareBodySensorClientAc.class);
            }
            break;

            case flag_add_temperature_sensor_ac:{
                in = new Intent(thisCon,PluginTemperatureSensorClientAc.class);
            }
            break;

            case flag_add_humidity_sensor_ac:{
                in = new Intent(thisCon,PluginHumiditySensorClientAc.class);
            }
            break;

            case flag_add_ioin_sensor_ac:{
                in = new Intent(thisCon, PluginIOInSensorClientAc.class);
            }
            break;




        }
        if (in == null) return;
        thisCon.startActivity(in);
        if (b) thisCon.finish();
    }


    public static void jumpToForResult(Activity thisCon, int flag, int req) {
        Intent in = null;
        if (in == null) return;
        thisCon.startActivityForResult(in, req);
    }

    public static void startService(Context thisCon, int flag) {
        Intent in = null;
        switch (flag) {
            case Main_Service_UDPreviceback_TCPLongLink: {
                in = new Intent(thisCon, MainService_UDPreviceback_TCPLongLink.class);
            }
            break;
            case 0: {

            }
            break;
            default:
        }
        if (in == null) return;
        thisCon.startService(in);
    }

    public static void stopService(Context mContext, int flag) {

        Intent in = null;
        switch (flag) {
            case Main_Service_UDPreviceback_TCPLongLink: {
                in = new Intent(mContext, MainService_UDPreviceback_TCPLongLink.class);
            }
            break;
            case Device_Service_UDPreviceback_TCPLongLink: {
                in = new Intent(mContext, DeviceService_TCPLongLink_.class);
            }
            break;
            case Client_Service_UDPreviceback_TCPLongLink: {
                in = new Intent(mContext, ClientService_TCPLongLink_.class);
            }
            break;

            case 0: {

            }
            break;
            default:
        }
        if (in == null) return;
        mContext.stopService(in);
    }


    public static void bindServiceToApp(Context thisCon, int flag, BytesClass mClassKeyByte) {
        Intent in = null;
        switch (flag) {
            case Device_Service_UDPreviceback_TCPLongLink: {
                in = new Intent(SuApplication.getInstance(), DeviceService_TCPLongLink_.class);
                in.putExtra(IntentKeys.obj_ClassKeyByte, (java.io.Serializable) mClassKeyByte);
            }
            break;


            case Client_Service_UDPreviceback_TCPLongLink: {
                in = new Intent(SuApplication.getInstance(), ClientService_TCPLongLink_.class);
                in.putExtra(IntentKeys.obj_ClassKeyByte, (java.io.Serializable) mClassKeyByte);
            }
            break;

            case 0: {

            }
            break;
            default:
        }
        if (in == null) return;
        SuApplication.getInstance().bindSuService(in, flag);
    }


    public static void unbindServiceToApp(int flag) {
        Intent in = null;
        switch (flag) {
//            case Device_Service_UDPreviceback_TCPLongLink: {
//            }
//            break;
            default:
        }
        SuApplication.getInstance().unbindService(flag);
    }


//    ===============================================================================

    public static boolean isDevServiceStart() {
        try {
            return SuApplication.getInstance().getAidlValue(AidlCacheKeys.DeviceService_TCPLongLink__ISRunning) != null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isClientServiceStart() {
        try {
            return SuApplication.getInstance().getAidlValue(AidlCacheKeys.Client_Service_UDPreviceback_TCPLongLink) != null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isMainServiceStart() {
        try {
            return SuApplication.getInstance().getAidlValue(AidlCacheKeys.MainService_UDPreviceback_TCPLongLink_ISRunning) != null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isClient_Dev_Main() {
        return isDevServiceStart() && isMainServiceStart() && DevRunningTime.isCanDevServerClientInThis;
    }

    public static void openSingletonAc(int type) {
        switch (type) {
            case DeviceCapacityBase.Type_Brightness:
            case DeviceCapacityBase.Type_Video_Surveillance_Sense:
            case DeviceCapacityBase.Type_Photograph: {
                try {
                    SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.openSingletonAcForDeviceCap, BytesClassAidl.To_Me, new Integer(type)));
                    Intent in = new Intent(SuApplication.getInstance(), OnTopActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SuApplication.getInstance().startActivity(in);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            break;

        }
    }
}
