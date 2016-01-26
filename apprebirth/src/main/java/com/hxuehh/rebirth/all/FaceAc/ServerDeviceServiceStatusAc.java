package com.hxuehh.rebirth.all.FaceAc;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.FaceView.SinitTestnetWifi2;
import com.hxuehh.rebirth.all.FaceView.SinitTobeLinkServer2Status;
import com.hxuehh.rebirth.all.FaceView.SinitTobeMainServer2;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.device.DeviceCapacityBuilder;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.service.DeviceService_TCPLongLink_;
import com.hxuehh.rebirth.server.FaceView.UDPbroadcast.UDPServerFinder;
import com.hxuehh.rebirth.server.Services.MainService_UDPreviceback_TCPLongLink;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/8/13.
 */

//主要链接服务  和 设备状态
public class ServerDeviceServiceStatusAc extends FaceBaseActivity_1 {

    @Override
    public int getViewKey() {
        return ViewKeys.ServerDeviceServiceStatusAc;
    }

    private String actions[] = new String[]{
            MainService_UDPreviceback_TCPLongLink.TCP_UDP_Succeed,
            MainService_UDPreviceback_TCPLongLink.TCPErr,
            MainService_UDPreviceback_TCPLongLink.UDPErr,
            DeviceService_TCPLongLink_.TCP_Succeed,
            DeviceService_TCPLongLink_.TCPListenErr,
            DeviceService_TCPLongLink_.TCPLinkMainErr,
            UDPServerFinder.UDPServerFinder,
    };

    FaceCommCallBack FaceCommCallBackON = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == toBeServer) {
                mSinitTobeMainServer.setSucceed();
            }
            return false;
        }
    };
    FaceCommCallBack FaceCommCallBackTCPerr = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == toBeServer) {
                mSinitTobeMainServer.setErrType(1);
            }
            return false;
        }
    };
    FaceCommCallBack FaceCommCallBackUDPerr = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == toBeServer) {
                mSinitTobeMainServer.setErrType(2);
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackON1 = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == deviceStart) {
                mSinitTobeDeviceServer.setRunningStatus();

            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackTCPListen = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == deviceStart) {
                mSinitTobeDeviceServer.setStartErrType(2);
                stopDeviceService();
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackTCPLink = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == deviceStart) {
                mSinitTobeDeviceServer.setStartErrType(1);
                stopDeviceService();
            }
            return false;
        }
    };
    FaceCommCallBack FaceCommCallBackFindMain = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (mSinitTobeDeviceServer != null) mSinitTobeDeviceServer.setRunningStatus();
            return false;
        }
    };


    private FaceCommCallBack faceCommCallBack[] = new FaceCommCallBack[]{
            FaceCommCallBackON, FaceCommCallBackTCPerr,
            FaceCommCallBackUDPerr, FaceCommCallBackON1,
            FaceCommCallBackTCPListen, FaceCommCallBackTCPLink, FaceCommCallBackFindMain};

    int device_used_type;
    String title, endShowStr;

    private void stopDeviceService() {
        IntentChangeManger.unbindServiceToApp(IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
        IntentChangeManger.stopService(this, IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin_buttom_button);
        setActionReceivers(actions, faceCommCallBack);
        device_used_type = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.device_used_type);
        initView();
        addListeners();
    }

    @Override
    public void addListeners() {
        super.addListeners();
        findViewById(R.id.change_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_used_type_noSet, true);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        switch (device_used_type) {
            case SharedPreferencesKeys.device_used_type_device:
                title = "设备状态检测";
                break;
            case SharedPreferencesKeys.device_used_type_only_server:
                title = "中心服务检测";
                break;
            case SharedPreferencesKeys.device_used_type_server_Device:
                title = "中心服务及设备状态检测";
                break;
            case SharedPreferencesKeys.device_used_type_client:
                title = "控制端状态检测";
                break;
        }

        TitleView mTitle = new TitleView(this);
        mTitle.setTitle(title);
        mTitle.addIntoView(this, R.id.title_lin);
    }


    boolean oo1, oo2;

    @Override
    protected void onResume() {
        super.onResume();
        if (status == all_OK) return;
        oo1 = IntentChangeManger.isDevServiceStart();
        oo2 = IntentChangeManger.isMainServiceStart();
        status = testWifi;
        doStatus();
    }

    int status = testWifi;

    public static final int testWifi = 1;
    public static final int toBeServer = 2;
    public static final int toBeServerOK = 3;
    public static final int deviceStart = 4;
    public static final int all_OK = 5;


    private void doStatus() {
        switch (status) {
            case testWifi: {
                testWifi(status);
            }
            break;
            case toBeServer: {
                switch (device_used_type) {
                    case SharedPreferencesKeys.device_used_type_device:
                        status = deviceStart;
                        doStatus();
                        return;

                }
                toBeServer(status);
            }
            break;
            case toBeServerOK:

            {
                switch (device_used_type) {
                    case SharedPreferencesKeys.device_used_type_only_server:
                        status = all_OK;
                        doStatus();
                        return;

                }
                toBeServerOK(status);
            }
            break;

            case deviceStart: {

//               listForDecices=null;
                deviceStart(status);
            }
            break;
            case all_OK:

            {
                switch (device_used_type) {
                    case SharedPreferencesKeys.device_used_type_device:
                        endShowStr = "设备启动完成，请使用控制端操作";
                        break;
                    case SharedPreferencesKeys.device_used_type_only_server:
                        endShowStr = "中心服务启动完成，（设备不被控制），请添加其他被控制端，或者重新设置";
                        break;
                    case SharedPreferencesKeys.device_used_type_server_Device:
                        endShowStr = "中心服务，设备启动完成，请使用控制端操作";
                        break;

                }
                all_OK(status);
            }
            break;
        }
    }


    private void all_OK(int status) {
        TextView mTe = new TextView(getFaceContext());
        mTe.setText(endShowStr);
        ViewKeys.addIntoLin(R.id.select_title_lin, mTe, getFaceContext());
    }

    private void toBeServerOK(int status) {
        TextView mTe = new TextView(getFaceContext());
        mTe.setText("中心服务启动完成");
        ViewKeys.addIntoLin(R.id.select_title_lin, mTe, getFaceContext());
        mFaceCommCallBackSucc.callBack(status + 1);
        SharedPreferencesUtils.putString(SharedPreferencesKeys.main_Server_ip, UDPTCPkeys.selfIp);
    }


    SinitTestnetWifi2 SinitTestnetWifiFortestWifi;
    SinitTobeMainServer2 mSinitTobeMainServer;
    SinitTobeLinkServer2Status mSinitTobeDeviceServer;


    FaceCommCallBack mFaceCommCallBackSucc = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            status = (Integer) t[0];
            doStatus();
            return false;
        }
    };


    private void deviceStart(int status2) {
        if (mSinitTobeDeviceServer == null) {
            mSinitTobeDeviceServer = new SinitTobeLinkServer2Status(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitTobeDeviceServer.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitTobeDeviceServer.setStatus(status2);
            mSinitTobeDeviceServer.setDoSucceed(mFaceCommCallBackSucc);
        }
        if (!oo1) {
            mSinitTobeDeviceServer.setLoadingView("设备连接...");
        } else {
            mSinitTobeDeviceServer.setRunningStatus();
        }
    }


    private void toBeServer(int status2) {
        if (mSinitTobeMainServer == null) {
            mSinitTobeMainServer = new SinitTobeMainServer2(new FaceContextWrapImp(getFaceContext()), 0);
            mSinitTobeMainServer.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitTobeMainServer.setStatus(status2);
            mSinitTobeMainServer.setDoSucceed(mFaceCommCallBackSucc);
        }
        if (!oo2) {
            mSinitTobeMainServer.setLoadingView("开启中心服务...");
        } else {
            mFaceCommCallBackSucc.callBack(status2 + 1);
            mSinitTobeMainServer.onOk("已经启动中心服务");
        }

    }


    private void testWifi(final int status2) {
        if (SinitTestnetWifiFortestWifi == null) {
            SinitTestnetWifiFortestWifi = new SinitTestnetWifi2(new FaceContextWrapImp(getFaceContext()), 0);
            SinitTestnetWifiFortestWifi.addIntoView(getFaceContext(), R.id.select_title_lin);
            SinitTestnetWifiFortestWifi.setStatus(status2);
            SinitTestnetWifiFortestWifi.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinitTestnetWifiFortestWifi.setLoadingView("检测wifi下进行...", device_used_type);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
