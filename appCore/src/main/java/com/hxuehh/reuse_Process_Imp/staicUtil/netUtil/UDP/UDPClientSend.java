package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.UDP;

import com.hxuehh.appCore.develop.Su;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UDPClientSend {


	DatagramSocket mDatagramSocket = null;
	InetAddress inetAddress = null;
	int targetport = 0;
	int useport;
	String targerIP;
	String uDPNames;
	public String getuDPNames() {
		return uDPNames;
	}
	public void setuDPNames(String uDPNames) {
		this.uDPNames = uDPNames;
	}
    public InetAddress getInetAddress() {
        return inetAddress;
    }
	public int getTargetport() {
		return targetport;
	}
	public void setTargetport(int targetport) {
		this.targetport = targetport;
	}

    public DatagramSocket getmDatagramSocket() {
        return mDatagramSocket;
    }

    public void setmDatagramSocket(DatagramSocket mDatagramSocket) {
        this.mDatagramSocket = mDatagramSocket;
    }

	public UDPClientSend(String targerIP, int useport, int targetport,
			String uDPNames) throws SocketException, UnknownHostException {
		this.uDPNames = uDPNames;
		if (useport == 0) {
			mDatagramSocket = new DatagramSocket();
		} else {
			mDatagramSocket = new DatagramSocket(useport);
			this.useport = useport;
		}
		this.targetport = targetport;
		this.targerIP = targerIP;
		inetAddress = InetAddress.getByName(targerIP);
	}


    public UDPClientSend(InetAddress inetAddress,  int useport,int targetport,
                         String uDPNames) throws SocketException, UnknownHostException {
        this.uDPNames = uDPNames;
        if (useport == 0) {
            mDatagramSocket = new DatagramSocket();
        } else {
            mDatagramSocket = new DatagramSocket(useport);
            this.useport = useport;
        }
        this.targetport = targetport;
        this.inetAddress = inetAddress;
    }


    public void send(String str) throws IOException {
		byte end[] = str.getBytes();
		DatagramPacket dataEnd = new DatagramPacket(end, end.length,inetAddress, targetport);
			mDatagramSocket.send(dataEnd);
			Su.log("udp sendSyncAddCache͵" + "  getLocalPort"
                    + mDatagramSocket.getLocalPort() + "   " + targetport
                    + "IP" + inetAddress.toString());
	}


    public void send(byte end[] ,int times,boolean isLog) throws IOException {
        DatagramPacket dataEnd = new DatagramPacket(end, end.length,inetAddress, targetport);
        int i=0;
        while(i++<times) {
            mDatagramSocket.send(dataEnd);
        }
		if(isLog)
        Su.log("udp sendSyncAddCache͵" + "  getLocalPort"
                + mDatagramSocket.getLocalPort() + " " + targetport
                + "IP" + inetAddress.toString());
    }



	public void close() {
		Su.log("UDPClientSend close");
		mDatagramSocket.close();
	}




}
