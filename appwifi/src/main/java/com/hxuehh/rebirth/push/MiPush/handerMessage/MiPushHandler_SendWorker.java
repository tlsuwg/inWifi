package com.hxuehh.rebirth.push.MiPush.handerMessage;

import com.hxuehh.appCore.develop.Face__UnsolvedDesignForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceProcess.ConsumeRun;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_Err_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessage_OrderBack_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_P2PErr_4;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageP2POrder_3;
import com.hxuehh.rebirth.suMessage.pro.ZMQClient;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by suwg on 2015/9/28.
 */

//两个功能，一个是mi order处理消息  一个是发送
public class MiPushHandler_SendWorker extends ConsumeRun<MidMessage> {

    MiPushP2PMessageHandler mMiPushP2PMessageHandler;
    Map<String, MidMessageOrder_2> callBackMap;
    public MiPushHandler_SendWorker(ConcurrentLinkedQueue inQueue, Map<String, MidMessageOrder_2> callBackMap) {
        super(inQueue);
         mMiPushP2PMessageHandler = new MiPushP2PMessageHandler(null);
        this.callBackMap=callBackMap;
    }

    String mainServerIP = UDPTCPkeys.NetServer;
    ZMQClient mZMQClientForMain;
    FaceCommCallBack faceCommCallBackForLinkstatus = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if ((Boolean) t[0]) {
                Su.log("连接到了");
                mZMQClientForMain.setStatus(RunningStatus.Running, "重新和主服务链接");
            } else {
                if (mZMQClientForMain.getStatus() != RunningStatus.RunTimeErr) {
                    mZMQClientForMain.setStatus(RunningStatus.RunTimeErr, "和主服务链接中断");
                }
            }
            return false;
        }
    };



    public ZMQClient getZMQClient() {
        if (mZMQClientForMain == null) {
            mZMQClientForMain = new ZMQClient(mainServerIP, UDPTCPkeys.NetServerPort, 3 + "_" + DeviceInfo.getInstens().getSU_UUID(), false,
                    faceCommCallBackForLinkstatus, UDPTCPkeys.TimeOutLong_Client,callBackMap);
        }
        return mZMQClientForMain;
    }

    public void sendMessageByZMQ(MidMessage mMidMessageRess333) throws FaceException {
        getZMQClient().send(mMidMessageRess333);
        mZMQClientForMain.close();
        mZMQClientForMain=null;
    }


    @Face__UnsolvedDesignForDlp
    public void handlerMessage(MidMessage midMessageOrder_2) {
        MidMessage mMidMessageBackRes = null;
        if(midMessageOrder_2.isSendToNet()){//自己发出的
            mMidMessageBackRes=midMessageOrder_2;
        }else{
            MidMessageP2POrder_3 mMidMessageP2POrder_3= (MidMessageP2POrder_3) midMessageOrder_2;
            mMidMessageBackRes =  handlerP2PMessage(mMidMessageP2POrder_3);
            if(mMidMessageBackRes==null){
                mMidMessageBackRes=new MidMessageBack_P2PErr_4("处理失败",mMidMessageP2POrder_3);
            }
        }

        try {
            sendMessageByZMQ(mMidMessageBackRes);
        } catch (FaceException e) {
            e.printStackTrace();
//            没有什么办法了吧
        }

    }

    private MidMessage handlerP2PMessage(MidMessageP2POrder_3 mMidMessageP2POrder_3) {
        MidMessage   mMidMessageBackRes=null;
        MidMessage_OrderBack_3 mMidMessageBack_ForOrder_3 = null;
        try {
            mMidMessageBack_ForOrder_3 = (MidMessage_OrderBack_3) mMiPushP2PMessageHandler.handlerMessage(mMidMessageP2POrder_3);
            mMidMessageBackRes=mMidMessageBack_ForOrder_3.getTo();
        } catch (FaceException e) {
            e.printStackTrace();
            mMidMessageBackRes=new MidMessageBack_Err_3(e.getMessage(),mMidMessageP2POrder_3);
        }
        return mMidMessageBackRes;
    }

    @Override
    public void doOne(MidMessage oo) {
        handlerMessage(oo);
    }


}
