package com.hxuehh.rebirth.client.FaceView;

import com.hxuehh.rebirth.R;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.client.service.mappingDeviceInfo.MappingDeviceInfoManager;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.imp.AgencyCapacity;
import com.hxuehh.rebirth.server.FaceView.ServerInit.ProViewForStep;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrderForDevice_3;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suwg on 2015/8/13.
 */
public class SinitCapsShow extends FaceGetMainViewImp implements ProViewForStep {

    DeviceInfo su_uuid;

    public SinitCapsShow(FaceContextWrapImp context, int Viewkey, DeviceInfo su_uuid) {
        super(context, Viewkey);
        this.su_uuid = su_uuid;
        initView();
    }

    ProView mProView;
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
            DeviceInfo mDeviceInfo = MappingDeviceInfoManager.getInstance().getDeviceInfoByUUID(su_uuid.getSU_UUID());
            if (mDeviceInfo != null && mDeviceInfo.getList() != null && mDeviceInfo.getList().size() > 0) {
                showCapsListView();
            } else {
                try {
                    getCapacity(mClientService_TCPLongLink_);
                } catch (FaceException e) {
                    e.printStackTrace();
                    mProView.setErrorInfo(e.getMessage() + "");
                }
            }
        }
    }


    FaceCommCallBack mFaceCommCallBackForGetCaps = new FaceCommCallBack() {
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
                                showCapsList(oo);
                            }
                        });
                    }
                }
            }
            return false;
        }
    };


    View.OnClickListener mViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int kk = (Integer) (v.getTag());

            DeviceInfo mDeviceInfo = MappingDeviceInfoManager.getInstance().getDeviceInfoByUUID(su_uuid.getSU_UUID());
            if (mDeviceInfo == null) {
                DialogUtil.showLongToast(getFaceContext(), "重新获取列表");

            }

            DeviceCapacityBase oo = mDeviceInfo.getDeviceCapacityByDeviceCapacityType(kk);
            if (oo != null) {
                try {
                    oo.OnItemClickListener(getFaceContext(), v, kk, mDeviceInfo.getList(), null, new Object[]{su_uuid});
                } catch (RemoteException e) {
                    e.printStackTrace();
                    DialogUtil.showLongToast(getFaceContext(), "程序异常");

                } catch (FaceException e) {
                    e.printStackTrace();
                    DialogUtil.showLongToast(getFaceContext(), e.getMessage());

                }
            }else {
                DialogUtil.showLongToast(getFaceContext(), "重新获取列表");
            }
        }
    };


    private void showCapsList(Object oo) {
        mProView.gone_all();
        MidMessage mMidMessage = (MidMessage) oo;
        Object err = mMidMessage.getByKey(MidMessage.Key_ErrInfo);
        if (err != null) {
            mProView.setErrorInfo(err.toString() + "");
            return;
        }
        List caps = new ArrayList();
        List list = (List) mMidMessage.getByKey(MidMessage.Key_All_Device_info);
        if (list == null || list.size() == 0) {
            mProView.setErrorInfo("没有在服务器端找到注册的device设备，请检查设备连接");
        } else {

            mainViewMain.removeAllViews();
            caps.clear();
            for (Object oode : list) {
                try {
                    JsonElement mDeviceInfoJson = new Gson().toJsonTree(oode);
                    AgencyCapacity baseInfo = new Gson().fromJson(mDeviceInfoJson, AgencyCapacity.class);
                    int type = baseInfo.getType();
                    DeviceCapacityBase minfo = null;
                    try {
                        Class cla = DeviceCapacityBase.getClass(type);
                        minfo = (DeviceCapacityBase) new Gson().fromJson(mDeviceInfoJson, cla);
                        if (!minfo.isShowInClient()) {
                            continue;
                        }
                        caps.add(minfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProView.setErrorInfo("获取到" + list.size() + ",展示出错");
                    break;
                }
            }


            DeviceInfo mDeviceInfo = MappingDeviceInfoManager.getInstance().getDeviceInfoByUUID(su_uuid.getSU_UUID());
            if (mDeviceInfo != null) {
                mDeviceInfo.setNowDeviceCapacityBases(caps);
            }
            showCapsListView();
        }
    }

    private void showCapsListView() {
        DeviceInfo mDeviceInfo = MappingDeviceInfoManager.getInstance().getDeviceInfoByUUID(su_uuid.getSU_UUID());
        List<DeviceCapacityBase> list = mDeviceInfo.getList();
        if(list==null)return;
        mainViewMain.removeAllViews();
        int i = 0;
        for (DeviceCapacityBase minfo : list) {
            View view = null;
            try {
                view = minfo.getView(getFaceContext(), i, null, mainViewMain, inflater, getViewKey(), null);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            view.setTag(minfo.getType());
            view.setOnClickListener(mViewOnClickListener);
            ViewKeys.addIntoLin(mainViewMain, view, getFaceContext());
            i++;
        }

    }


    void getCapacity(ClientService_TCPLongLink_ mClientService_TCPLongLink_) throws FaceException {
        MidMessageOrderForDevice_3 mid = new MidMessageOrderForDevice_3(MidMessageCMDKeys.MidMessageCMD_Device_Info, su_uuid.getSU_UUID());
        mid.setmFaceCommCallBack(mFaceCommCallBackForGetCaps);
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
        mProView.setOk(info, false);
//        faceCommCallBackOK.callBack(status + 1);
    }


    public void onResume() {
        showCapsListView();
    }
}
