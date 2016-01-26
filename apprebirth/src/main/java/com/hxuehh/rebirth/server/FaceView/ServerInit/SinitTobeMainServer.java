package com.hxuehh.rebirth.server.FaceView.ServerInit;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitTobeMainServer extends FaceGetMainViewImp implements ProViewForStep {

    public SinitTobeMainServer(FaceContextWrapImp context, int Viewkey) {
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
        IntentChangeManger.startService(getFaceContext(),IntentChangeManger.Main_Service_UDPreviceback_TCPLongLink);
    }

    @Override
    public void onErr() {
        mProView.setErrorInfo("启动中心服务失败，请清理Android进程重试");
    }

    public  void setErrType(int key){
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
        mProView.setOk(info,false);
        faceCommCallBackOK.callBack(status + 1);
    }



    public void setSucceed() {
        mProView.setOk("已经启动中心服务",false);
        faceCommCallBackOK.callBack(status+1);
    }
}
