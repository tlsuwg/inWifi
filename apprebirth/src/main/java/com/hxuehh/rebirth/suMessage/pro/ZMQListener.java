package com.hxuehh.rebirth.suMessage.pro;

import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;

import org.zeromq.ZMQ;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by suwg on 2015/8/18.
 */
public class ZMQListener {

//    public static final int EVENT_CONNECTED = zmq.ZMQ.ZMQ_EVENT_CONNECTED;   //当主动建立连接建立成功之后的事件
//    public static final int EVENT_DELAYED = zmq.ZMQ.ZMQ_EVENT_CONNECT_DELAYED;   //连接延迟
//    public static final int EVENT_RETRIED = zmq.ZMQ.ZMQ_EVENT_CONNECT_RETRIED;    //尝试重新连接
//    public static final int EVENT_CONNECT_FAILED = zmq.ZMQ.ZMQ_EVENT_CONNECT_FAILED;   //连接失败
//
//    public static final int EVENT_LISTENING = zmq.ZMQ.ZMQ_EVENT_LISTENING;    //建立了监听
//    public static final int EVENT_BIND_FAILED = zmq.ZMQ.ZMQ_EVENT_BIND_FAILED;  //bind失败
//
//    public static final int EVENT_ACCEPTED = zmq.ZMQ.ZMQ_EVENT_ACCEPTED;   //接收到accept事件
//    public static final int EVENT_ACCEPT_FAILED = zmq.ZMQ.ZMQ_EVENT_ACCEPT_FAILED;   //accept出错的事件
//
//    public static final int EVENT_CLOSED = zmq.ZMQ.ZMQ_EVENT_CLOSED;   //关闭事件
//    public static final int EVENT_CLOSE_FAILED = zmq.ZMQ.ZMQ_EVENT_CLOSE_FAILED;     //关闭失败
//    public static final int EVENT_DISCONNECTED = zmq.ZMQ.ZMQ_EVENT_DISCONNECTED;   //连接断开
//
//    public static final int EVENT_ALL = zmq.ZMQ.ZMQ_EVENT_ALL;   //所有的事件


    @Face_UnsolvedForDlp
    public static ZMQ.Socket setListener(final AtomicBoolean is,ZMQ.Context context, ZMQ.Socket socket, String name, final FaceCommCallBack faceCommCallBack) {
         ZMQ.Socket moniter=null;
        try {
            socket.monitor("inproc://" + name,
                    zmq.ZMQ.ZMQ_EVENT_CONNECTED | zmq.ZMQ.ZMQ_EVENT_CONNECT_DELAYED | zmq.ZMQ.ZMQ_EVENT_CLOSED | zmq.ZMQ.ZMQ_EVENT_DISCONNECTED);  //这段代码会创建一个pair类型的socket，专门来接收当前socket发生的事件
             moniter = context.socket(ZMQ.PAIR);   //这里创建一个pair类型的socket，用于与上面建立的moniter建立连接
            moniter.connect("inproc://" + name);  //连接当前socket的监听

        }catch (Exception e){
            e.printStackTrace();
        }

        if(moniter!=null) {
            final ZMQ.Socket      moniter2=moniter;
            ThreadManager.getInstance().getNewThread("socket check " + name, new Runnable() {
                @Override
                public void run() {
                    while (is.get()) {
                        try {
                            zmq.ZMQ.Event event = zmq.ZMQ.Event.read(moniter2.base());  //从当前moniter里面读取event
                            if (event != null)
                                switch (event.event) {
                                    case zmq.ZMQ.ZMQ_EVENT_CONNECTED: {
                                        faceCommCallBack.callBack(true);
                                    }
                                    break;
//                        case zmq.ZMQ.ZMQ_EVENT_CONNECT_DELAYED: {
//                            faceCommCallBack.callBack(false);
//                        }
//                        break;
//                        case zmq.ZMQ.ZMQ_EVENT_CONNECT_RETRIED: {
//
//                        }
//                        break;
                                    case zmq.ZMQ.ZMQ_EVENT_CLOSED: {
                                        faceCommCallBack.callBack(false);
                                    }
                                    break;

                                    case zmq.ZMQ.ZMQ_EVENT_DISCONNECTED: {
                                        faceCommCallBack.callBack(false);
                                    }
                                    break;
                                }

                        } catch (Exception e) {
//                        e.printStackTrace();
                        }

                    }

                    Su.log("moniter end");
                }
            }).start();
        }


        return moniter;
    }
}
