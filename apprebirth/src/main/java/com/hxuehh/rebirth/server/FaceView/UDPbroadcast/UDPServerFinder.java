package com.hxuehh.rebirth.server.FaceView.UDPbroadcast;

import android.content.Intent;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP.UDPClientSend;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP.UDPClientget;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by suwg on 2015/8/13.
 */
public class UDPServerFinder extends RunningStatus {

    public static final String UDPServerFinder = "su.com.hxuehh.UDPServerFinder";


    private FaceCommCallBack faceCommCallBackForServerHas;
    private UDPClientSend mUDPClientSend;
    private UDPClientget mget;
    int tryTimes;

    public int getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }


    int serviceTyp;

    public UDPServerFinder(int serviceType, FaceCommCallBack faceCommCallBackForServerHas) {
        this.serviceTyp = serviceType;
        this.faceCommCallBackForServerHas = faceCommCallBackForServerHas;
    }


    public void listenGetUDP(boolean b) {
        if (mget == null) {
            ThreadManager.getInstance().getNewThread("listenGetUDP", new Runnable() {
                @Override
                public void run() {
                    getUDP();
                }
            }).start();
        } else {
            send10Times();
            if (b) {
                setStatus(RunningStatus.Running, "尝试连接次数" + getTryTimes());
                SuApplication.getInstance().sendBroadcast(new Intent(UDPServerFinder));
            }

        }
    }

    final FaceCommCallBack getUDPcall = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (t != null && t.length > 0) {
                isFind = true;
                if (faceCommCallBackForServerHas != null) {
                    faceCommCallBackForServerHas.callBack(true, t[0]);
                }
            } else {//代表可以发了  get线程启动了
                send10Times();
            }
            return false;
        }

    };


    private void getUDP() {
        if (mget == null) {
            setStatus(RunningStatus.Init, null);
            if (serviceTyp == AbService_TCPLongLink_.AbService_TCPLongLink_Client || serviceTyp == AbService_TCPLongLink_.AbService_TCPLongLink_Device) {
                mget = new UDPClientget(UDPTCPkeys.Device_Client_broadcastPort, getUDPcall, 10);
            } else {
                mget = new UDPClientget(UDPTCPkeys.Main_broadcastPort, getUDPcall, 10);
            }
        }
        if (mget != null) {
            mget.getForGetAddress();
            setStatus(RunningStatus.Running, null);
        } else {
            setStatus(RunningStatus.InitErr, null);
        }
    }


    boolean isFind = false;

    public void send10Times() {
        tryTimes++;
        ThreadManager.getInstance().submitUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int times = 0;
                while (times++ < 5 && !isFind) {
                    sendUDPNoCoreAny();
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!isFind && faceCommCallBackForServerHas != null) {
                    faceCommCallBackForServerHas.callBack(false);//没有找到
                }

            }
        });
    }


    private void sendUDPNoCoreAny() {
        try {
            if (mUDPClientSend == null)
                mUDPClientSend = new UDPClientSend(UDPTCPkeys.hostbroadcast, 0, UDPTCPkeys.Main_broadcastPort, "broadcast_UDP");
            mUDPClientSend.send(new byte[]{UDPTCPkeys.UDPFindServer}, 1,true);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        setStatus(RunningStatus.End, null);
        if (mget != null) {
            mget.close();
            mget = null;
        }

        if (mUDPClientSend != null) {
            mUDPClientSend.close();
            mUDPClientSend = null;
        }
    }


}
