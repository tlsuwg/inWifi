package com.hxuehh.rebirth.apFind.FaceView;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;


/**
 * Created by suwg on 2015/8/13.
 */
public class SinitTestnetWifi extends FaceGetMainViewImp implements ProViewForStep {

    public SinitTestnetWifi(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    private ProView mProView;

    @Override
    protected void initView() {
        mainView= View.inflate(getFaceContext(), R.layout.select_init_backg, null);
        mProView = new ProView(getFaceContext());
        ViewKeys.addIntoLin(mainView.findViewById(R.id.main_view),mProView.getMainView(),getFaceContext());
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
        if (NetStatusUtil.isWifiAvailable()) {
            getFaceContext().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProView.setOk("wifi环境确认,wifi名称:"+ DeviceUtil.getWifiSsid(),false);
                    faceCommCallBackOK.callBack(status+1,NetStatusUtil.getWifiInfo());
                }
            }, 1000);

        } else {
            getFaceContext().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onErr();
                }
            }, 1000);

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
}
