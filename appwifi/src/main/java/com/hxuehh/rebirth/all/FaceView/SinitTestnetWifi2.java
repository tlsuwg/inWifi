package com.hxuehh.rebirth.all.FaceView;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.apFind.FaceView.ProViewForStep;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitTestnetWifi2 extends FaceGetMainViewImp implements ProViewForStep {

    public SinitTestnetWifi2(FaceContextWrapImp context, int Viewkey) {
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

    public void setLoadingView(String info, int device_used_type) {

        mProView.setLoadingName(info);
        if (NetStatusUtil.isWifiAvailable()) {
            String currentWifiName = DeviceUtil.getWifiSsid();
            final String deviceWifeName = SharedPreferencesUtils.getString(SharedPreferencesKeys.deviceWifiName);
            String mainServerWifiName = SharedPreferencesUtils.getString(SharedPreferencesKeys.mainServerWifiName);
            String client_WifiName = SharedPreferencesUtils.getString(SharedPreferencesKeys.client_WifiName);

            boolean is = true;
            switch (device_used_type) {
                case SharedPreferencesKeys.device_used_type_device:
                    if (!deviceWifeName.equals(currentWifiName)) {
                        is = false;
                    }
                    break;
                case SharedPreferencesKeys.device_used_type_only_server:

                    if (!mainServerWifiName.equals(currentWifiName)) {
                        is = false;
                    }
                    break;
                case SharedPreferencesKeys.device_used_type_server_Device:
                    if (!deviceWifeName.equals(currentWifiName)) {
                        is = false;
                    }
                    if (!mainServerWifiName.equals(currentWifiName)) {
                        is = false;
                    }
                    break;
                case SharedPreferencesKeys.device_used_type_client:
                    if (!client_WifiName.equals(currentWifiName)) {
                        is = false;
                    }
                    break;
            }

            if (!is) {
                getFaceContext().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mProView.setErrorInfo("wifi变动，需要使用" + deviceWifeName+"，您可以点击设置网络");
                    }
                });
                mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetStatusUtil.openNetSetting(getFaceContext());
                    }
                });

            }else {
                getFaceContext().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProView.setOk("wifi环境确认,wifi名称:" + DeviceUtil.getWifiSsid(), false);
                        mainView.setOnClickListener(null);
                        faceCommCallBackOK.callBack(status + 1);
                    }
                }, (getViewKey() == ViewKeys.Client_StatusAc) ? 100 : 1000);
            }

        } else {
            getFaceContext().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProView.setErrorInfo("如果使用，必须使用Wifi关联，请点击设置");
                    mainView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NetStatusUtil.openNetSetting(getFaceContext());
                        }
                    });
                }
            }, 1000);

        }


    }
}
