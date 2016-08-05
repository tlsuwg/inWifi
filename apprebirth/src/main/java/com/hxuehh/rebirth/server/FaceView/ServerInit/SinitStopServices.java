package com.hxuehh.rebirth.server.FaceView.ServerInit;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitStopServices extends FaceGetMainViewImp implements ProViewForStep {

    public SinitStopServices(FaceContextWrapImp context, int Viewkey) {
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
        boolean oo1 = IntentChangeManger.isDevServiceStart(),
                oo2 = IntentChangeManger.isMainServiceStart(),
                ooclient = IntentChangeManger.isClientServiceStart();

        boolean isCloseServerDev = (getViewKey() == ViewKeys.Client_InitAc) ? !DevRunningTime.isCanDevServerClientInThis : true;
        if (isCloseServerDev) {//开发模式不关闭
            if (oo2)
                IntentChangeManger.stopService(getFaceContext(), IntentChangeManger.Main_Service_UDPreviceback_TCPLongLink);
            if (oo1) {
                IntentChangeManger.unbindServiceToApp(IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
                IntentChangeManger.stopService(getFaceContext(), IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
            }
        }

        if (ooclient){
            IntentChangeManger.unbindServiceToApp(IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink);
            IntentChangeManger.stopService(getFaceContext(), IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink);
        }


        getFaceContext().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProView.setOk("清理完成",  false);
                faceCommCallBackOK.callBack(status + 1);
            }
        }, (!oo2 && !oo1 ? 500 : 3000));
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


}
