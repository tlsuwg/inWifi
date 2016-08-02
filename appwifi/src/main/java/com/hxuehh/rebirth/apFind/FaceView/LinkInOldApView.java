package com.hxuehh.rebirth.apFind.FaceView;

import android.net.wifi.WifiInfo;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.apFind.WifiAdmin;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;


/**
 * Created by suwg on 2015/8/13.
 */
public class LinkInOldApView extends FaceGetMainViewImp implements ProViewForStep {

    public LinkInOldApView(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    private ProView mProView;

    @Override
    protected void initView() {
        mainView = View.inflate(getFaceContext(), R.layout.select_init_backg, null);
        mProView = new ProView(getFaceContext());
        ViewKeys.addIntoLin(mainView.findViewById(R.id.main_view), mProView.getMainView(), getFaceContext());
    }

    int status;

    @Override
    public int setStatus(int status) {
        this.status = status;
        return status;
    }


    WifiAdmin mWifiAdmin;

    @Override
    public void setLoadingView(String info) {
        mProView.setLoadingName(info);
        if (mWifiAdmin == null) {
            mWifiAdmin = new WifiAdmin(getFaceContext());
        }
//       WifiInfo mWifiInfo= NetStatusUtil.getWifiInfo();
//        mWifiAdmin.disconnectWifi(mWifiInfo.getNetworkId());
        boolean isTrue = mWifiAdmin.enableNetwork(this.mWifiInfo.getNetworkId());
        if (isTrue) {
            checkWifiLinkRes();
        } else {
            mProView.setErrorInfo("不能接入" + mWifiInfo.getSSID() + ",可能导致服务器端无法进行绑定,您可以收到设置wifi");
            SharedPreferencesUtils.putString(SharedPreferencesKeys.SunShaoQingregisterNoSendServerStatus,"1");
        }
    }

    private void checkWifiLinkRes() {
        if(getFaceContext().isOnTop())
        getFaceContext().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetStatusUtil.isNetWorkEnable()) {
                    checkIP();
                } else {
                    checkWifiLinkRes();
                }
            }
        }, 2000);
    }



    private void checkIP() {
        String ip = null;
        try {
            ip = NetStatusUtil.getWifiIp();
        } catch (Exception e) {
            e.printStackTrace();
            mProView.setErrorInfo("没有获取到局域网IP");
        }
        if("0.0.0.0".equals(ip)|| StringUtil.isEmpty(ip)){
            getFaceContext().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkIP();
                }
            },2000);
        } else {
            mProView.setOk("连接到" + mWifiInfo.getSSID(), false);
            faceCommCallBackOK.callBack(status + 1);
        }
    }


    @Override
    public void onErr() {
        mProView.setErrorInfo("首次使用必须使用Wifi关联，请点击设置");
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetStatusUtil.openNetSetting(getFaceContext());
            }
        });
    }

    FaceCommCallBack faceCommCallBackOK;

    @Override
    public void setDoSucceed(FaceCommCallBack faceCommCallBack) {
        mainView.setOnClickListener(null);
        this.faceCommCallBackOK = faceCommCallBack;
    }

    @Override
    public void onOk(String info) {

    }

    WifiInfo mWifiInfo;
    public void setOldWifiInfo(WifiInfo mWifiInfo) {
        this.mWifiInfo = mWifiInfo;
    }
}
