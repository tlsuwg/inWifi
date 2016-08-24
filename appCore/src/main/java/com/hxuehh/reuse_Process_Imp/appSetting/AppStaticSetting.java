package com.hxuehh.reuse_Process_Imp.appSetting;

/**
 * Created by suwg on 2014/8/8.
 */
public class AppStaticSetting {
    public static boolean isTest=true;//测试环境

    public static final int LOG_CLOSED = 0;// 0 展示log日常使用   1线上环境
    public static final int IM_TEST = 0;    //im配置开关 ，;0正式环境，2开发，1测试使用
    public static final int H5_TEST = 0; //h5打开测试页面，0正式环境
    public static final int PROMOTION_CLOSED = 0;//测试用 强制关掉大促 正式环境设置成0
    public static final int DEV_TEST_SWITCH = 0; //测试使用直接改变device ID

    public static  boolean isMiTestLog =true;//
    public static  boolean isMiTestNet =false;//


    public static final int LOG_ERR_FEED = 1;//1回传日志  0不会传
    public static final int LOG_ERR_SAVE = 0;//1日志本地保存
    public static final long POLLING_INTERVAL = 7200000;
    public static final int SHOW_TIANMAO_ICON = 0;//0 显示 1不显示 注:这个不要修改,恒为0.
    public static final int SALES_COUNT_SHOW_WAN= 1;// 0:不显示万 该多少就多少 ;
    // 1:显示万 销量超过1w就显示x.x万
    public static final int SHOW_COVERED_GUIDE = 1;// 是否显示覆盖安装的新手引导, 1 显示,0 不显示.
    @Deprecated //建议不使用
    public static final int NETWORK_PROPERTIES_TEST_ENVIRONMENT =0;//0 为正式,其他都是测试环境

    public static final int IS_ADD_USER_IDENTITY_INFO_TO_DEAL_LIST = 1; //列表数据请求的时候是否添加身份信息   0：不添加  ,   1：添加

    public static final long REFRESH_INTERVAL=1000*60*5;//列表刷新的间隔
    // database
    public static String DEFAULT_DATABASE = "rebirth.db";




    public static  boolean isSunShaoQingPro=true;


    public static  int RELIC_SWITCH = 0;//1打开    0关闭
    public static  int SHOW_NET_LOC_SWITCH = 0;//1打开   工信部  ， 0关闭
    public static  int SZLM_O_SWITCH = 0;//1打开  数字联盟  ， 0关闭
    public static  int TELECOM_SWITCH = 0;//1打开 电信返积分 ， 0关闭
    public static  int OFFLINE_SWITCH = 0; // 0 关闭   线下预制， 1 打开
    public static  int WIDGET_SWITCH = 0; // 小部件, 0 关闭， 1 打开
    public static  int IM_SWITCH = 1; // im 0 关闭， 1 打开
    public static  int MI_SWITCH = 1; //小米push 0 关闭， 1 打开
    public static  int JIDIAO_SWITCH = 0; //基调 0 关闭， 1 打开

    public static int Loc_Geographic_Time=1000*0;//定位一次
    public static boolean isTestPhotoGraph=false;
    public static long PhotographGetSleepTime=1000*15;
    public static long VideoraphGetSleepTime=1000*25;//视频打开超时
    public static boolean isUSBMustLink=true;//正式环境是true
    public static long openScreeTestSleepTime=100;//检查
    public static int BrightnessSetting=10;
    public static int VolumeSetting=1;

    public static int Device_AccelerationSensor_X=1;
    public static int Device_AccelerationSensor_Y=1;
    public static int Device_AccelerationSensor_Z=1;
    public static long XYZ3TimesTime=1000*5;
    public static int XYZ3Times=2;

    public static long SensorWakelockTime=1000*10;
    public static long ScreenWakelockTime=1000*30;

    public static long FlashlightWakelockTime=1000*10;//屏幕自动点亮时间 一分钟;
    public static long onOtherChangeAccuracyChangedTimeOut=1000*5;
    public static byte WCH_Rotate_SettingTime=12;
    public static int RC=255,GC=255,BC=255;

    public static long Light_Lock_Time=1000l;//开闭灯传感器关闭200毫秒
    public static long UnLcokLastTime=200l;//解锁之后还要延迟100 毫秒




    public static long FullStorageSize=2;//单位G
    public static long SDStorageSize=80;
    public static String carDir="car_video";
    public static String vedioEnd=".3gp";
    public static long OneCarFilezSize=2*1024*1024;
    public static String noDeleteFlag="重要_";

    static{
        if(!isTest){
            isMiTestLog =false;
            isTestPhotoGraph=false;
        }
    }



    static{
//---------------------------------
        //    @800监控#%add%#
//            RELIC_SWITCH=1;
        //    @800监控#%end%#
//---------------------------------
        //     @800工信#%add%#
//            SHOW_NET_LOC_SWITCH=1;
        //     @800工信#%end%#
//---------------------------------
        //     @800数字联盟#%add%#
//            SZLM_O_SWITCH=1;
        //     @800数字联盟#%end%#
//---------------------------------
        //     @800电信返积分#%add%#
//            TELECOM_SWITCH=1;
        //     @800电信返积分#%end%#
//---------------------------------
        //     @800线下预制#%add%#
        //    OFFLINE_SWITCH=1;
        //     @800线下预制#%end%#
//---------------------------------
        //     @800小部件#%add%#
//            WIDGET_SWITCH=1;
        //     @800小部件#%end%#
//---------------------------------
        //     @800mi#%add%#
//             MI_SWITCH=1;
        //     @800mi%end%#
 //---------------------------------
        //    @800im#%add%#
//            IM_SWITCH=1;
        //    @800im#%end%#
//---------------------------------
        //    @800基调#%add%#
//            JIDIAO_SWITCH=1;
        //    @800基调#%end%#
  // ---------------------------------


    }



}
