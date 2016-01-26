package com.hxuehh.reuse_Process_Imp.staticKey;

import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;

/**
 * Created by suwg on 2015/8/14.
 */
public class UDPTCPkeys {

    public static final String hostbroadcast = "255.255.255.255";//广播地址
//    public static final String selfIp = "localhost";

    public static final String selfIp = "*";

    public static final int Main_broadcastPort = 8904;//广播地址
    public static final int Device_Client_broadcastPort = 9029;//


    public static final int MainServerTCPPort = 8903;
    public static final int DeviceTCPPort = 9039;
    public static final int ClientTCPPort = 9049;


    public static String  NetServer="192.168.31.134";

    static {
        if(!AppStaticSetting.isMiTestNet){
            NetServer="182.92.114.220";
        }
    }




    public static final int NetServerPort = 8888;

    public static byte UDPFindServer = 1;
    public static byte UDPFindServerOK = 2;

    //    public static final String ZMQ_IPC = "ipc://";
    public static final String ZMQ_IPC = "inproc://";
    public static String ZMQ_TCP = "tcp://";


    public static long TimeOutLong_Device =1000*30;//超时时间
    public static long TimeOutLong_Client =1000*10;//超时时间
    public static long Check_TimeOutLong_Device =TimeOutLong_Device+3*1000;//超时时间 检测
    public static long Check_TimeOutLong_Client =TimeOutLong_Client+3*1000;//超时时间 检测

    public static long TimeOutLong_MAIN_link =1000*10;//超时时间







    public static long HeartLong_Device =1000*10;//内网心跳
    public static long HeartLong_Client=1000*60;

    public static int VideoUdpPortForGet=8978;
}
