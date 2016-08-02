package com.hxuehh.rebirth.apFind.FaceView;

import android.net.wifi.ScanResult;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.apFind.WifiAdmin;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;


/**
 * Created by suwg on 2015/8/13.
 */
public class LinkInMyApView extends FaceGetMainViewImp implements ProViewForStep {

    public LinkInMyApView(FaceContextWrapImp context, int Viewkey) {
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

        mWifiAdmin.openWifi();
        boolean isTrue = mWifiAdmin.addNetwork_In(mWifiAdmin.CreateWifiInfo(mScanResult.SSID, null, 1));
        if (isTrue) {
            checkWifiLinkRes();
        } else {
            mProView.setErrorInfo("不能接入"+mScanResult.SSID);
        }

    }

    private void checkWifiLinkRes() {
        if(getFaceContext().isOnTop())
        getFaceContext().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(NetStatusUtil.isNetWorkEnable()){
                    mProView.setOk("连接到" + mScanResult.SSID, false);
                    faceCommCallBackOK.callBack(status + 1);
                }else{
                    checkWifiLinkRes();
                }
            }
        },2000);

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

    ScanResult mScanResult;

    public void setScanResult(ScanResult mScanResult) {
        this.mScanResult = mScanResult;
    }
}
