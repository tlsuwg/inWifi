package com.hxuehh.rebirth.server.Services.handerMessage;

import com.hxuehh.appCore.develop.Face__UnsolvedDesignForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_Err_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.MidMessageNotHandledException;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler_Imp;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suwg on 2015/8/16.
 */


public class MidMessageHandler_Through extends MidMessageHandler_Imp {


//    ExecutorService pool = Executors.newFixedThreadPool(8);
    HashMap<String, DeviceInfo> deviceMap;

    public MidMessageHandler_Through(MidMessageHandler father, HashMap<String, DeviceInfo> deviceMap) {
        super(father);
        this.deviceMap = deviceMap;
    }

    @Override
    public boolean isCanConsume(MidMessageOrder_2 midMessage) {
        return MidMessageCMDKeys.isDeviceCom(midMessage.getCmd())||MidMessageCMDKeys.isClientCom(midMessage.getCmd());//可能是给
    }


    Timer mTimer = new Timer();

    @Face__UnsolvedDesignForDlp
    @Override
    public MidMessageBack_2 handlerMessage(final MidMessageOrder_2 midMessage) throws FaceException {
        super.handlerMessage(midMessage);

        {//如果是 设备端分发消息
            Object le = midMessage.getByKey(MidMessage.Key_DeviceToClientNotification_level);
            if (le != null) {
                double led= (double) le;
                int lev = (int) led;
                if (lev == MidMessage.Key_DeviceToClientNotification_level_1) {

                } else if (lev == MidMessage.Key_DeviceToClientNotification_level_2) {//需要设计必须知道的消息  通过小米发push

                }

                Iterator<DeviceInfo> it = deviceMap.values().iterator();
                while (it.hasNext()) {
                    DeviceInfo mDeviceInfo = it.next();
                    if (mDeviceInfo.getTypeService() == AbService_TCPLongLink_.AbService_TCPLongLink_Client) {
                        sendToOneGetResBack(mDeviceInfo, midMessage);
                    }
                }

                return new MidMessageBack_2(midMessage);
            }
        }
//        ========================
        {// 转走吧
            Object device_or_client = midMessage.getByKey(MidMessage.Key_To_Which_DeviceID);
            if (device_or_client == null)
                throw new MidMessageNotHandledException("没有该设备映射，已经注销，或没有主动链接");
            final DeviceInfo mDeviceInfo = deviceMap.get(device_or_client + "_1");
            if (mDeviceInfo == null)
                throw new MidMessageNotHandledException("没有该设备映射，已经注销，或没有主动链接");
//        掉线处理呢
            Su.log("2转走..." + midMessage.toString());
            sendToOneGetResBack(mDeviceInfo, midMessage);
            return midMessage.getRes();
        }
    }


//    发送给某给人
    private void sendToOneGetResBack(final DeviceInfo mDeviceInfo, final MidMessageOrder_2 midMessage) {
        final TimerTask mTimerTask = new TimerTask() {//清理延时业务
            @Override
            public void run() {
                Su.log("等待结束，木有结果？");
                thisNotify(midMessage);
            }
        };
        mTimer.schedule(mTimerTask, mDeviceInfo.getTimeOutLong());//清理延时业务

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                MidMessageBack_2 mMidMessageBack_2 = null;
                try {
                    mMidMessageBack_2 = mDeviceInfo.sendForRes(midMessage);
                } catch (Exception e) {
                    mMidMessageBack_2 = new MidMessageBack_Err_3("设备失联，请检查设备端是否启动APP，联网情况等", midMessage);
                }
                midMessage.setRes(mMidMessageBack_2);
                mTimerTask.cancel();
                thisNotify(midMessage);
                Su.log("处理完成");
            }
        };
//        pool.submit(mRunnable);
        ThreadManager.getInstance().getNewThread("转移消息", mRunnable).start();

        thisWait(midMessage);//锁死当前进程

        if (midMessage.getRes() == null) {
            midMessage.setIsDiscard(true);
            MidMessageBack_2 mMidMessageBack_2 =  new MidMessageBack_Err_3("处理超时——设备失联，请检查设备端是否启动APP，联网情况等", midMessage);
            midMessage.setRes(mMidMessageBack_2);
        }
        Su.log("2转走...res" + midMessage.getRes().toString());
    }



    void thisNotify(MidMessageOrder_2 midMessage) {
        synchronized (midMessage) {
            midMessage.notify();
        }
    }

    void thisWait(MidMessageOrder_2 midMessage) {
        try {
            synchronized (midMessage) {
                midMessage.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
