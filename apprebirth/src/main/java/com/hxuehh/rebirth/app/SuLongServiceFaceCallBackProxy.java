package com.hxuehh.rebirth.app;

import android.content.Context;
import android.content.Intent;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.all.FaceAc.ServerDeviceServiceStatusAc;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NotifiationUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.IntentKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

/**
 * Created by suwg on 2015/8/24.
 */

//耗时操作  最好放在这个里面
public class SuLongServiceFaceCallBackProxy implements FaceCommCallBack {

    public static final int NetChange = 2;
    Context mCon;

    public SuLongServiceFaceCallBackProxy() {

    }


    @Override
    public boolean callBack(Object... ts) {
        mCon= (Context) ts[0];;
        Intent in = (Intent) ts[1];
        BytesClass mBytesClass = (BytesClass) in.getSerializableExtra(IntentKeys.obj_ClassKeyByte);
        if (mBytesClass != null) {
            Object kk = mBytesClass.getTrue();
            if (kk != null) {
                Integer intk = (Integer) kk;
                doSatus(intk, in, mBytesClass);
            }
        }
        return false;
    }

    private void doSatus(Integer intk, Intent in, BytesClass mBytesClass) {
        switch (intk) {
            case NetChange: {
                boolean isWifi = NetStatusUtil.isWifiAvailable();
                if(isWifi) {
                    DeviceInfo.getInstens().setIp();
                }

                String wifiname = DeviceUtil.getWifiSsid();
                int key = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.device_used_type);
                boolean oo1 = IntentChangeManger.isDevServiceStart(),
                        oo2 = IntentChangeManger.isMainServiceStart();

                String deviceWifeName = SharedPreferencesUtils.getString(SharedPreferencesKeys.deviceWifiName);
                String mainServerWifiName = SharedPreferencesUtils.getString(SharedPreferencesKeys.mainServerWifiName);
                String client_WifiName = SharedPreferencesUtils.getString(SharedPreferencesKeys.client_WifiName);


                switch (key) {
                    case SharedPreferencesKeys.device_used_type_noSet:
                        break;
                    case SharedPreferencesKeys.device_used_type_device: {
                        if (deviceWifeName.equals(wifiname)) {
                            if (isWifi) {
                                if (!oo1) {
                                    startDev();
                                }
                            }
                        } else {//关闭
                            if (!oo1) {
                                stopDev();
                            }
                        }
                    }
                    break;
                    case SharedPreferencesKeys.device_used_type_only_server:
                        if (mainServerWifiName.equals(wifiname)) {
                            if (isWifi) {
                                if (!oo2) {
                                    startMain();
                                }

                            }
                        } else {//关闭
                            if (!oo2 ) {
                                stopMain();
                            }

                        }
                        break;
                    case SharedPreferencesKeys.device_used_type_server_Device: {
                        if (mainServerWifiName.equals(wifiname)) {
                            if (isWifi) {
                                if (!oo2) {
                                    startMain();
                                }

                            }
                        } else {//关闭
                            if (!oo2) {
                                stopMain();
                            }
                        }

                        if (deviceWifeName.equals(wifiname)) {
                            if (isWifi) {
                                if (!oo1) {
                                    startDev();
                                }
                            }
                        } else {//关闭
                            if (!oo1) {
                                stopDev();
                            }
                        }
                    }
                    break;
                    case SharedPreferencesKeys.device_used_type_client: {

                    }
                    break;
                }
            }
            break;
        }
    }






    private void startMain() {
        IntentChangeManger.startService(mCon, IntentChangeManger.Main_Service_UDPreviceback_TCPLongLink);
        NotifiationUtil.cancel(NotifiationUtil.LinkInWifi);
    }

    private void stopMain() {
        IntentChangeManger.stopService(mCon, IntentChangeManger.Main_Service_UDPreviceback_TCPLongLink);
        NotifiationUtil.showNotification(new Intent(mCon, ServerDeviceServiceStatusAc.class),"服务终止","网络变化，连接断开",NotifiationUtil.LinkInWifi);
    }

    private void stopDev() {
        IntentChangeManger.unbindServiceToApp(IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
        IntentChangeManger.stopService(mCon, IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
        NotifiationUtil.showNotification(new Intent(mCon, ServerDeviceServiceStatusAc.class),"服务终止","网络变化，连接断开",NotifiationUtil.LinkInWifi);
    }

    private void startDev() {
        String ip = SharedPreferencesUtils.getString(SharedPreferencesKeys.main_Server_ip);
        if (StringUtil.isEmpty(ip)) {
            ip = UDPTCPkeys.selfIp;
        }
        IntentChangeManger.bindServiceToApp(mCon, IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink, new BytesClass(ip));
        NotifiationUtil.cancel(NotifiationUtil.LinkInWifi);
    }


}
