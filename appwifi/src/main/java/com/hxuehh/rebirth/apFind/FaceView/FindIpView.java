package com.hxuehh.rebirth.apFind.FaceView;

import android.net.wifi.ScanResult;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;


/**
 * Created by suwg on 2015/8/13.
 */
public class FindIpView extends FaceGetMainViewImp implements ProViewForStep {

    public FindIpView(FaceContextWrapImp context, int Viewkey) {
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


    @Override
    public void setLoadingView(String info) {
        mProView.setLoadingName(info);
        checkIP();
    }

    private void checkIP() {
        String ip = null;
        try {
            ip = NetStatusUtil.getWifiIp();
        } catch (Exception e) {
            e.printStackTrace();
            mProView.setErrorInfo("没有获取到局域网IP");
        }
       if(ip.equals("0.0.0.0")){
            getFaceContext().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkIP();
                }
            },2000);
        }else  if (!ip.startsWith("192")) {
            mProView.setErrorInfo("使用的IP为非192段" + ip + "，出错！");
        } else {
            mProView.setOk("查找到当前ip:" + ip, false);
            faceCommCallBackOK.callBack(status + 1, ip);
        }
    }


    @Override
    public void onErr() {
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
