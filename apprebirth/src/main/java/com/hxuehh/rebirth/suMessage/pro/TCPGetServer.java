package com.hxuehh.rebirth.suMessage.pro;

import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.Face__UnsolvedDesignForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.ServiceAb.AbService_TCPLongLink_;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_Err_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TCPGetServer extends RunningStatus{

    private ZMQ.Context context;
    private ZMQ.Socket dealer;
    private ZMQ.Socket router;


    String name;
    int type;
    MidMessageHandler mMidMessageHandler;

    int allMessageCont;

    public TCPGetServer(String name, int type, MidMessageHandler nMidMessageHandler) {
        this.name = name;
        this.type = type;
        this.mMidMessageHandler = nMidMessageHandler;
    }

    public void listenTCP(final FaceCommCallBack tcPcall) {
        ThreadManager.getInstance().getNewThread("Tcp server:" + name, new Runnable() {
            @Override
            public void run() {
                {
                    if (type==0) {
                        trueMainListen(tcPcall);
                    } else {
                        trueDevicelisten(tcPcall);
                    }
                }
            }
        }).start();


    }

    private void trueDevicelisten(FaceCommCallBack tcPcall) {
        context = ZMQ.context(1);
        String ip = getIP();
        ZMQ.Socket response = context.socket(ZMQ.REP);

//        ZMQListener.setListener(context, response, name, new FaceCommCallBack() {
//            @Override
//            public boolean callBack(Object[] t) {
//                zmq.ZMQ.Event event = (zmq.ZMQ.Event) t[0];
//                Su.log(name+" "+event.event + "  " + event.addr);
//                return false;
//            }
//        });

        try {
            response.bind(ip);    //绑定端口
        } catch (Exception e) {
            e.printStackTrace();
            tcPcall.callBack(e);
            setStatus(RunningStatus.InitErr,e.getMessage());
            close();
            trueClose();
            return;
        }
        tcPcall.callBack();//OK
        setStatus(RunningStatus.Running,null);
        getallget(response);

        response.close();
        try {
            context.term();
        }catch (Exception e){
            e.printStackTrace();
        }
        context=null;

    }




    String getIP() {
        int port=0;
        switch (type){
            case 0:
                port=UDPTCPkeys.MainServerTCPPort;
            break;
            case AbService_TCPLongLink_.AbService_TCPLongLink_Client:
                port=UDPTCPkeys.ClientTCPPort;
                break;
            case AbService_TCPLongLink_.AbService_TCPLongLink_Device:
                port=UDPTCPkeys.DeviceTCPPort;
                break;
        }
        String ip = UDPTCPkeys.ZMQ_TCP + UDPTCPkeys.selfIp + ":" + port;
        Su.log(ip);
        return ip;
    }



    List<ZMQ.Socket> list;
    @Face__UnsolvedDesignForDlp
    private void trueMainListen(final FaceCommCallBack tcPcall) {
        setStatus(RunningStatus.Init,null);
        context = ZMQ.context(1);
        final String ipcAddress = UDPTCPkeys.ZMQ_IPC + name;
        try {
            dealer = context.socket(ZMQ.DEALER);  //后端  转接数据源
            dealer.bind(ipcAddress);  //监听这个地方
        } catch (Exception e) {
            e.printStackTrace();
            tcPcall.callBack(e);
            setStatus(RunningStatus.InitErr,e.getMessage());
            close();
            trueClose();
            return;
        }

        try {
            router = context.socket(ZMQ.ROUTER);  //暴露出去  前端  去接受  转给dealer后端
//        router.bind("ipc://fjs1");
            String ip = getIP();
            router.bind(ip);    //绑定端口
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(RunningStatus.InitErr,e.getMessage());
            tcPcall.callBack(e);
            close();
            trueClose();
            return;
        }


        tcPcall.callBack();//OK


//        设置 response  绑定在fjs2 上
        for (int i = 0; i < (type==0 ? ThreadManager.TCPMainResponseThread : ThreadManager.TCPDeviceResponseThread); i++) {
            ThreadManager.getInstance().getNewThread(name + " response get Thread " + i,10, new Runnable() {
                @Override
                public void run() {
                    list=new ArrayList();
                    ZMQ.Socket response = context.socket(ZMQ.REP);
//                    ZMQListener.setListener(context, response, name+ finalI, new FaceCommCallBack() {
//                        @Override
//                        public boolean callBack(Object[] t) {
//                            zmq.ZMQ.Event event = (zmq.ZMQ.Event) t[0];
//                            Su.log(name + " " + event.event + "  " + event.addr);
//                            return false;
//                        }
//                    });

                    response.connect(ipcAddress);  //response就是获取的地方；获取的连接到 这个地方去读取
                    list.add(response);
                    getallget(response);
                    response.close();
                    trueClose();
                }
            }).start();
        }

        ZMQ.proxy(router, dealer, null);  //到前端的请求  转到后端去  置换中心  代理接受啊
        setStatus(RunningStatus.Running,null);

    }


    @Face__UnsolvedDesignForDlp
    private void getallget(ZMQ.Socket response) {
        while (isRun) {
            allMessageCont++;
            MidMessageBack_2 midMessageres = null;
            MidMessageOrder_2 midMessage = new MidMessageOrder_2();
            long start=new Date().getTime();
            try {
                byte[] bs = response.recv();
                if(DevRunningTime.isShowRec)
                    Su.log(name+" recv byte 时间;"+(new Date().getTime()-start)/1000f+ " 长度："+bs.length +" 线程："+Thread.currentThread().getName()+" 总数:"+allMessageCont+" response:"+response);
                try {
                    midMessage.getTrue(bs);
                } catch (FaceException e) {
                    e.printStackTrace();
                    midMessageres = new MidMessageBack_Err_3(e.getMessage(), midMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                midMessageres = new MidMessageBack_Err_3("get message err", midMessage);
            }

            try {
                midMessageres = mMidMessageHandler.handlerMessage(midMessage);
            } catch (MidMessageNotHandledException e) {
                midMessageres = new MidMessageBack_Err_3("MidMessageNotHandledException", midMessage);
            }catch (FaceException e){
                midMessageres = new MidMessageBack_Err_3(e.getMessage(), midMessage);
            }

            if (midMessageres == null) {
                midMessageres = new MidMessageBack_Err_3("MidMessageHandledException", midMessage);
            }
            if(isRun)
                response.send(midMessageres.getAllBytes());
        }

    }

    void trueClose() {
        if (type==0) {
            if (router != null)
                router.close();
            router = null;

            if (dealer != null)
                dealer.close();
            dealer = null;

            if(list!=null&&list.size()>0){
                for(ZMQ.Socket ms:list){
                    ms.close();
                }
            }
            list.clear();
        }


        try {
            if (context != null)
                context.term();

        }catch (Exception e){
            e.printStackTrace();
        }
        context = null;

    }

    boolean isRun = true;
    public void close() {
        isRun = false;
        ThreadManager.getInstance().getNewThread("end ",new Runnable() {
            @Override
            public void run() {
                setStatus(RunningStatus.End,null);
                trueClose();
            }
        }).start();

    }


}