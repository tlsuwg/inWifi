package com.hxuehh.rebirth.server.Services;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP.UDPClientget;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

/**
 * Created by suwg on 2015/8/13.
 */

//一直等待着被发现，把自己暴露出去
public class UDPServerShower {

    private UDPClientget mget;

    public void listenGetUDP(final FaceCommCallBack faceCommCallBackERR) {
        ThreadManager.getInstance().getNewThread("get UDP send back",new Runnable() {
            @Override
            public void run() {
                getUDP(faceCommCallBackERR);
            }
        }).start();

    }


    private void getUDP(FaceCommCallBack faceCommCallBackERR) {
            if (mget == null) {
                mget = new UDPClientget(UDPTCPkeys.Main_broadcastPort, faceCommCallBackERR, 10);
            }
            byte[] bs=new byte[]{UDPTCPkeys.UDPFindServerOK};
            mget.getForSendBack(bs);
    }


    public void close() {
        if(mget!=null)
        mget.close();
    }
}
