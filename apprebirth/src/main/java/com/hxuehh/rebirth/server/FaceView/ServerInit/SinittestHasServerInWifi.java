package com.hxuehh.rebirth.server.FaceView.ServerInit;

import com.hxuehh.rebirth.R;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.server.FaceView.UDPbroadcast.UDPServerFinder;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

import java.net.InetAddress;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinittestHasServerInWifi extends FaceGetMainViewImp implements ProViewForStep {

    public SinittestHasServerInWifi(FaceContextWrapImp context, int Viewkey) {
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


    UDPServerFinder mUDPServerFinder;
    @Override
    public void setLoadingView(String info) {
        mProView.setLoadingName(info);

        if(mUDPServerFinder==null)
         mUDPServerFinder=new UDPServerFinder(AbService_TCPLongLink_.AbService_TCPLongLink_Device, new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] t) {
                mUDPServerFinder.close();
                boolean isHas=(Boolean)t[0];
                if(isHas){
                    Object oo=t[1];
                    if(oo instanceof Exception) {
                        getFaceContext().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mProView.setErrorInfo("发现异常，请清理全部进程之后重试！");
                            }
                        });
                    }else if(oo instanceof InetAddress){
                        InetAddress mInetAddress=(InetAddress)oo;
                        final String ip=mInetAddress.getHostAddress();
                        SharedPreferencesUtils.putString(SharedPreferencesKeys.main_Server_ip,ip);
                        getFaceContext().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mProView.setOk("发现wifi下主机"+ip,false);
                                faceCommCallBackOK.callBack(status+3);
                            }
                        });
                    }
                }else{
                    mProView.setOk("检测完成，添加服务",false);
                    getFaceContext().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            faceCommCallBackOK.callBack(status+1);
                        }
                    });
                }
                return false;
            }
        });

        mUDPServerFinder.listenGetUDP(false);
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
