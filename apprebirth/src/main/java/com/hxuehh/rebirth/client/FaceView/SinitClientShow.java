package com.hxuehh.rebirth.client.FaceView;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.rebirth.R;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.client.service.mappingDeviceInfo.MappingDeviceInfoManager;
import com.hxuehh.rebirth.server.FaceView.ServerInit.ProViewForStep;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitClientShow extends FaceGetMainViewImp implements ProViewForStep {

    public SinitClientShow(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    private ProView mProView;
    LayoutInflater inflater;
    ViewGroup mainViewMain;

    @Override
    protected void initView() {
        inflater = getFaceContext().getLayoutInflater();
        mainView = View.inflate(getFaceContext(), R.layout.select_init_backg, null);
        mProView = new ProView(getFaceContext());
        ViewKeys.addIntoLin(mainView.findViewById(R.id.main_view), mProView.getMainView(), getFaceContext());
        mainViewMain = (ViewGroup) mainView.findViewById(R.id.main_view_2);
    }


    @Override
    public int setStatus(int status) {
        return status;
    }

    String info;


    @Override
    public void setLoadingView(String info) {
        this.info = info;
        mProView.setLoadingName(info);
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_ClientService();
        if (mClientService_TCPLongLink_ == null) {
            mProView.setErrorInfo("链接服务不存在");
        } else {
            try {
                getDevices(mClientService_TCPLongLink_);
            } catch (FaceException e) {
                e.printStackTrace();
                mProView.setErrorInfo(e.getMessage() + "");
            }
        }
    }


    FaceCommCallBack mFaceCommCallBackForGetDevice = new FaceCommCallBack() {
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
                                showDevicesList(oo);
                            }
                        });

                    }
                }
            }
            return false;
        }
    };
    List devices=new ArrayList();

    View.OnClickListener mViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int kk = (Integer) (v.getTag());
            if (devices != null && devices.size() > kk) {
                Object oo = devices.get(kk);
                if (oo != null) {
                    try {
                        ((DeviceInfo)oo).OnItemClickListener(getFaceContext(),v,kk,devices,null,null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        mProView.setErrorInfo("" + e.getMessage());
                    }
                }
            }
        }
    };


    private void showDevicesList(Object oo) {
        mProView.gone_all();
        MidMessage mMidMessage = (MidMessage) oo;
        List devicesOO = (List) mMidMessage.getByKey(MidMessage.Key_All_Device_info);
        if (devicesOO == null || devicesOO.size() == 0) {
            mProView.setErrorInfo("没有在服务器端找到注册的device设备，请检查设备连接");
        } else {
            int i = 0;
            mainViewMain.removeAllViews();
            this.devices.clear();
            for (Object oode : devicesOO) {
                try {
                    JsonElement mDeviceInfoJson = new Gson().toJsonTree(oode);
                    DeviceInfo minfo = new Gson().fromJson(mDeviceInfoJson, DeviceInfo.class);
                    this.devices.add(minfo);

                    try {
                        SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.MappingDeviceInfo_ShortCut,BytesClassAidl.To_Me,minfo));
                    }catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }

                    View view = minfo.getView(getFaceContext(), i, null, mainViewMain, inflater, getViewKey(), null);
                    view.setTag(i);
                    view.setOnClickListener(mViewOnClickListener);
                    ViewKeys.addIntoLin(mainViewMain, view, getFaceContext());
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    mProView.setErrorInfo("获取到" + devicesOO.size() + ",展示出错");
                    break;
                }
            }


            MappingDeviceInfoManager.getInstance().setNowDeviceInfo( this.devices);

        }
    }


    void getDevices(ClientService_TCPLongLink_ mClientService_TCPLongLink_) throws FaceException {
        MidMessageOrder_2 mid = new MidMessageOrder_2(MidMessageCMDKeys.MidMessageCMD_Service_Info);
        mid.setmFaceCommCallBack(mFaceCommCallBackForGetDevice);
        mClientService_TCPLongLink_.sendSyncAddCache(mid);
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
        mProView.setOk(info,  false);
//        faceCommCallBackOK.callBack(status + 1);
    }


}
