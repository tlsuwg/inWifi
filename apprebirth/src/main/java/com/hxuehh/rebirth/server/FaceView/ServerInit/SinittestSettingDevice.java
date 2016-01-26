package com.hxuehh.rebirth.server.FaceView.ServerInit;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinittestSettingDevice extends FaceGetMainViewImp implements ProViewForStep {

    public SinittestSettingDevice(FaceContextWrapImp context, int Viewkey) {
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
        ThreadManager.getInstance().submitUIThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                do(){
//
//                }

                mProView.setOk("设备初始化完成",false);
                getFaceContext().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        faceCommCallBackOK.callBack(status+1);
                    }
                });

            }
        });


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
