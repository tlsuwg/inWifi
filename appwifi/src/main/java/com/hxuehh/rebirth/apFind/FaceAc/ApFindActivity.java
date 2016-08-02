package com.hxuehh.rebirth.apFind.FaceAc;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.apFind.FaceView.FindIpView;
import com.hxuehh.rebirth.apFind.FaceView.FindMyApView;
import com.hxuehh.rebirth.apFind.FaceView.LinkInMyApView;
import com.hxuehh.rebirth.apFind.FaceView.LinkInOldApView;
import com.hxuehh.rebirth.apFind.FaceView.SendInfoToServerView;
import com.hxuehh.rebirth.apFind.FaceView.SendtoApMyWifiInfosView;
import com.hxuehh.rebirth.apFind.FaceView.SinitTestnetWifi;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2016/1/6.
 */
public class ApFindActivity extends FaceBaseActivity_1 {

    public static final String SunShaoQingApDeviceRegsetInServerOK="com.sunshaoqing.SunShaoQingApDeviceRegsetInServerOK";
    int status = StopS;
    public static final int StopS = 0;
    public static final int TestWifi = 1;
    public static final int FindMyAp = 2;
    public static final int LinkInMyAP = 3;
    public static final int Find_IP = 4;
    public static final int Send_APwifi_infos = 5;
    public static final int LinkInOldAP = 6;
    public static final int SendToServer= 7;
    public static final int SendToServerOK= 8;
    public static final int All_OK = 9;

    boolean isTestLinkInOldAP=false;

    @Override
    public int getViewKey() {
        return ViewKeys.ApFindActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_lin);
        initView();
        addListeners();
    }

    @Override
    public void initView() {
        super.initView();
        TitleView mTitle = new TitleView(this);
        mTitle.setTitle("发现本地设备");
        mTitle.addIntoView(this, R.id.title_lin);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (status == All_OK) return;
        String registerNoSendServerStatus=SharedPreferencesUtils.getString(SharedPreferencesKeys.SunShaoQingregisterNoSendServerStatus);
        if(!StringUtil.isEmpty(registerNoSendServerStatus)){
            status=LinkInOldAP;
        }else if(isTestLinkInOldAP){//测试
            status=  SendToServer;
        }else {
            status = StopS;
        }
        doStatus();
    }


    private void doStatus() {
        switch (status) {
            case StopS: {
                StopS(status);
            }
            break;
            case TestWifi: {
                testWifi(status);
            }
            break;
            case FindMyAp: {
                findMyAp(status);
            }
            break;
            case LinkInMyAP: {
                linkInMyAP(status, mScanResult_MyAp);
            }
            break;
            case Find_IP: {
                find_IP(status);
            }
            break;
            case Send_APwifi_infos: {
                Send_wifi_infos(status);
            }
            break;
            case LinkInOldAP: {
                LinkInOldAP(status);
            }
            break;
            case SendToServer: {
                SendToServer(status);
            }
            break;
            case SendToServerOK: {
                SendToServerOk(status);
            }
            break;
            case All_OK: {
                SharedPreferencesUtils.remove(SharedPreferencesKeys.SunShaoQingregisterNoSendServerStatus);
                all_OK(status);
            }
            break;
        }
    }

    private void SendToServerOk(int status) {
        TextView mTextView=new TextView(getFaceContext());
        mTextView.setText("发送成功，等待服务端响应");
        ViewKeys.addIntoLin(R.id.select_title_lin,mTextView,getFaceContext());
    }


    private void all_OK(int status) {
        SharedPreferencesUtils.putString(SharedPreferencesKeys.SunShaoQingRegisterSucceed,"1");
        TextView mTextView=new TextView(getFaceContext());
        mTextView.setText("绑定成功");
        ViewKeys.addIntoLin(R.id.select_title_lin,mTextView,getFaceContext());
    }

    SendInfoToServerView mSendInfoToServerView;
    private void SendToServer(int status) {
        if (mSendInfoToServerView == null) {
            mSendInfoToServerView = new SendInfoToServerView(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSendInfoToServerView.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSendInfoToServerView.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSendInfoToServerView.setLoadingView("发送服务端，进行绑定...");
    }

    LinkInOldApView mLinkInOldApView;
    private void LinkInOldAP(int status2) {
        if (mLinkInOldApView == null) {
            mLinkInOldApView = new LinkInOldApView(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mLinkInOldApView.addIntoView(getFaceContext(), R.id.select_title_lin);
            mLinkInOldApView.setStatus(status2);
            mLinkInOldApView.setOldWifiInfo(mWifiInfo);
            mLinkInOldApView.setDoSucceed(mFaceCommCallBackSucc);
        }
        mLinkInOldApView.setLoadingView("接入wifi"+mWifiInfo.getSSID());
    }


    FindIpView mFindIpView;
    private void find_IP(int status2) {
        if (mFindIpView == null) {
            mFindIpView = new FindIpView(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mFindIpView.addIntoView(getFaceContext(), R.id.select_title_lin);
            mFindIpView.setStatus(status2);
            mFindIpView.setDoSucceed(mFaceCommCallBackSucc);
        }
        mFindIpView.setLoadingView("查找服务IP");
    }

    SendtoApMyWifiInfosView mSend_wifi_infosView;
    private void Send_wifi_infos(int status2) {
        if (mSend_wifi_infosView == null) {
            mSend_wifi_infosView = new SendtoApMyWifiInfosView(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSend_wifi_infosView.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSend_wifi_infosView.setStatus(status2);
            mSend_wifi_infosView.setDoSucceed(mFaceCommCallBackSucc);
        }
        String[] ips=ip.split("\\.");
        ips[3]="1";
        StringBuilder sb=new StringBuilder();
        for(String ss:ips){
            sb.append(ss).append(".");
        }
        sb.deleteCharAt(sb.length()-1);
       String newIP=sb.toString();
        mSend_wifi_infosView.setOldWifiInfo(mWifiInfo);
        mSend_wifi_infosView.setIP(newIP);
    }


    LinkInMyApView mLinkInMyApView;
    private void linkInMyAP(int status2, ScanResult mScanResult) {
        if (mLinkInMyApView == null) {
            mLinkInMyApView = new LinkInMyApView(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mLinkInMyApView.addIntoView(getFaceContext(), R.id.select_title_lin);
            mLinkInMyApView.setStatus(status2);
            mLinkInMyApView.setScanResult(mScanResult);
            mLinkInMyApView.setDoSucceed(mFaceCommCallBackSucc);
        }
        mLinkInMyApView.setLoadingView("接入wifi"+mScanResult.SSID);
    }


    FindMyApView mFindMyApView;
    private void findMyAp(int status2) {
        if (mFindMyApView == null) {
            mFindMyApView = new FindMyApView(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mFindMyApView.addIntoView(getFaceContext(), R.id.select_title_lin);
            mFindMyApView.setStatus(status2);
            mFindMyApView.setDoSucceed(mFaceCommCallBackSucc);
        }
        mFindMyApView.setLoadingView("请启动设备，并设置为AP模式。查找符合的Ap...");
    }


    SinitTestnetWifi SinitTestnetWifiFortestWifi;
    private void testWifi(final int status2) {
        if (SinitTestnetWifiFortestWifi == null) {
            SinitTestnetWifiFortestWifi = new SinitTestnetWifi(new FaceContextWrapImp(getFaceContext()), getViewKey());
            SinitTestnetWifiFortestWifi.addIntoView(getFaceContext(), R.id.select_title_lin);
            SinitTestnetWifiFortestWifi.setStatus(status2);
            SinitTestnetWifiFortestWifi.setDoSucceed(mFaceCommCallBackSucc);
        }
        SinitTestnetWifiFortestWifi.setLoadingView("需要在wifi下进行...");
    }


    private void StopS(int status2) {
        mFaceCommCallBackSucc.callBack(status2+1);
    }



    ScanResult mScanResult_MyAp;
    WifiInfo mWifiInfo;
    String ip;

    FaceCommCallBack mFaceCommCallBackSucc = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            status = (Integer) t[0];
                if(status==LinkInMyAP){//FindMyAp  new
                mScanResult_MyAp = (ScanResult) t[1];
            }else if(status==FindMyAp){//TestWifi  old
                if(mWifiInfo==null)
                mWifiInfo= (WifiInfo) t[1];
            }else if(status== Send_APwifi_infos){//Find_IP
                ip= (String) t[1];
            }
            doStatus();
            return false;
        }
    };




}
