package com.hxuehh.rebirth.suMessage.domain;



/**
 * Created by suwg on 2015/8/16.
 */
public class MidMessageCMDKeys {


    public static final int MidMessageCMD_Back = 1;//返回  类型 直接找ID 找对象 找回掉




    //==============================================================================================

//    10-50服务器端响应

    public static final int MidMessageCMD_Service_MIN = 11;//包含==
    public static final int MidMessageCMD_Service_MAX = 50;


    public static boolean isServerCom(int kk) {
        return kk <= MidMessageCMD_Service_MAX && kk > MidMessageCMD_Service_MIN;
    }


    public static final int MidMessageCMD_Service_End = 11;
    public static final int MidMessageCMD_Service_ReStatrt = 12;
    public static final int MidMessageCMD_Service_Info = 13;
    public static final int MidMessageCMD_Service_Device_register = 15;
    public static final int MidMessageCMD_Service_Device_Heartbeat = 16;


    //==============================================================================================

    //    51-250客户单push端，透传响应
    public static final int MidMessageCMD_client_MIN = 51;//包含==
    public static final int MidMessageCMD_Client_MAX = 250;


    public static boolean isClientCom(int kk) {
        return kk <= MidMessageCMD_Client_MAX && kk > MidMessageCMD_client_MIN;
    }

    public static final int MidMessageCMD_Client_DeviceInfo_Change = 51;
    public static final int MidMessageCMD_Client_DeviceCapacity_Change = 52;

    //==============================================================================================

    public static final int MidMessageCMD_Device_MIN = 251;//包含==
    public static final int MidMessageCMD_Device_MAX = 1000;

    public static boolean isDeviceCom(int kk) {
        return kk <= MidMessageCMD_Device_MAX && kk > MidMessageCMD_Device_MIN;
    }

    public static final int MidMessageCMD_Device_End = 251;
    public static final int MidMessageCMD_Device_ReStatrt = 252;
    public static final int MidMessageCMD_Device_Info = 253;
    public static final int MidMessageCMD_Device_Cmd_Opstion = 254;


    public static final int MidMessageCMD_Device_AutoUpdate = 255;//自动升级
    public static final int MidMessageCMD_Device_StopAll = 256;//关闭所有设备
    public static final int MidMessageCMD_Device_CloseAll = 257;//注销掉
//    public static final int MidMessageCMD_Device_CloseAll = 256;//
//    public static final int MidMessageCMD_Device_CloseAll = 256;//
//    public static final int MidMessageCMD_Device_CloseAll = 256;//
//    public static final int MidMessageCMD_Device_CloseAll = 256;//
//    public static final int MidMessageCMD_Device_CloseAll = 256;//
//    public static final int MidMessageCMD_Device_CloseAll = 256;//
    
    
    
    
//    ======================================================
    public static final int MidMessageCMD_P2P_MIN = 1001;//包含==
    public static final int MidMessageCMD_P2P_MAX =1200 ;

    public static boolean isP2PCom(int kk) {
        return kk <= MidMessageCMD_P2P_MIN && kk > MidMessageCMD_P2P_MAX;
    }
    
    public static final int MidMessageCMD_P2P_Regist = 1001;//注册 IMEI 和reg;
    public static final int MidMessageCMD_P2P_Get_ShowLoc = 1002;//请求

    public static final int MidMessageCMD_P2P_Call = 1001;
  
    

}
