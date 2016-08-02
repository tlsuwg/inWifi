package com.hxuehh.rebirth.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.rebirth.push.MiPush.MiPushMainActivity;
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

            case flag_device_mi_test: {
                in = new Intent(thisCon, MiPushMainActivity.class);
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


    }
}
