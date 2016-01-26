package com.hxuehh.rebirth.client.service;

import android.content.Context;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.client.service.mappingDeviceInfo.MappingDeviceInfoManager;
import com.hxuehh.rebirth.device.DeviceCapacityBuilder;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler_Imp;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.SerializeUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

/**
 * Created by suwg on 2015/8/16.
 */


public class MidMessageHandler_Client extends MidMessageHandler_Imp {

    Context mCon;

    public MidMessageHandler_Client(MidMessageHandler father, Context mCon) {
        super(father);
        this.mCon = mCon;
    }


    @Override
    public MidMessageBack_2 handlerMessage(MidMessageOrder_2 midMessage) throws FaceException {
        super.handlerMessage(midMessage);
        boolean is = false;
        MidMessageBack_2 mMidMessageRess333 = new MidMessageBack_2(midMessage);

        switch (midMessage.getCmd()) {
            case MidMessageCMDKeys.MidMessageCMD_Device_Info://测试
                is = true;
                mMidMessageRess333.putKeyValue(MidMessage.Key_All_Device_info, DeviceCapacityBuilder.getInstance().getmDeviceCapacitysUsed());
                break;

            case MidMessageCMDKeys.MidMessageCMD_Client_DeviceCapacity_Change://分享变化
            {
                is = true;
                byte[] b = midMessage.getBytes();//使用
                if (b == null || b.length <= 0) new FaceException("没有传递参数");
                Object oo = SerializeUtil.unserialize(b);
                if (oo == null) throw new FaceException("传递参数出错,可能是版本不兼容");
                if (!(oo instanceof DeviceCapacityBase)) throw new FaceException("传递参数格式出错");
                DeviceCapacityBase mDeviceCapacityBase = (DeviceCapacityBase) oo;
                String UUID = (String) midMessage.getByKey(MidMessage.Key_DeviceID);
                DeviceInfo mDeviceInfo = MappingDeviceInfoManager.getInstance().getDeviceInfoByUUID(UUID);
                if (mDeviceInfo != null) {
                    mDeviceInfo.setOneDeviceCapacity(mDeviceCapacityBase);
                }
            }
                break;

            case MidMessageCMDKeys.MidMessageCMD_Client_DeviceInfo_Change://分享变化
            {
                is = true;
                byte[] b = midMessage.getBytes();//使用
                if (b == null || b.length <= 0) new FaceException("没有传递参数");
                Object oo = SerializeUtil.unserialize(b);
                if (oo == null) throw new FaceException("传递参数出错,可能是版本不兼容");
                if (!(oo instanceof DeviceInfo)) throw new FaceException("传递参数格式出错");
                DeviceInfo mDeviceCapacityBase = (DeviceInfo) oo;
                MappingDeviceInfoManager.getInstance().setOneDeviceInfo(mDeviceCapacityBase);
            }
                break;
        }

        if (is)
            return mMidMessageRess333;
        return null;
    }


    private void startService() {
        String ip = SharedPreferencesUtils.getString(SharedPreferencesKeys.main_Server_ip);
        if (StringUtil.isEmpty(ip)) {
            ip = UDPTCPkeys.selfIp;
        }
        IntentChangeManger.bindServiceToApp(mCon, IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink, new BytesClass(ip));
    }

    private void endService() {
        IntentChangeManger.unbindServiceToApp(IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
        IntentChangeManger.stopService(mCon, IntentChangeManger.Device_Service_UDPreviceback_TCPLongLink);
    }


    @Override
    public boolean isCanConsume(MidMessageOrder_2 midMessage) {
        int kk = midMessage.getCmd();
        return MidMessageCMDKeys.isClientCom(kk);
    }
}
