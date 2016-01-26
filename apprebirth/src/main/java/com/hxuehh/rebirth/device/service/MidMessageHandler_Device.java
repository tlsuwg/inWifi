package com.hxuehh.rebirth.device.service;

import android.content.Context;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.DeviceCapacityBuilder;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler_Imp;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.SerializeUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.util.Date;
import java.util.List;

/**
 * Created by suwg on 2015/8/16.
 */


public class MidMessageHandler_Device extends MidMessageHandler_Imp {

    Context mCon;

    public MidMessageHandler_Device(MidMessageHandler father, Context mCon) {
        super(father);
        this.mCon = mCon;
    }


    @Override
    public MidMessageBack_2 handlerMessage(MidMessageOrder_2 midMessage) throws FaceException {
        super.handlerMessage(midMessage);
        boolean is = false;
        MidMessageBack_2 mMidMessageRess333 = new MidMessageBack_2(midMessage);
        if (!DeviceCapacityBuilder.getInstance().isInitOK()) throw new FaceException("设备还没有完成初始化");

        switch (midMessage.getCmd()) {
            case MidMessageCMDKeys.MidMessageCMD_Device_ReStatrt: {
                is = true;
                mMidMessageRess333.putKeyValue(MidMessage.Key_BackWarning, "已经重启，请于5秒后重现连接");
                ThreadManager.getInstance().getNewThread("device restart", new Runnable() {
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

            case MidMessageCMDKeys.MidMessageCMD_Device_End:
//                @Face__UnsolvedDesignForDlp  不能操作的API
                is = true;
                mMidMessageRess333.putKeyValue(MidMessage.Key_BackWarning, "已经关闭，请手动开启");
                ThreadManager.getInstance().getNewThread("device end", new Runnable() {
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
            case MidMessageCMDKeys.MidMessageCMD_Device_Info:
                is = true;
                mMidMessageRess333.putKeyValue(MidMessage.Key_All_Device_info, DeviceCapacityBuilder.getInstance().getmDeviceCapacitysSupport());
                break;

            case MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion:
                is = true;
                int DeviceCapacityKey = 0;
                try {
                    Object oo=midMessage.getByKey(MidMessage.Key_DeviceCapaci_ID);
                    if(oo!=null)
                    DeviceCapacityKey = Integer.parseInt((String) oo);
                } catch (Exception e) {
                    throw new FaceException("没有传递功能ID");
                }
                if (DeviceCapacityKey <= 0) throw new FaceException("没有传递功能ID");
                DeviceCapacityBase mDeviceCapacity = DeviceCapacityBuilder.getInstance().getDeviceCapacityByKey(DeviceCapacityKey);
                if (mDeviceCapacity == null) throw new FaceException("没有此功能区间");
                if(!mDeviceCapacity.isUserSettingEnable()) throw new FaceException("功能以及禁止使用");

                byte[] b = midMessage.getBytes();//使用
                if (b == null || b.length <= 0) new FaceException("没有传递参数");
                Object oo = SerializeUtil.unserialize(b);
                if (oo == null) throw new FaceException("传递参数出错,可能是版本不兼容");
                if(!(oo instanceof DeviceCapacityInParameter) ) throw new FaceException("传递参数格式出错");
                DeviceCapacityInParameter mDeviceCapacityInParameter= (DeviceCapacityInParameter) oo;
                mDeviceCapacityInParameter.setTagTime(new Date().getTime());
                DeviceCapacityOutResult_3 mDeviceCapacityOutResult = mDeviceCapacity.doChangeStatus(mDeviceCapacityInParameter);
                mMidMessageRess333=mDeviceCapacityOutResult;
                break;

            case MidMessageCMDKeys.MidMessageCMD_Device_AutoUpdate:

                break;
            case MidMessageCMDKeys.MidMessageCMD_Device_CloseAll: {
                is = true;
                List<DeviceCapacityBase> lists = DeviceCapacityBuilder.getInstance().getmDeviceCapacitysUsed();
                if (lists != null) {
                    for (DeviceCapacityBase m : lists) {
                        m.stop();
                        m.onDestry();
                    }
                }
            }
            break;

            case MidMessageCMDKeys.MidMessageCMD_Device_StopAll: {
                is = true;
                List<DeviceCapacityBase> lists = DeviceCapacityBuilder.getInstance().getmDeviceCapacitysUsed();
                if (lists != null) {
                    for (DeviceCapacityBase m : lists) {
                        m.stop();
                    }
                }
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
        return MidMessageCMDKeys.isDeviceCom(kk);
    }
}
