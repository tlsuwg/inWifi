package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClientget {

    DatagramSocket datagramSocket = null;
    DatagramPacket pack;
    int bytesize;

    FaceCommCallBack mUDPMessageGetListener;

    public UDPClientget(DatagramSocket getmDatagramSocket,
                        FaceCommCallBack mUDPMessageGetListener) {
        this.mUDPMessageGetListener = mUDPMessageGetListener;
        datagramSocket = getmDatagramSocket;
        Su.log("UDP" + datagramSocket.getLocalPort() + " ");
    }


    public UDPClientget(int port, FaceCommCallBack mUDPMessageGetListener, int bytesize) {
        this.mUDPMessageGetListener = mUDPMessageGetListener;
        this.bytesize = bytesize;
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            mUDPMessageGetListener.callBack(e);
            return;
        }
        Su.log("UDP" + datagramSocket.getLocalPort() + " ");
    }

    public void getForGetAddress() {

        if(datagramSocket==null)return;
        byte bs[] = new byte[bytesize];
        mUDPMessageGetListener.callBack();//另外线程发送了 标记线程已经运行起来
        while (isRun) {
            pack = new DatagramPacket(bs, bs.length);
            try {
                datagramSocket.receive(pack);
                if (mUDPMessageGetListener != null) {
                    byte[] bys = pack.getData();
                    if(bys[0]==UDPTCPkeys.UDPFindServerOK) {
                        InetAddress mInetAddress = pack.getAddress();
                        mUDPMessageGetListener.callBack( mInetAddress);
                        close();
                    }
                }
            } catch (IOException e) {
                if(isRun)
                e.printStackTrace();
            }

        }
    }




    public void getForSendBack(byte[] bsnd) {
        byte bs[] = new byte[bytesize];
        mUDPMessageGetListener.callBack();//另外线程发送了 标记线程已经运行起来
        while (isRun) {
            try {
                pack = new DatagramPacket(bs, bs.length);
                datagramSocket.receive(pack);
                try {
                    InetAddress mInetAddress = pack.getAddress();
                    UDPClientSend mUDPClientSend = new UDPClientSend(mInetAddress,0, UDPTCPkeys.Device_Client_broadcastPort, "sendback");
                    mUDPClientSend.send(bsnd,10,true);
                } catch (Exception e) {
                    if (isRun)
                        e.printStackTrace();
                }

            } catch (IOException e) {
                if (isRun)
                    e.printStackTrace();
                break;
            }
        }


    }

    boolean isRun = true;

    public void close() {
        isRun = false;
        datagramSocket.close();
    }

}
