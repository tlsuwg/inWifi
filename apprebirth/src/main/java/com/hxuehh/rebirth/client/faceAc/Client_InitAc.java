package com.hxuehh.rebirth.client.faceAc;

import com.hxuehh.rebirth.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.client.FaceView.SinittestHasMainServerInWifi2;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitStopServices;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitTestnetWifi;
import com.hxuehh.rebirth.server.FaceView.ServerInit.SinitToStartService;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/8
 */
public class Client_InitAc extends FaceBaseActivity_1 {

    @Override
    public int getViewKey() {
        return ViewKeys.Client_InitAc;
    }

    FaceCommCallBack FaceCommCallBackON1 = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == ClientStart) {
                mSinitToStartService.setSucceed();
                SharedPreferencesUtils.putString(SharedPreferencesKeys.client_WifiName, DeviceUtil.getWifiSsid());
                if(!IntentChangeManger.isClient_Dev_Main()) {
                    SharedPreferencesUtils.putInteger(SharedPreferencesKeys.device_used_type, SharedPreferencesKeys.device_used_type_client);//只是设备
                }
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackTCPListen = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == ClientStart) {
                mSinitToStartService.setErrType(2);
                stopClientService();
            }
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackTCPLink = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (status == ClientStart) {
                mSinitToStartService.setErrType(1);
                stopClientService();
            }
            return false;
        }
    };

    private FaceCommCallBack faceCommCallBack[] = new FaceCommCallBack[]{
            FaceCommCallBackON1,
            FaceCommCallBackTCPListen, FaceCommCallBackTCPLink};

    private String actions[] = new String[]{
            ClientService_TCPLongLink_.TCP_Succeed,
            ClientService_TCPLongLink_.TCPListenErr,
            ClientService_TCPLongLink_.TCPLinkMainErr,
    };



    private void stopClientService() {
        IntentChangeManger.unbindServiceToApp(IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink);
        IntentChangeManger.stopService(this, IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        setActionReceivers(actions, faceCommCallBack);
        initView();
        addListeners();
    }

    @Override
    public void initView() {
        super.initView();
        TitleView mTitle = new TitleView(this);
        mTitle.setTitle("控制端设置");
        mTitle.addIntoView(this, R.id.title_lin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status == all_OK) return;
        status = stopService;
        doStatus();
    }

    int status = stopService;
    public static final int stopService = 0;
    public static final int testWifi = 1;
    public static final int testHasMainServerInWifi = 2;
    public static final int ClientStart = 3;
    public static final int all_OK = 4;


    private void doStatus() {
        switch (status) {
            case stopService: {
                stopService(status);
            }
            break;

            case testWifi: {
                testWifi(status);
            }
            break;
            case testHasMainServerInWifi: {
                testHasServerInWifi(status);
            }
            break;

            case ClientStart: {
                deviceStart(status);
            }
            break;
            case all_OK: {
                all_OK(status);
            }
            break;
        }
    }


    private void all_OK(int status) {
        TextView mTe = new TextView(getFaceContext());
        mTe.setText("中心服务链接完成，请点击使用");
        ViewKeys.addIntoLin(R.id.select_title_lin, mTe, getFaceContext());
        mTe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentChangeManger.jumpTo(getFaceContext(),IntentChangeManger.flag_client_only,true);
            }
        });
    }

    SinitTestnetWifi SinitTestnetWifiFortestWifi;
    SinittestHasMainServerInWifi2 SinittestHasServerInWifi;
    SinitToStartService mSinitToStartService;
    SinitStopServices mSinitStopServices;


    FaceCommCallBack mFaceCommCallBackSucc = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            status = (Integer) t[0];
            doStatus();
            return false;
        }
    };


    private void deviceStart(int status2) {
        if (mSinitToStartService == null) {
            mSinitToStartService = new SinitToStartService(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitToStartService.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitToStartService.setStatus(status2);
            mSinitToStartService.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSinitToStartService.setLoadingView("中心服务连接...");
    }


    private void stopService(int status2) {
        if (mSinitStopServices == null) {
            mSinitStopServices = new SinitStopServices(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitStopServices.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitStopServices.setStatus(status2);
            mSinitStopServices.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSinitStopServices.setLoadingView("正在关闭各个服务...");
    }


    private void testHasServerInWifi(int status2) {
        if (SinittestHasServerInWifi == null) {
            SinittestHasServerInWifi = new SinittestHasMainServerInWifi2(new FaceContextWrapImp(getFaceContext()), getViewKey());
            SinittestHasServerInWifi.addIntoView(getFaceContext(), R.id.select_title_lin);
            SinittestHasServerInWifi.setStatus(status2);
            SinittestHasServerInWifi.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinittestHasServerInWifi.setLoadingView("正在检测wifi下主机...");
    }

    private void testWifi(final int status2) {
        if (SinitTestnetWifiFortestWifi == null) {
            SinitTestnetWifiFortestWifi = new SinitTestnetWifi(new FaceContextWrapImp(getFaceContext()), getViewKey());
            SinitTestnetWifiFortestWifi.addIntoView(getFaceContext(), R.id.select_title_lin);
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
}
