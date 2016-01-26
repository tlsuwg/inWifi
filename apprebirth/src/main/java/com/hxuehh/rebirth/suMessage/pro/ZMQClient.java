package com.hxuehh.rebirth.suMessage.pro;

import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.domain.LinkTimeFaceCall;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageP2POrder_3;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import org.zeromq.ZMQ;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class ZMQClient extends RunningStatus {

    private final boolean isThrough_NotAddToSend;//直接发送
    String mainServerIP, name;
    int port;

    ConcurrentLinkedQueue<MidMessage> inQueue = new ConcurrentLinkedQueue<MidMessage>();// 获取到的消息，需要处理
    AtomicBoolean isRun = new AtomicBoolean(true);
    String ip;

    Map<String, MidMessageOrder_2> callBackMap;
    FaceCommCallBack faceCommCallBackForLinkstatus;
    long timeOutLong;

    public ZMQClient(final String mainServerIP, final int port, final String name, boolean isThrough,
                     FaceCommCallBack faceCommCallBackForLinkstatus, long timeOutLong, Map<String, MidMessageOrder_2> callBackMap) {
        this.mainServerIP = mainServerIP;
        this.name = name;
        this.isThrough_NotAddToSend = isThrough;
        this.faceCommCallBackForLinkstatus = faceCommCallBackForLinkstatus;
        this.timeOutLong = timeOutLong;
        this.port = port;
        this.callBackMap = callBackMap;
        if (StringUtil.isEmpty(mainServerIP)) {
            this.mainServerIP = UDPTCPkeys.selfIp;
        }
        ip = UDPTCPkeys.ZMQ_TCP + mainServerIP + ":" + port;


        if (!isThrough) {
            new Thread(new Runnable() {
                private MidMessage getPoll() {
                    synchronized (inQueue) {
                        MidMessage oo = inQueue.poll();
                        if (oo == null && isRun.get()) {
                            try {
                                inQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return getPoll();
                        } else {
                            return oo;
                        }
                    }
                }

                @Override
                public void run() {
                    if (context == null) link();
                    while (isRun.get()) {
                        MidMessage oo = getPoll();
                        if (!isRun.get()) break;
                        try {
                            send(oo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    trueClose();

                }
            }, " ZMQClient send queue " + name).start();
        }
    }

    private void trueClose() {
        Su.log("true close"+this+"  "+isRun.get());
        if (moniter != null)
            moniter.close();

        try {
            if (socket != null)
            socket.close();
            if (context != null)
            context.term();
        }catch (Exception e){
            e.printStackTrace();
        }

        context=null;
        socket=null;

    }


    boolean isSending;
    public MidMessageBack_2 send(MidMessage oo) throws FaceException {
        Su.log(oo.getCmd()+" send"+this+"  "+isRun.get());
        if (context == null) link();
        byte[] bs = oo.getAllBytes();
        long start = 0,end=0;
        if (DevRunningTime.isShowSend)
            start = new Date().getTime();
        if(isSending)throw new FaceException("设备链接繁忙，请检查设备端是否启动APP，联网情况等");
        isSending=true;
        socket.send(bs);
        if (DevRunningTime.isShowSend) {
            Su.log(name + " send ok 时间:" + (new Date().getTime() - start) / 1000f + "长度;" + bs.length);
        }
        byte[] responseGetBytes = socket.recv();
        isSending=false;
        end=new Date().getTime();
        if (DevRunningTime.isShowSend)
            Su.log(name + " send back Used 时间;" + (end - start) / 1000f + "长度;"  + bs.length);

        if (!isThrough_NotAddToSend) {
            callBackToHander(responseGetBytes, oo,end);
            return null;
        } else {
            return backThrough(responseGetBytes, oo,end);
        }
    }


    private MidMessageBack_2 backThrough(byte[] response, MidMessage send, long end) throws FaceException {
        MidMessageBack_2 mid = new MidMessageBack_2();
        mid.getTrue(response);
        return mid;
    }

    private void callBackToHander(byte[] response, MidMessage send, long end) {
        try {
            FaceCommCallBack faceCommCallBack = send.getmFaceCommCallBack();
            if (faceCommCallBack != null) {
                MidMessage mid = new MidMessageBack_2();
                mid.setTag(send.getTag());
                try {
                    mid.getTrue(response);
                    if (mid.getByKey(MidMessage.Key_NoHand_Back) != null) {//是直接返回类型的
                        putQueueWaitCall((MidMessageP2POrder_3)send);
                    } else {
                        faceCommCallBack.callBack(mid,end);
                    }
                } catch (FaceException e) {
                    e.printStackTrace();
                    faceCommCallBack.callBack(e,end);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putQueueWaitCall(MidMessageP2POrder_3 send) {
        if (callBackMap == null) return;
        String key = send.getmResquetID();
        if (StringUtil.isEmpty(key)) return;
        callBackMap.put(key, send);
    }


    public void sendSyncAddCache(MidMessage mid) {
        inQueue.add(mid);
        synchronized (inQueue) {
            inQueue.notify();
        }
    }


    ZMQ.Socket socket;
    ZMQ.Context context;

    private void link() {
        setStatus(RunningStatus.Init, null);
        context = ZMQ.context(1);  //创建一个I/O线程的上下文
        socket = context.socket(ZMQ.REQ);   //创建一个request类型的socket，这里可以将其简单的理解为客户端，用于向response端发送数据
        socket.connect(ip);   //与response端建立连接
        if (faceCommCallBackForLinkstatus != null && this.timeOutLong > 0) {
            setLinstenlinkStatus(this.faceCommCallBackForLinkstatus, this.timeOutLong);
        }
    }

    public void close() {
        Su.log(" close "+this+"  "+isRun.get());
        setStatus(RunningStatus.End, null);
        if (!isThrough_NotAddToSend) {
            isRun.set(false);
            synchronized (inQueue) {
                inQueue.notify();
            }
        }
        ThreadManager.getInstance().getNewThread("end " + name, 10,new Runnable() {
            @Override
            public void run() {
                Su.log("end zmq client true");
                trueClose();
            }
        }).start();

    }


    ZMQ.Socket moniter;

    public void setLinstenlinkStatus(final FaceCommCallBack faceCommCallBackForLinkErr, long timeOut) {

        LinkTimeFaceCall mLinkTimeFaceCall = new LinkTimeFaceCall(timeOut) {
            @Override
            public boolean callBack(Object[] t) {
                if(!isRun.get())return  false;
                if (!(Boolean) t[0]) {
                    if (getStartTime() == 0) {
                        setStartTime();
                        faceCommCallBackForLinkErr.callBack(false);
                    } else {
                        this.setActionTime();
                        if (isTimeOut()) {
//                            setinitStartTime();
                            setStartTime();
                            Su.log("检测：连接断开");
                            faceCommCallBackForLinkErr.callBack(false);
                        }
                    }
                } else {
                    setinitStartTime();
                    faceCommCallBackForLinkErr.callBack(true);
                    Su.log("检测：连接恢复");
                }

                return false;
            }
        };
        moniter = ZMQListener.setListener(isRun, context, socket, name, mLinkTimeFaceCall);
    }


}
