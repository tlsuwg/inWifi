package com.hxuehh.rebirth.device.faceAc;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.all.FaceView.SinitTobeLinkServer2Status;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.server.FaceView.UDPbroadcast.UDPServerFinder;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

import java.util.List;

/**
 * Created by suwg on 2015/9/11.
 */


public abstract class BaseDeviceCapacityAc_2 extends FaceBaseActivity_1 {
    protected DeviceInfo mDeviceInfo;
    protected DeviceCapacityBase mDeviceCapacity;
    protected ProView mProView;
    private SinitTobeLinkServer2Status mSinitTobeLinkServer2Status;//链接状态

    FaceCommCallBack FaceCommCallBackStatusErr = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
            if (mSinitTobeLinkServer2Status != null)
                mSinitTobeLinkServer2Status.setRunningStatus();
            return false;
        }
    };

    FaceCommCallBack FaceCommCallBackStatusOK = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
            mSinitTobeLinkServer2Status.setRunningStatus();
            findViewById(R.id.main_lin).setVisibility(View.VISIBLE);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.select_title_lin).setVisibility(View.GONE);
                }
            }, 3000);
            return false;
        }
    };


    private String actions[] = new String[]{
            UDPServerFinder.UDPServerFinder,//重连
            ClientService_TCPLongLink_.TCPLinkReLinkedOK,//链接OK
    };


    private FaceCommCallBack faceCommCallBacks[] = new FaceCommCallBack[]{
            FaceCommCallBackStatusErr,
            FaceCommCallBackStatusOK};


    protected FaceCommCallBack mFaceCommCallBackForLoad = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (t != null && t.length > 0) {
                final Object oo = t[0];
                if (oo instanceof Exception) {
                    getFaceContext().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mProView.setErrorInfo(((Exception) oo).getMessage() + "");
                        }
                    });
                } else {
                    if (oo instanceof MidMessage) {
                        getFaceContext().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                ShowBack((MidMessageBack_2) oo);
                            }
                        });
                    }
                }
            }
            return false;
        }
    };



    protected ClientService_TCPLongLink_ getLinkShow() {
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_ClientService();
        if (mClientService_TCPLongLink_ == null) {
            mProView.setErrorInfo("没有存在链接");
            return null;
        }

        if(mClientService_TCPLongLink_.getRunStatus()[2]!=null){
            mProView.setErrorInfo("链接已经出错");
            return null;
        }


        return mClientService_TCPLongLink_;
    }


    private void testHasLinkServerStarted(int status2) {
        if (mSinitTobeLinkServer2Status == null) {
            mSinitTobeLinkServer2Status = new SinitTobeLinkServer2Status(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitTobeLinkServer2Status.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitTobeLinkServer2Status.setStatus(status2);

        }
        mSinitTobeLinkServer2Status.setRunningStatus();
        if (IntentChangeManger.isClientServiceStart()) {
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        if(mDeviceInfo==null)
        try {
            List list = (List) getDeviceInfo(AidlCacheKeys.Provisionality);
            mDeviceInfo = (DeviceInfo) list.get(0);
            mDeviceCapacity = (DeviceCapacityBase) list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mDeviceInfo == null || mDeviceCapacity == null) {
            DialogUtil.showShortToast(SuApplication.getInstance(), "参数不存在，退出");
            finish();
            return;
        }
        setActionReceivers(actions, faceCommCallBacks);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        testHasLinkServerStarted(0);
    }

    private Object getDeviceInfo(int key) {
        try {
            BytesClassAidl mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(key, BytesClassAidl.To_Me);
            if (mBytesClassAidl != null) {
                Object oo = mBytesClassAidl.getTrue();
                return oo;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initView() {
        super.initView();
        initTitle();
        setMainView();
    }

    public abstract void setMainView() ;
    public abstract void doChange(int changeKey) ;
    public abstract MidMessageOrder_2 getCmdForChange(int changeKey) throws FaceException;
    public abstract void ShowBack(MidMessageBack_2 mMidMessageBack) ;

    private void initTitle() {
        TitleView title = new TitleView(getFaceContext());
        title.setTitle(mDeviceInfo.getBrand() + " " + mDeviceInfo.getModel() + " " + mDeviceCapacity.getSelfTypeName());
        ViewKeys.addIntoLin(R.id.title_lin, title.getMainView(), getFaceContext());
    }

    @Override
    public void addListeners() {
        super.addListeners();
    }


}
