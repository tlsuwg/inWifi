package com.hxuehh.rebirth.all.FaceView;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.server.FaceView.ServerInit.ProViewForStep;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitTobeLinkServer2Status extends FaceGetMainViewImp implements ProViewForStep {

    public SinitTobeLinkServer2Status(FaceContextWrapImp context, int Viewkey) {
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
    @Override
    public void setLoadingView(String info) {
        this.info=info;
        mProView.setLoadingName(info);
       start();
    }

    private void start() {
        String ip = SharedPreferencesUtils.getString(SharedPreferencesKeys.main_Server_ip);
        if (StringUtil.isEmpty(ip)) {
            ip = UDPTCPkeys.selfIp;
        }
        if (getViewKey() == ViewKeys.Client_StatusAc) {
            IntentChangeManger.bindServiceToApp(getFaceContext(), IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink, new BytesClass(ip));
        } else {
            IntentChangeManger.bindServiceToApp(getFaceContext(), IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink, new BytesClass(ip));
        }
    }

    @Override
    public void onErr() {

    }

    public void setStartErrType(int key) {
        if (key == 1) {
            mProView.setErrorInfo("连接中心服务，启动失败，请清理Android进程重试");

        } else if (key == 2) {
            mProView.setErrorInfo("本机链接中心服务，失败，请查看该设备联网情况，并且检查应用启动。"+StringUtil.N+"点击可重试。");
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLoadingView(info);
                }
            });
        }

    }


    FaceCommCallBack faceCommCallBackOK;

    @Override
    public void setDoSucceed(FaceCommCallBack faceCommCallBack) {
        mainView.setOnClickListener(null);
        this.faceCommCallBackOK = faceCommCallBack;
    }

    @Override
    public void onOk(String info) {
        mProView.setOk(info, false);
//        faceCommCallBackOK.callBack(status + 1);
    }


    public void setRunningStatus() {
        AbService_TCPLongLink_ mAbService_TCPLongLink_ = null;
        if (getViewKey() != ViewKeys.ServerDeviceServiceStatusAc) {
            mAbService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_ClientService();
        } else {
            mAbService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_DeviceService();
        }

        if (mAbService_TCPLongLink_ != null) {
            RunningStatus ms[] = mAbService_TCPLongLink_.getRunStatus();
            String info1 = null;
            if (ms[0] != null) {
                info1 = "监听服务运行正常:" + ms[0].toStatusString() + StringUtil.N;
            }
            if (ms[1] != null) {
                if (ms[1].getStatus() == RunningStatus.RunTimeErr) {
                    mProView.setErrorInfo(info1 + StringUtil.N + "中心服务链接异常:" + ms[1].toStatusString() +
                            StringUtil.N + (ms[2] == null ? "" : ";" + ms[2].toStatusString() + StringUtil.N) + ";导致的原因可能是中心服务设备掉线 或者 app被杀死。");
                } else {
                    if (getViewKey() == ViewKeys.Client_StatusAc) {
                        onOk("链接正常：" + StringUtil.N + mAbService_TCPLongLink_.getRunStatusInfo());
                    } else {
                        onOk("设备，运行正常：" + StringUtil.N + mAbService_TCPLongLink_.getRunStatusInfo());
                    }
                }
            }
        }else{
            mProView.setErrorInfo("没有发现本机链接，请退出该界面");
        }

    }
}
