package com.hxuehh.rebirth.all.ServiceAb;

import android.content.Intent;
import android.os.IBinder;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.app.SuSuperService;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.server.FaceView.UDPbroadcast.UDPServerFinder;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.TCPGetServer;
import com.hxuehh.rebirth.suMessage.pro.ZMQClient;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.IntentKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.net.InetAddress;
import java.util.Date;

/**
 * Created by suwg on 2015/8/14.
 */


public abstract class AbService_TCPLongLink_ extends SuSuperService implements IsOnTop {


    public static final int AbService_TCPLongLink_Device = 1;
    public static final int AbService_TCPLongLink_Client = 2;


    private int startStatus;

    public static final int status_TCPListen_OK = 1;
    public static final int status_TCP_link_main_OK = 2;//allOK


    public static final int status_mainlinkERR_UDPfindingserver = 3;//发现服务链接问题，重新发现
    public static final int status_mainlinkReResister = 4;//错误之后还要重新注册

    String mainServerIP;

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }



    @Override
    public IBinder onBind(Intent intent) {
        BytesClass mBytesClass = (BytesClass) intent.getSerializableExtra(IntentKeys.obj_ClassKeyByte);
        String ip = (String) mBytesClass.getTrue();
        if (!StringUtil.isEmpty(ip)) {
            boolean isIpchange = !ip.equals(mainServerIP);
            if (isIpchange) {
                mainServerIP = ip;
                closeLink();
                SharedPreferencesUtils.putBoolean(SharedPreferencesKeys.DeviceService_TCPLongLink_Satus + getServiceType(), true);
            }
        }
        doStatus();
        return mMyBinder;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null
                && intent.getAction() != null &&
                intent.getAction().equals(getTCP_heartbeat())) {
            if (!SharedPreferencesUtils.getBoolean(SharedPreferencesKeys.DeviceService_TCPLongLink_Satus + getServiceType())) {
                onServiceDestryedGetAlarm();
                return;
            }
            if (startStatus == status_TCP_link_main_OK) {
                sendHeartbeat();
            } else {
                doStatus();
            }

        }
    }


    @Override
    public void onDestroy() {
        Su.log(this + " onDestroy");
        this.isOnTop = false;
        closeLink();
        SharedPreferencesUtils.putBoolean(SharedPreferencesKeys.DeviceService_TCPLongLink_Satus + getServiceType(), false);
        super.onDestroy();
    }


    public String getRunStatusInfo() {
        String info1 = "监听服务:" + (mTCPServer == null ? "失败" : mTCPServer.toStatusString());
        String info2 = null;
        if (mZMQClientForMain != null) {
            if (mZMQClientForMain.getStatus() == RunningStatus.RunTimeErr) {
                info2 = "中心链接关系:" + mZMQClientForMain.toStatusString() + StringUtil.N + (mUDPServerFinder == null ? "" : ";尝试链接次数：" + mUDPServerFinder.getTryTimes());
            } else {
                info2 = "中心链接关系:" + mZMQClientForMain.toStatusString();
            }
        } else {
            info2 = "中心链接关系:失败";
        }

        return info1 + StringUtil.N + info2;
    }


    public RunningStatus[] getRunStatus() {
        return new RunningStatus[]{mTCPServer, mZMQClientForMain, mUDPServerFinder};
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.isOnTop = true;
    }


    private TCPGetServer mTCPServer;//获取消息
    private ZMQClient mZMQClientForMain;//发送消息
    private UDPServerFinder mUDPServerFinder;//UDP探测


    protected abstract String getTCPListenErr();

    protected abstract String getTCP_heartbeat();

    protected abstract String getTCPLinkMainErr();

    protected abstract String getTCP_Succeed();

    protected abstract String getTCP_ReLinkedOk();

    protected abstract void onAllStartOk();

    protected abstract long getHeartLong();

    public abstract MidMessageHandler getmMidMessageHandler();

    protected abstract void onServiceDestryedGetAlarm();

    public abstract long getGetTCPTimeOutErrTime();

    public abstract int getServiceType();

    protected abstract void onLinkErr();


    private void LinKMainserverLinstenStatus() {
        if (mZMQClientForMain != null) mZMQClientForMain.close();
        mZMQClientForMain = null;

        if(StringUtil.isEmpty(mainServerIP)){
            Su.logE("没有的mainServerIP");
            return;
        }
        mZMQClientForMain = new ZMQClient(mainServerIP, UDPTCPkeys.MainServerTCPPort, getServiceType() + "_" + DeviceInfo.getInstens().getSU_UUID(), false,
                faceCommCallBackForLinkstatus, getGetTCPTimeOutErrTime(), null);
        sendForRegister();
    }

    private void sendForRegister() {
        MidMessageOrder_2 mid = new MidMessageOrder_2(MidMessageCMDKeys.MidMessageCMD_Service_Device_register);
        mid.putKeyValue(MidMessage.Key_Service_typeID, this.getServiceType() + "");
        mid.putKeyValue(MidMessage.Key_Device_info, DeviceInfo.getInstens());
        mid.setmFaceCommCallBack(new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] t) {
                if (t != null && t.length > 0) {
                    Object oo = t[0];
                    if (oo instanceof Exception) {
                        if (startStatus == status_mainlinkReResister) {
                            return false;
                        }
                        sendBroadcast(new Intent(getTCPLinkMainErr()));
                        closeLink();
                        stopSelf();
                    } else if (oo instanceof MidMessage) {
                        heartbeatgetingBack = false;
                        noheartbeatgetingBackTime = 0;
                        lastDateForSendK = (long) t[1];

                        if (startStatus == status_mainlinkReResister || startStatus == status_mainlinkERR_UDPfindingserver) {
                            startStatus = status_TCP_link_main_OK;
                            sendBroadcast(new Intent(getTCP_ReLinkedOk()));
                            Su.log("重新注册完成");
                            mZMQClientForMain.setStatus(RunningStatus.Running, "重新和主服务链接");
                            return false;
                        }

                        Su.log(DeviceInfo.getInstens().getSU_UUID() + "注册成功");
                        startStatus = status_TCP_link_main_OK;
                        doStatus();
                    }
                }
                return false;
            }
        });

        try {
            sendSyncAddCache(mid);
        } catch (FaceException e) {
            e.printStackTrace();
        }

    }


    public void sendSyncAddCache(MidMessageOrder_2 mid) throws FaceException {
        if (mZMQClientForMain != null) {
            mZMQClientForMain.sendSyncAddCache(mid);
        } else {
            if (startStatus == status_mainlinkReResister || startStatus == status_mainlinkERR_UDPfindingserver)
                doStatus();
            throw new FaceException("链接出错");
        }
    }


    private FaceCommCallBack TCPListen = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (t != null && t.length > 0) {//TCP抛异常
                sendBroadcast(new Intent(getTCPListenErr()));
                closeLink();
                stopSelf();
            } else {
                startStatus = status_TCPListen_OK;
                Su.log("监听成功");
                doStatus();
            }
            return false;
        }
    };

    private void beServer() {
        mTCPServer = new TCPGetServer(DeviceInfo.getInstens().getSU_UUID(), getServiceType(), getmMidMessageHandler());
        mTCPServer.listenTCP(TCPListen);
    }


    @Face_UnsolvedForDlp
    private void doStatus() {
        if (this.startStatus == 0) {
            beServer();//已经监听
        } else if (this.startStatus == status_TCPListen_OK || this.startStatus == status_mainlinkReResister) {
            LinKMainserverLinstenStatus();//已经监听
        } else if (this.startStatus == status_TCP_link_main_OK) {
            sendBroadcast(new Intent(getTCP_Succeed()));
            onAllStartOk();
        } else if (startStatus == status_mainlinkERR_UDPfindingserver) {
            udpFindNewServer();
        }
    }


    int allTimes;
    long startTime;

    protected void sendtest() {
        if (DevRunningTime.isSend100) {
            ThreadManager.getInstance().getNewThread("test send", new Runnable() {
                @Override
                public void run() {
                    {
                        int i = 0;
                        MidMessage midMessage = new MidMessageOrder_2(MidMessageCMDKeys.MidMessageCMD_Service_Device_register);
//                    midMessage.setBytes(new byte[1024 * 1024 * 10]);
                        midMessage.setBytes(new byte[512 * 2]);
                        midMessage.putKeyValue(MidMessage.Key_DeviceID, DeviceInfo.getInstens().getSU_UUID());
                        midMessage.setmFaceCommCallBack(new FaceCommCallBack() {
                            @Override
                            public boolean callBack(Object[] t) {
                                if (t != null && t.length > 0) {
                                    Object oo = t[0];
                                    if (oo instanceof Exception) {

                                    } else if (oo instanceof MidMessage) {
                                        if (allTimes++ == 100) {
                                            long kkkk = new Date().getTime() - startTime;
                                            double time = kkkk / 1000d;
                                            Su.log("use startTime：" + time + "+ sudu:" + 100 * 1024l * 1000 / (kkkk) + "byte" + "  ");
                                        }
                                    }
                                }
                                return false;
                            }
                        });

                        startTime = new Date().getTime();
                        while (i++ < 101) {
                            mZMQClientForMain.sendSyncAddCache(midMessage);
                        }
                    }

                }
            }).start();
        }
    }


    FaceCommCallBack faceCommCallBackForgettingBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (t != null && t.length > 0) {
                Object oo = t[0];
                if (oo instanceof Exception) {

                } else if (oo instanceof MidMessage) {
                    heartbeatgetingBack = false;
                    noheartbeatgetingBackTime = 0;
//                    Su.log("心跳完成");
                    lastDateForSendK = (long) t[1];

                }
            }
            return false;
        }
    };

    private boolean heartbeatgetingBack;//等待数据返回
    private long lastDateForSendK;//最后的 服务器返回的回执时间
    private MidMessageOrder_2 midForHeart;


    public boolean isLastLinkStatus() {//是不是已经超时了
        if (lastDateForSendK == 0) return false;//还木有开始
        return (new Date().getTime() - lastDateForSendK) >= this.getHeartLong() + 10 * 1000;
    }

    int noheartbeatgetingBackTime;//丢弃心跳的次数

    private void sendHeartbeat() {
        if (heartbeatgetingBack || mZMQClientForMain == null) {
            Su.log("心跳没有回执，回退");
            noheartbeatgetingBackTime++;
            return;
        }
        heartbeatgetingBack = true;
        if (midForHeart == null) {
            midForHeart = new MidMessageOrder_2(MidMessageCMDKeys.MidMessageCMD_Service_Device_Heartbeat);
            midForHeart.putKeyValue(MidMessage.Key_DeviceID, DeviceInfo.getInstens().getSU_UUID());
            midForHeart.setmFaceCommCallBack(faceCommCallBackForgettingBack);
        }
        mZMQClientForMain.sendSyncAddCache(midForHeart);
    }


    protected boolean isLinkMain;//
    //    主服务链接 监听
    FaceCommCallBack faceCommCallBackForLinkstatus = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if ((Boolean) t[0]) {
                Su.log("连接到了");
                isLinkMain = true;
//                if (startStatus == status_mainlinkERR_UDPfindingserver) {
//                    if (mUDPServerFinder != null) {
//                        mUDPServerFinder.close();
//                        mUDPServerFinder = null;
//                    }
//                }
            } else {
                Su.log("连接ERR");
                if (startStatus == status_TCPListen_OK) {//初始化 链接状态
                    sendBroadcast(new Intent(getTCPListenErr()));
                    closeLink();
                    stopSelf();
                    return false;
                }


                if (mZMQClientForMain.getStatus() != RunningStatus.RunTimeErr) {
                    mZMQClientForMain.setStatus(RunningStatus.RunTimeErr, "和主服务链接中断");
                }

                startStatus = status_mainlinkERR_UDPfindingserver;//直接去找
                Su.log("发现服务链接问题，重新发现");
                doStatus();

                if (isLinkMain) {
                    onLinkErr();
                    isLinkMain = false;
                }
            }

            return false;
        }
    };


    private void udpFindNewServer() {
        if (mZMQClientForMain != null) mZMQClientForMain.close();
        mZMQClientForMain = null;

        if (mUDPServerFinder == null)
            mUDPServerFinder = new UDPServerFinder(getServiceType(), new FaceCommCallBack() {
                @Override
                public boolean callBack(Object[] t) {
                    boolean isHas = (Boolean) t[0];
                    if (isHas) {
                        if (mUDPServerFinder != null) {
                            mUDPServerFinder.close();
                            mUDPServerFinder = null;
                        }

                        InetAddress mInetAddress = (InetAddress) t[1];
                        final String ip = mInetAddress.getHostAddress();
                        if (!mainServerIP.equals(ip)) {
                            Su.log("server UDP IP没有变化");
                            SharedPreferencesUtils.putString(SharedPreferencesKeys.main_Server_ip, ip);
                            mainServerIP = ip;
                        }
                        startStatus = status_mainlinkReResister;
                        doStatus();

                    } else {
                        Su.log("server UDP 确实找不到了");
                    }

                    return false;
                }
            });
        mUDPServerFinder.listenGetUDP(startStatus == status_mainlinkERR_UDPfindingserver);
    }


    private void closeLink() {
        Su.log(this + " closeLink");
        if (mTCPServer != null)
            mTCPServer.close();

        mTCPServer = null;
        if (mZMQClientForMain != null) mZMQClientForMain.close();
        mZMQClientForMain = null;
        startStatus = 0;
    }

    boolean isOnTop;

    @Override
    public boolean isOnTop() {
        return isOnTop;
    }

    @Override
    public void setOnTop(boolean isOnTop) {
        this.isOnTop = isOnTop;
    }
}
