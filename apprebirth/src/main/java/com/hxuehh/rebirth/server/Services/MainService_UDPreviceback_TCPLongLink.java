package com.hxuehh.rebirth.server.Services;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.app.SuSuperService;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IsOnTop;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.server.Services.handerMessage.MidMessageHandler_Server;
import com.hxuehh.rebirth.server.Services.handerMessage.MidMessageHandler_Through;
import com.hxuehh.rebirth.suMessage.pro.TCPGetServer;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;

import java.util.HashMap;

/**
 * Created by suwg on 2015/8/14.
 */
public class MainService_UDPreviceback_TCPLongLink extends SuSuperService implements IsOnTop {

    public static final String UDPErr = "com.su.UDPreviceback_TCPLongLink_Service.udpErr";
    public static final String TCPErr = "com.su.UDPreviceback_TCPLongLink_Service.tcpErr";
    public static final String TCP_UDP_Succeed = "com.su.UDPreviceback_TCPLongLink_Service.TCP_UDP_Succeed";


    private int status;
    public static final int status_UDP_OK = 1;
    public static final int status_TCP_OK = 2;

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.isOnTop = true;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        doStatus();
    }

    @Override
    public void onDestroy() {
        this.isOnTop = false;
        trueClose();
//        SuNetEvn.getInstance().removeNetChangeListener(faceCommCallBackForNet);
        try {
            SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.MainService_UDPreviceback_TCPLongLink_ISRunning, BytesClassAidl.To_Me);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    UDPServerShower mUDPServerShower;
    private FaceCommCallBack UDPcall = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (t != null && t.length > 0) {//UDP抛异常
                trueClose();
                sendBroadcast(new Intent(UDPErr));
            } else {
                status = status_UDP_OK;
                doStatus();
            }
            return false;
        }
    };


    private FaceCommCallBack TCPcall = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if (t != null && t.length > 0) {//TCP抛异常
                trueClose();
                sendBroadcast(new Intent(TCPErr));
            } else {
                status = status_TCP_OK;
                doStatus();
            }
            return false;
        }
    };

    void listenUDP() {
        if (mUDPServerShower != null) return;
        mUDPServerShower = new UDPServerShower();
        mUDPServerShower.listenGetUDP(UDPcall);
    }

    private TCPGetServer mTCPServer;

    HashMap<String, DeviceInfo> deviceMap = new HashMap<String, DeviceInfo>();
    MidMessageHandler_Through mMidMessageHandler_Through = new MidMessageHandler_Through(null, deviceMap);//转移到其他客户端
    MidMessageHandler_Server mMidMessageHandler_Server = new MidMessageHandler_Server(mMidMessageHandler_Through, deviceMap, this);//服务端控制

    private void serverTcp() {
        if (mTCPServer != null) return;
        mTCPServer = new TCPGetServer("mainserver", 0, mMidMessageHandler_Server);
        mTCPServer.listenTCP(TCPcall);
    }


    private void doStatus() {
        if (this.status == 0) {
            listenUDP();
        } else if (this.status == status_UDP_OK) {
            serverTcp();//已经监听
        } else if (this.status == status_TCP_OK) {
            sendBroadcast(new Intent(TCP_UDP_Succeed));
            try {
                SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.MainService_UDPreviceback_TCPLongLink_ISRunning, BytesClassAidl.To_Me, true));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }




    void trueClose() {
        if (mUDPServerShower != null)
            mUDPServerShower.close();
        mUDPServerShower = null;
        if (mTCPServer != null)
            mTCPServer.close();
        mTCPServer = null;
        status = 0;
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
