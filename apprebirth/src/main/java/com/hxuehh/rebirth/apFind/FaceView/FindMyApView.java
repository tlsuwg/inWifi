package com.hxuehh.rebirth.apFind.FaceView;

import com.hxuehh.rebirth.R;

import android.net.wifi.ScanResult;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.apFind.WifiAdmin;
import com.hxuehh.rebirth.server.FaceView.ServerInit.ProViewForStep;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.util.List;


/**
 * Created by suwg on 2015/8/13.
 */
public class FindMyApView extends FaceGetMainViewImp implements ProViewForStep {

    public FindMyApView(FaceContextWrapImp context, int Viewkey) {
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


    String info;
    int tryTimes;


    @Override
    public void setLoadingView(String info) {
        this.info = info;
        findWifetest();
    }

    private void findWifetest() {
        mProView.setLoadingName(info + (tryTimes++));
        List<ScanResult> list = null;
        try {
            list = NetStatusUtil.getAllWifiAps();
        } catch (Exception e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage());
            return;
        }

        boolean isget = false;
        if (list != null && list.size() > 0) {
            for (ScanResult mScanResult : list) {
                String SSID = mScanResult.SSID;

                try {
//                    if(SSID.startsWith("SENSOR_ALARM_WIFI_AP_")){
//                        isget = true;
//                    }

                    if(SSID.equals("ybbm2")){
                        isget = true;
                    }

//                    String[] arrs = SSID.split(":");
//                    if (arrs != null && arrs.length >= 3) {
//                        if (Integer.parseInt(arrs[2]) == Integer.parseInt(arrs[1]) + Integer.parseInt(arrs[0])) {
//                            isget = true;
//                        }
//                    }

                    if(isget){
                        mProView.setOk("查找到" + mScanResult.SSID, false);
                        faceCommCallBackOK.callBack(status + 1, mScanResult);
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!isget&&getFaceContext().isOnTop())
            getFaceContext().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findWifetest();
                }
            }, 3000);

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
