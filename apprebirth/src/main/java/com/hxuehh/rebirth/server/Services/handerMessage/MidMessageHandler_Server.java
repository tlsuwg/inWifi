package com.hxuehh.rebirth.server.Services.handerMessage;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.MidMessageNotHandledException;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler_Imp;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by suwg on 2015/8/16.
 */


public class MidMessageHandler_Server extends MidMessageHandler_Imp {

    private Context mContext;
    HashMap<String, DeviceInfo> deviceMap;

    public MidMessageHandler_Server(MidMessageHandler father, HashMap<String, DeviceInfo> deviceMap, Context mContext) {
        super(father);
        this.deviceMap = deviceMap;
        this.mContext = mContext;
    }

    @Override
    public MidMessageBack_2 handlerMessage(final MidMessageOrder_2 midMessage) throws FaceException {

        MidMessageBack_2 mMidMessageRess333 =   super.handlerMessage(midMessage);
        if(mMidMessageRess333!=null){
            return mMidMessageRess333;
        }


        boolean is = false;
         mMidMessageRess333 = new MidMessageBack_2(midMessage);
        switch (midMessage.getCmd()) {
            case MidMessageCMDKeys.MidMessageCMD_Service_ReStatrt: {
                is = true;
                mMidMessageRess333.putKeyValue(MidMessage.Key_BackWarning, "已经重启，请于5秒后重现连接");

                ThreadManager.getInstance().getNewThread("server restart", new Runnable() {
                    @Override
                    public void run() {
//                        消息发出
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        endService();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startService();
                    }
                }).start();
            }
            break;

            case MidMessageCMDKeys.MidMessageCMD_Service_End:
//                @Face__UnsolvedDesignForDlp  不能操作的API
                is = true;
                mMidMessageRess333.putKeyValue(MidMessage.Key_BackWarning, "已经关闭，请手动开启");

                ThreadManager.getInstance().getNewThread("server end", new Runnable() {
                    @Override
                    public void run() {
//                        消息发出
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        endService();
                    }
                }).start();

                break;
            case MidMessageCMDKeys.MidMessageCMD_Service_Info://获取所有的信息
                is = true;
                List list = new ArrayList();
                if (deviceMap.size() > 0) {
                    Iterator<DeviceInfo> it = deviceMap.values().iterator();
                    while (it.hasNext()) {
                        DeviceInfo de = it.next();
                        if (de.getTypeService() == AbService_TCPLongLink_.AbService_TCPLongLink_Device) {
                            list.add(de);
                        }
                    }
                }
                if (list.size() > 0)
                    mMidMessageRess333.putKeyValue(MidMessage.Key_All_Device_info, list);
                else
                    mMidMessageRess333.putKeyValue(MidMessage.Key_All_Device_info, null);

                break;
            case MidMessageCMDKeys.MidMessageCMD_Service_Device_register: {//注册
                is = true;
                Object oo = midMessage.getByKey(MidMessage.Key_Device_info);
                if (oo == null) throw new MidMessageNotHandledException("没有传递来DeviceInfo");
                JsonElement mDeviceInfoJson = new Gson().toJsonTree(oo);
                final DeviceInfo mDeviceInfo = new Gson().fromJson(mDeviceInfoJson, DeviceInfo.class);
                DeviceInfo old = deviceMap.remove(mDeviceInfo.getSU_UUID());
                if (old != null) {
                    old.close();
                }
                mDeviceInfo.setStartTime();
                mDeviceInfo.setActionTime();
                try {
                    int type = Integer.parseInt((String) midMessage.getByKey(MidMessage.Key_Service_typeID));
                    mDeviceInfo.setTypeService(type);
                    if(type==AbService_TCPLongLink_.AbService_TCPLongLink_Client){
                        mDeviceInfo.setTimeOutLong(UDPTCPkeys.Check_TimeOutLong_Client);
                    }else if(type==AbService_TCPLongLink_.AbService_TCPLongLink_Device){
                        mDeviceInfo.setTimeOutLong(UDPTCPkeys.Check_TimeOutLong_Device);
                    }
                } catch (Exception e) {

                }

                deviceMap.put(mDeviceInfo.getSU_UUID() + "_" + mDeviceInfo.getTypeService(), mDeviceInfo);
                if (!StringUtil.isEmpty(mDeviceInfo.getAlias())) {
                    deviceMap.put(mDeviceInfo.getAlias(), mDeviceInfo);
                }
                mMidMessageRess333.setOK();

//                ThreadManager.getInstance().getNewThread("registerBack", new Runnable() {
//                    @Override
//                    public void run() {
//                        mDeviceInfo.sendForRes(midMessage);
//                    }
//                }).start();

            }
            break;
            case MidMessageCMDKeys.MidMessageCMD_Service_Device_Heartbeat: {
                is = true;
                Object oo1 = midMessage.getByKey(MidMessage.Key_DeviceID);
                if (oo1 == null) throw new MidMessageNotHandledException("没有获取到deviceID");
                DeviceInfo mDeviceInfo = deviceMap.get(oo1);
                if (mDeviceInfo == null)
                    throw new MidMessageNotHandledException("没有发现本地的DeviceInfo" + oo1);
                mDeviceInfo.setActionTime();
                mMidMessageRess333.setOK();
            }
            break;
        }

        if (is)
            return mMidMessageRess333;
        return null;
    }

    private void startService() {
        IntentChangeManger.startService(mContext, IntentChangeManger.Main_Service_UDPreviceback_TCPLongLink);
    }

    private void endService() {
        IntentChangeManger.stopService(mContext, IntentChangeManger.Main_Service_UDPreviceback_TCPLongLink);
    }


    @Override
    public boolean isCanConsume(MidMessageOrder_2 midMessage) {
        int kk = midMessage.getCmd();
        return MidMessageCMDKeys.isServerCom(kk);
    }
}
