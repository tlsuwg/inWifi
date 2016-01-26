package com.hxuehh.rebirth.push.MiPush.handerMessage;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by suwg on 2015/9/28.
 */
public class MiPushHandlerManger {
    private static MiPushHandlerManger instance = null;
    ConcurrentLinkedQueue<MidMessage>  inQueue = new ConcurrentLinkedQueue();
    Map<String, MidMessageOrder_2> callBackMap=new HashMap();
    MiPushHandler_SendWorker mMiPushHandlerWorker;
    private MiPushHandlerManger() {
         mMiPushHandlerWorker = new MiPushHandler_SendWorker(inQueue,callBackMap);
        new Thread(mMiPushHandlerWorker).start();
    }
    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new MiPushHandlerManger();
        }
    }
    public static MiPushHandlerManger getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    public void addOrder(MidMessage oo){
        synchronized (inQueue){
            inQueue.add(oo);
            inQueue.notify();
        }
    }

    public void resCallBack(MidMessage res) {
       String info= (String) res.getByKey(MidMessage.Key_CMDkeyResquetID);
        if(StringUtil.isEmpty(info))return;
        MidMessage from= callBackMap.remove(info);
        if( from!=null&&from.getmFaceCommCallBack()!=null) {
            from.getmFaceCommCallBack().callBack(res);
        }
    }
}
