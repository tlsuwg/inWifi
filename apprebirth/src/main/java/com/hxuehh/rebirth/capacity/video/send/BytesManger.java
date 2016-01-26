package com.hxuehh.rebirth.capacity.video.send;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.rebirth.capacity.video.send.ByteSendWorker;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP.UDPClientSend;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by suwg on 2015/10/9.
 */
public class BytesManger {
    ByteSendWorker mByteSendWorker;

    public BytesManger(String ip,int port) throws SocketException, UnknownHostException {
        UDPClientSend mUDPClientSend=new UDPClientSend(ip,0,port,"video sender udp client");
        mByteSendWorker=new ByteSendWorker(new ConcurrentLinkedQueue(),mUDPClientSend);
        ThreadManager.getInstance().getNewThread("video sender udp ",mByteSendWorker).start();
    }

    List<byte[]> list=new ArrayList();
    boolean is5;

    public  void add(byte[] bs){
        if(!is5){
            list.add(bs);
            if(list.size()>5){
                for(byte[] b:list){
                    mByteSendWorker.add(b);
                    Su.log(b.length+" send");
                }
                is5=true;
            }
        }else {
            mByteSendWorker.add(bs);
        }
    }


}
