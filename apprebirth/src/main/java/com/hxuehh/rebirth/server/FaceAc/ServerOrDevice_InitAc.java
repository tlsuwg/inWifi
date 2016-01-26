package com.hxuehh.rebirth.server.FaceAc;

import com.hxuehh.rebirth.R;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.capacity.screen.LockFaceAc.AdminReceiver;
import com.hxuehh.rebirth.device.DeviceCapacityBuilder;
import com.hxuehh.rebirth.device.service.DeviceService_TCPLongLink_;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitGetDevices;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitStopServices;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitTestnetWifi;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitToStartService;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitTobeMainServer;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinittestHasServerInWifi;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinittestSettingDevice;
import com.hxuehh.rebirth.server.Services.MainService_UDPreviceback_TCPLongLink;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.util.List;

/**
 * Created by suwg on 2015/8/13.
 */
public class ServerOrDevice_InitAc extends FaceBaseActivity_1 {

    @Override
    public int getViewKey() {
        return 0;
    }


    FaceCommCallBack FaceCommCallBackON   =new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(status==toBeServer){
                mSinitTobeMainServer.setSucceed();
                SharedPreferencesUtils.putString(SharedPreferencesKeys.mainServerWifiName, DeviceUtil.getWifiSsid());
                if(DevRunningTime.isSetSharedPreferencesForType) {
                    SharedPreferencesUtils.putInteger(SharedPreferencesKeys.device_used_type, SharedPreferencesKeys.device_used_type_only_server);//只是服务
                }
            }
            return false;
        }
    };
    FaceCommCallBack FaceCommCallBackTCPerr   =new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(status==toBeServer){
                mSinitTobeMainServer.setErrType(1);
            }
            return false;
        }
    };
    FaceCommCallBack FaceCommCallBackUDPerr   =new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(status==toBeServer){
                mSinitTobeMainServer.setErrType(2);
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackON1   =new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(status==deviceStart){
                mSinitTobeDeviceServer.setSucceed();
                int type=SharedPreferencesUtils.getInteger(SharedPreferencesKeys.device_used_type);
                SharedPreferencesUtils.putString(SharedPreferencesKeys.deviceWifiName, DeviceUtil.getWifiSsid());
                if(type==SharedPreferencesKeys.device_used_type_only_server){
                    if(DevRunningTime.isSetSharedPreferencesForType)
                    SharedPreferencesUtils.putInteger(SharedPreferencesKeys.device_used_type,SharedPreferencesKeys.device_used_type_server_Device);//双身份
                }else{
                    if(DevRunningTime.isSetSharedPreferencesForType)
                    SharedPreferencesUtils.putInteger(SharedPreferencesKeys.device_used_type,SharedPreferencesKeys.device_used_type_device);//只是设备
                }
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackTCPListen =new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(status==deviceStart){
                mSinitTobeDeviceServer.setErrType(2);
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackTCPLink   =new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(status==deviceStart){
                mSinitTobeDeviceServer.setErrType(1);
            }
            return false;
        }
    };

    private String actions[] = new String[]{
            MainService_UDPreviceback_TCPLongLink.TCP_UDP_Succeed,
            MainService_UDPreviceback_TCPLongLink.TCPErr,
            MainService_UDPreviceback_TCPLongLink.UDPErr,
            DeviceService_TCPLongLink_.TCP_Succeed,
            DeviceService_TCPLongLink_.TCPListenErr,
            DeviceService_TCPLongLink_.TCPLinkMainErr,

    };

    private FaceCommCallBack faceCommCallBack[] = new FaceCommCallBack[]{
            FaceCommCallBackON,FaceCommCallBackTCPerr,
            FaceCommCallBackUDPerr,FaceCommCallBackON1,
            FaceCommCallBackTCPListen,FaceCommCallBackTCPLink};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        setActionReceivers(actions,faceCommCallBack);
        initView();
        addListeners();
    }

    @Override
    public void initView() {
        super.initView();
        TitleView mTitle=new TitleView(this);
        mTitle.setTitle("设备设置");
        mTitle.addIntoView(this,R.id.title_lin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(status==all_OK||status==hasServerInWifi_getDevice ||status==hasServerInWifi_OK_Ask_Device)return;
        status=stopService;
        doStatus();
    }

    int status=stopService;
    public static final int stopService=0;
    public static final int testWifi=1;
    public static final int testHasMainServerInWifi =2;
    public static final int toBeServer=3;
    public static final int toBeServerOK=4;
    public static final int hasServerInWifi_OK =5;


    public static final int hasServerInWifi_OK_Ask_Device =6;
    public static final int hasServerInWifi_Ask_Admin_Device=7;
    public static final int hasServerInWifi_getDevice =8;
    public static final int hasServerInWifi_ShowDevice =9;
    public static final int hasServerInWifi_SettingDevice =10;
    public static final int deviceStart=11;
    public static final int all_OK=12;


    private void doStatus() {
       switch (status){
           case stopService:{
               stopService(status);
           }break;

           case testWifi:
           {
               testWifi(status);
           }
               break;
           case testHasMainServerInWifi:
           {
               testHasServerInWifi(status);
           }
           break;
           case toBeServer:
           {
               toBeServer(status);
           }
           break;
           case toBeServerOK:
           {
               toBeServerOK(status);
           }
           break;
           case hasServerInWifi_OK: {
               hasServerInWifi_OK(status);
           }
           break;

           case hasServerInWifi_OK_Ask_Device: {
               hasServerInWifi_OK_Ask_Device(status);
           }
           break;

           case hasServerInWifi_Ask_Admin_Device: {
               hasServerInWifi_Ask_Admin_Device(status);
           }
           break;
           case hasServerInWifi_getDevice:{
               hasServerInWifi_getDevice(status);
           }
           break;
           case hasServerInWifi_ShowDevice:
           {
               hasServerInWifi_ShowDevice(status);
           }
           break;
           case hasServerInWifi_SettingDevice:
           {
               hasServerInWifi_SettingDevice(status);
           }
           break;
           case deviceStart:
           {
//               listForDecices=null;
               deviceStart(status);
           }
           break;
           case all_OK:
           {
               all_OK(status);
           }
           break;
       }
    }

    private static final int MY_REQUEST_CODE = 9999;
    private void hasServerInWifi_Ask_Admin_Device(final int status) {

        DevicePolicyManager   policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        final ComponentName componentName = new ComponentName(this, AdminReceiver.class);
        if (policyManager.isAdminActive(componentName)){
            mFaceCommCallBackSucc.callBack(status + 1);
            return;
        }else{
            DialogUtil.showDelDialog(getFaceContext(), "设置", "需要使用“设备管理器”才能对屏幕进行操作，请“激活”使用", 0, "好的，清楚了", new FaceCommCallBack() {
                @Override
                public boolean callBack(Object[] t) {
                    // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    //权限列表
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    //描述(additional explanation)
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活后才能使用锁屏功能哦亲^^");
                    startActivityForResult(intent, MY_REQUEST_CODE);
                    return false;

                }
            },null,null);
        }

    }

    private void hasServerInWifi_OK_Ask_Device(final int status) {
        if(this.isOnTop())
        DialogUtil.showDelDialog(getFaceContext(), "设置", "设置本设备作为“被控制”端，响应指令控制。", 0, "确定", new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] t) {
                mFaceCommCallBackSucc.callBack(status + 1);
                return false;

            }
        }, "不使用", new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] t) {
                if (SharedPreferencesUtils.getInteger(SharedPreferencesKeys.device_used_type) == SharedPreferencesKeys.device_used_type_only_server) {
                    DialogUtil.showLongToast(getFaceContext(), "只作为中心服务使用，已经设置完毕");
                } else {
                    DialogUtil.showLongToast(getFaceContext(), "您没有把设备设置任何功能，3秒后重试");
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doStatus();
                        }
                    }, 3000);
                }
                return false;
            }
        });

    }


    private void hasServerInWifi_ShowDevice(final int status2) {
        if(getFaceContext().isOnTop()) {
            DialogUtil.showDelDialog(getFaceContext(),"展示","展示",0,"确认",new FaceCommCallBack() {
                @Override
                public boolean callBack(Object[] t) {
                    mFaceCommCallBackSucc.callBack(status2+1);
                    return false;

                }
            },null,null);
        }
    }

    private void all_OK(int status) {
        TextView mTe=new TextView(getFaceContext());
        mTe.setText("中心服务，设备启动完成，请使用控制端操作");
        ViewKeys.addIntoLin(R.id.select_title_lin,mTe,getFaceContext());

        if(AppStaticSetting.isTest) {
            Button b = new Button(getFaceContext());
            b.setText("测试变化");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    USBSerializ nn = (USBSerializ) DeviceCapacityBuilder.getInstance().getDeviceCapacityByKey(USBSerializ.Type_USB_Serializ);
                    nn.notifyStatusChange();
                }
            });
            ViewKeys.addIntoLin(R.id.select_title_lin, b, getFaceContext());
        }
    }

    private void toBeServerOK(int status) {
        TextView mTe=new TextView(getFaceContext());
        mTe.setText("中心服务启动完成");
        ViewKeys.addIntoLin(R.id.select_title_lin,mTe,getFaceContext());
        mFaceCommCallBackSucc.callBack(status + 1);
        SharedPreferencesUtils.putString(SharedPreferencesKeys.main_Server_ip, UDPTCPkeys.selfIp);
    }

    private void hasServerInWifi_OK(final int status) {
        TextView mTe=new TextView(getFaceContext());
        mTe.setText("局域网存在中心服务");
        ViewKeys.addIntoLin(R.id.select_title_lin, mTe, getFaceContext());
        mFaceCommCallBackSucc.callBack(status + 1);



    }



    SinitTestnetWifi SinitTestnetWifiFortestWifi;
    com.hxuehh.rebirth.server.FaceView.ServerInit.SinittestHasServerInWifi SinittestHasServerInWifi;
    SinitTobeMainServer mSinitTobeMainServer;
    com.hxuehh.rebirth.server.FaceView.ServerInit.SinitGetDevices SinitGetDevices;
    SinittestSettingDevice SinittestSetDevice;
    SinitToStartService mSinitTobeDeviceServer;
    SinitStopServices mSinitStopServices;

    transient List listForDecices;

    FaceCommCallBack mFaceCommCallBackSucc= new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            status=(Integer)t[0];
            if(status== hasServerInWifi_ShowDevice) {
                if (t != null && t.length > 0) {
                    listForDecices = (List) t[1];
                }
            }else if(status == hasServerInWifi_OK){
                if (t != null && t.length > 1) {
                    String ip = (String) t[1];
                    SharedPreferencesUtils.putString(SharedPreferencesKeys.main_Server_ip,ip);
                }
            }
            doStatus();
            return false;
        }
    };


    private void deviceStart(int status2) {
        if(mSinitTobeDeviceServer==null){
            mSinitTobeDeviceServer=new SinitToStartService(new FaceContextWrapImp(getFaceContext()),0);
            mSinitTobeDeviceServer.addIntoView(getFaceContext(),R.id.select_title_lin);
            mSinitTobeDeviceServer.setStatus(status2);
            mSinitTobeDeviceServer.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSinitTobeDeviceServer.setLoadingView("设备连接...");
    }



    private void hasServerInWifi_SettingDevice(int status2) {
        if(SinittestSetDevice==null){
            SinittestSetDevice=new SinittestSettingDevice(new FaceContextWrapImp(getFaceContext()),0);
            SinittestSetDevice.addIntoView(getFaceContext(),R.id.select_title_lin);
            SinittestSetDevice.setStatus(status2);
            SinittestSetDevice.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinittestSetDevice.setLoadingView("初始化设备...");
    }

    private void toBeServer(int status2) {
        if(mSinitTobeMainServer ==null){
            mSinitTobeMainServer =new SinitTobeMainServer(new FaceContextWrapImp(getFaceContext()),0);
            mSinitTobeMainServer.addIntoView(getFaceContext(),R.id.select_title_lin);
            mSinitTobeMainServer.setStatus(status2);
            mSinitTobeMainServer.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSinitTobeMainServer.setLoadingView("开启中心服务...");
    }

    private void stopService(int status2) {
        if(mSinitStopServices ==null){
            mSinitStopServices =new SinitStopServices(new FaceContextWrapImp(getFaceContext()),0);
            mSinitStopServices.addIntoView(getFaceContext(),R.id.select_title_lin);
            mSinitStopServices.setStatus(status2);
            mSinitStopServices.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSinitStopServices.setLoadingView("正在关闭各个服务...");
    }


    private void hasServerInWifi_getDevice(int status2) {
        if(SinitGetDevices ==null){
            TextView mTe=new TextView(getFaceContext());
            mTe.setText("...........................");
            ViewKeys.addIntoLin(R.id.select_title_lin,mTe,getFaceContext());

            SinitGetDevices =new SinitGetDevices(new FaceContextWrapImp(getFaceContext()),0);
            SinitGetDevices.addIntoView(getFaceContext(), R.id.select_title_lin);
            SinitGetDevices.setStatus(status2);
            SinitGetDevices.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinitGetDevices.setLoadingView("正在发现本机能力...");
    }

    private void testHasServerInWifi(int status2) {
        if(SinittestHasServerInWifi==null){
            SinittestHasServerInWifi=new SinittestHasServerInWifi(new FaceContextWrapImp(getFaceContext()),0);
            SinittestHasServerInWifi.addIntoView(getFaceContext(),R.id.select_title_lin);
            SinittestHasServerInWifi.setStatus(status2);
            SinittestHasServerInWifi.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinittestHasServerInWifi.setLoadingView("正在检测wifi下主机...");
    }

    private void testWifi(final int status2) {
        if(SinitTestnetWifiFortestWifi==null){
            SinitTestnetWifiFortestWifi=new SinitTestnetWifi(new FaceContextWrapImp(getFaceContext()),0);
            SinitTestnetWifiFortestWifi.addIntoView(getFaceContext(),R.id.select_title_lin);
            SinitTestnetWifiFortestWifi.setStatus(status2);
            SinitTestnetWifiFortestWifi.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinitTestnetWifiFortestWifi.setLoadingView("首次使用，需要在wifi下进行...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //获取权限成功，立即锁屏并finish自己，否则继续获取权限
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            mFaceCommCallBackSucc.callBack(status+1);
        }
        else
        {
            doStatus();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
