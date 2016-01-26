package com.hxuehh.rebirth.server.FaceView.ServerInit;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitToStartService extends FaceGetMainViewImp implements ProViewForStep {

    public SinitToStartService(FaceContextWrapImp context, int Viewkey) {
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
        String ip = SharedPreferencesUtils.getString(SharedPreferencesKeys.main_Server_ip);
        if (StringUtil.isEmpty(ip)) {
            ip = UDPTCPkeys.selfIp;
        }
        if (this.getViewKey() == ViewKeys.Client_InitAc) {
            IntentChangeManger.bindServiceToApp(getFaceContext(), IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink, new BytesClass(ip));
        } else {
            IntentChangeManger.bindServiceToApp(getFaceContext(), IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink, new BytesClass(ip));
        }
    }

    @Override
    public void onErr() {

    }

    public void setErrType(int key) {
//        if (this.getViewKey() == ViewKeys.Client_InitAc) {
//        }else {
//        }
        if (key == 1) {
            mProView.setErrorInfo("连接中心服务，启动失败，请清理Android进程重试");
        } else if (key == 2) {
            mProView.setErrorInfo("提供本机链接服务，启动失败，请清理Android进程重试");
        }
        onErr();
    }


    FaceCommCallBack faceCommCallBackOK;

    @Override
    public void setDoSucceed(FaceCommCallBack faceCommCallBack) {
        mainView.setOnClickListener(null);
        this.faceCommCallBackOK = faceCommCallBack;
    }

    @Override
    public void onOk(String info) {
        mProView.setOk(info,  false);
        faceCommCallBackOK.callBack(status + 1);
    }

    public void setSucceed() {
        if (this.getViewKey() == ViewKeys.Client_InitAc) {
            mProView.setOk("控制端启动完成", false);
        }else {
            mProView.setOk("设备启动完成",  false);
        }

        faceCommCallBackOK.callBack(status + 1);
    }
}
