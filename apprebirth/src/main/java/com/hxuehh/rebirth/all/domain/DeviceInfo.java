package com.hxuehh.rebirth.all.domain;

import android.app.Activity;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.AdapterViewOnItemClickListener;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.InViewGroupWithAdaper;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.all.domain.Interfaces.TimeOut;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.service.DeviceService_TCPLongLink_;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_Err_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.ZMQClient;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.SerializeUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suwg on 2015/8/16.
 */
public class DeviceInfo implements TimeOut, InViewGroupWithAdaper, AdapterViewOnItemClickListener, Serializable {

    transient static DeviceInfo mDeviceInfo;

    private DeviceInfo() {

    }

    public static synchronized DeviceInfo getInstens() {
        if (mDeviceInfo == null) {
            mDeviceInfo = new DeviceInfo();
            mDeviceInfo.getInfo();
        }
        return mDeviceInfo;
    }


    String brand;
    String device;
    String model;
    String cpuABI;
    int SDKINT;
    String SU_UUID;
    String alias;
    String ip;


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSU_UUID() {
        return SU_UUID;
    }

    public void setSU_UUID(String SU_UUID) {
        this.SU_UUID = SU_UUID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCpuABI() {
        return cpuABI;
    }

    public void setCpuABI(String cpuABI) {
        this.cpuABI = cpuABI;
    }

    public int getSDKINT() {
        return SDKINT;
    }

    public void setSDKINT(int SDKINT) {
        this.SDKINT = SDKINT;
    }

    public String getIp() {
        return ip;
    }

    public void setIp() {
        this.ip = DeviceUtil.getWifiIP();
    }


    void getInfo() {
        this.brand = DeviceUtil.getBrand();
        this.device = DeviceUtil.getimei();
        this.model = DeviceUtil.getModel();
        this.cpuABI = DeviceUtil.getCPU_ABI();
        this.SDKINT = DeviceUtil.getSDKINT();
        this.SU_UUID = SharedPreferencesUtils.getString(SharedPreferencesKeys.device_UUID);
        if (StringUtil.isEmpty(SU_UUID)) {
            this.SU_UUID = this.brand + "_" + this.device + "_" + new Date().getTime() + "_" + java.util.UUID.randomUUID();
            SharedPreferencesUtils.putString(SharedPreferencesKeys.device_UUID, this.SU_UUID);
        }
        setIp();
    }


//    =========================客户端和被控制的device端

    public static final int Type_Device = 1;
    public static final int Type_Client = 2;
    int linkType = Type_Device;

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }


    //    =====================================主要是使用在客户端和被控制的device端
    transient ZMQClient zmqClient;
    //    主服务链接 监听
    transient FaceCommCallBack faceCommCallBackForLinkstatus = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if ((Boolean) t[0]) {
                if(isLinked)return false;
                Su.log("连接到了");
                isLinked = true;
                return true;
            } else {
                if(!isLinked)return false;
                Su.log("发现服务到device client链接出现问题");
                isLinked = false;
                return true;
            }

        }
    };

    transient   boolean isLinked=true;

    private String getShowName() {
        return this.brand + (this.getAlias() == null ? "" : "_" + this.getAlias());
    }


    public MidMessageBack_2 sendForRes(MidMessageOrder_2 midMessage) {
        if (zmqClient == null || !isLinked) {
            close();
            getZmqClient();
        }
        try {
            if (DevRunningTime.isSendHeartbeat) {
                if (!isLinked || isTimeOut()) {//第一检测连接；第二是查看心跳是不是正常
                    return new MidMessageBack_Err_3(getShowName() + "设备失联，请检查" + "设备端是否启动APP，联网情况等", midMessage);
                }
            }
            return zmqClient.send(midMessage);
        } catch (FaceException e) {
            e.printStackTrace();
            return new MidMessageBack_Err_3(e.getMessage(), midMessage);
        }
    }


    private void getZmqClient() {
        this.zmqClient = new ZMQClient(getIp(), getTypeService()== AbService_TCPLongLink_.AbService_TCPLongLink_Device?UDPTCPkeys.DeviceTCPPort:UDPTCPkeys.ClientTCPPort, getSU_UUID(), true, faceCommCallBackForLinkstatus, UDPTCPkeys.TimeOutLong_MAIN_link, null);
    }

    public void close() {
        if (zmqClient != null) zmqClient.close();
        zmqClient = null;
    }


//======================timeout

    transient long startTime;
    transient long endTime;
    transient long actionTime;
    transient long timeOutLong;

    public boolean isTimeOut() {//检测心跳是不是超时
        if (timeOutLong <= 0) return false;//没有设置时间则是不超时
        return actionTime - startTime > timeOutLong;
    }

    public boolean doTimeOut() {
        return true;
    }

    public long getTimeOutLong() {
        return timeOutLong;
    }

    public void setTimeOutLong(long timeOutLong) {
        this.timeOutLong = timeOutLong;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        this.endTime = new Date().getTime();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        this.startTime = new Date().getTime();
    }

    public long getActionTime() {
        return actionTime;
    }

    public void setActionTime() {
        this.actionTime = this.startTime = new Date().getTime();
    }

//    ===============================================================

    transient int typeService;

    public int getTypeService() {
        return typeService;
    }

    public void setTypeService(int typeService) {
        this.typeService = typeService;
    }

    public String getShowInfo() {
        return this.brand + " " + this.model + " " + this.SU_UUID;
    }

    @Override
    public View getView(Activity mContext, int i, View view, ViewGroup viewGroup, LayoutInflater inflater, int viewItemKey, Object[] params) throws Exception {
        TextView mTextView = new TextView(mContext);
        mTextView.setText(this.getShowInfo());
        return mTextView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException {
        SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.Provisionality, BytesClassAidl.To_Me, this));
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_show_device_capacitys, false);
    }


    //    =================================================

    protected DeviceService_TCPLongLink_ getLinkShow() {
        DeviceService_TCPLongLink_ mDeviceService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_DeviceService();
        if (mDeviceService_TCPLongLink_ == null) {
            return null;
        }

        if(mDeviceService_TCPLongLink_.getRunStatus()[2]!=null){
            return null;
        }

        return mDeviceService_TCPLongLink_;
    }

    public void notifyStatusChange() {
        DeviceService_TCPLongLink_ mDeviceService_TCPLongLink_=getLinkShow();
        if(mDeviceService_TCPLongLink_==null)return;
        MidMessageOrder_2 mMidMessageOrder_2=new MidMessageOrder_2(MidMessageCMDKeys.MidMessageCMD_Client_DeviceInfo_Change);
        mMidMessageOrder_2.putKeyValue(MidMessage.Key_DeviceToClientNotification_level,MidMessage.Key_DeviceToClientNotification_level_1);
        mMidMessageOrder_2.putKeyValue(MidMessage.Key_DeviceID, DeviceInfo.getInstens().getSU_UUID());
        mMidMessageOrder_2.setBytes(SerializeUtil.serialize(this));
        try {
            mDeviceService_TCPLongLink_.sendSyncAddCache(mMidMessageOrder_2);
        } catch (FaceException e) {
            e.printStackTrace();
        }

    }


//    =================================================

    private transient Map<Integer, DeviceCapacityBase> mDeviceCapacityBaseMap = new HashMap();//只是用在client 一侧
    private  transient List<DeviceCapacityBase> list;

    public List<DeviceCapacityBase> getList() {
        return list;
    }



    public void setNowDeviceCapacityBases(List<DeviceCapacityBase> list) {
        if(list==null||list.size()==0)return;
        mDeviceCapacityBaseMap.clear();
        this.list=list;
        for (DeviceCapacityBase m : list) {
            mDeviceCapacityBaseMap.put(m.getType(), m);
        }
    }

    public DeviceCapacityBase getDeviceCapacityByDeviceCapacityType(int DeviceCapacityBaseType) {
        return mDeviceCapacityBaseMap.get(DeviceCapacityBaseType);
    }

    public DeviceCapacityBase setOneDeviceCapacity(DeviceCapacityBase mDeviceCapacityBase) {

        DeviceCapacityBase mDeviceCapacityBaseOld=mDeviceCapacityBaseMap.put(mDeviceCapacityBase.getType(),mDeviceCapacityBase);
        if(list!=null&&mDeviceCapacityBaseOld!=null){
            int index=list.indexOf(mDeviceCapacityBaseOld);
            if(index>0){
                list.remove(index);
                list.add(index,mDeviceCapacityBase);
            }else{
                list.add(mDeviceCapacityBase);
            }
        }
        return mDeviceCapacityBaseOld;
    }

    public void clearAllCaps() {
        mDeviceCapacityBaseMap.clear();
        list.clear();
        list=null;
    }
}
