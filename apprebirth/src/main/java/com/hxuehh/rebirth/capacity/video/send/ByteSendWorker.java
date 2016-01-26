package com.hxuehh.rebirth.capacity.video.send;

import com.hxuehh.appCore.faceFramework.faceProcess.ConsumeRun;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP.UDPClientSend;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by suwg on 2015/10/9.
 */
public class ByteSendWorker extends ConsumeRun<byte[]> {

    UDPClientSend mUDPClientSender;

    public ByteSendWorker(ConcurrentLinkedQueue<byte[]> inQueue,UDPClientSend mUDPClientSender) {
        super(inQueue);
        this.mUDPClientSender=mUDPClientSender;
    }


    @Override
    public void doOne(byte[] oo) {
        try {
            mUDPClientSender.send(oo,1,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
