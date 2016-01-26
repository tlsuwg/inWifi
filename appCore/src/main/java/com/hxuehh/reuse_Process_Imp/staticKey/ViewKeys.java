package com.hxuehh.reuse_Process_Imp.staticKey;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by suwg on 2014/5/24.
 */
public class ViewKeys {

    public static int StaticProxyNullView = 1;//独立的viewType
    public static int StaticProxyAccurate = 2;
    public static int StaticProxyView = 3;

    //    视图是什么
    public static final int AutoView_HeadGView = 1;//可以切换
    public static final int AutoView_HeadGViewOnlyList = 2;//只是list
    public static final int AutoView_HeadGViewOnlyGird = 3;//只是grid ==2
    public static final int AutoView_WaterView = 4;//瀑布流
    public static final int AutoView_PADView_4 = 5;//pad 4列
    @Deprecated
    public static final int AutoView_PADSTIDIYView_4 = 6;//pad 4列  加头
    public static final int AutoView_PADView_3 = 7;//pad 3列
    public static final int AutoView_HeadGViewOnlyListChat = 8;
    public static final int AutoView_HeadGViewOnlyListLoadNextFromTop = 9;//只是list 但是下拉加载next
    public static final int AutoView_Brand_MuYing = 10;//母婴品牌列表
    public static final int AutoView_Brand_MuYing_Two = 11;//母婴两列
    @Deprecated
    public static final int AutoView_PADSTIDIYView = 12;
    public static final int AutoView_Brand = 13;
    public static final int AutoView_BrandDetail = 14;
    public static final int AutoView_HeadGViewOnlyListImHis = 15;
    public static final int AutoView_HotBrand = 16;


    //    按照什么展示
    public static final int ItemListView = 01;
    public static final int ItemGridView = 02;
    public static final int ItemWaterView = 03;
    public static final int ItemPadView_4 = 04;
    public static final int ItemPadView_3 = 05;


    public static final int ACKEY_DEFAULT = -1;       //除了下面的key，其他的都是-1
    public static final int ACKEY_JU_CHANNEL = 0;     //聚频道
    public static final int ACKEY_BANNER = 2;         //首页banner
    public static final int ACKEY_PUSH = 4;           //Push
    public static final int ACKEY_JINRIGENGXIN = 7;   //今日更新
    public static final int ACKEY_PROMOTION = 8;      //大促banner
    public static final int ACKEY_SALE_TIP = 9;       //开卖提醒
    public static final int ACKEY_JINGXUANYUGAO = 10; //精选预告
    public static final int ACKEY_SEARCH_RESULT = 11; //搜索结果
    public static final int ACKEY_EVERY_TEN = 14;     //每日十件

    public static final int ACKEY_JIUKUAIJIU = 15;     //九块九包邮
    public static final int ACKEY_20YUANFENGDING = 16; //20元封顶


    public static final int Activity = 10000;//Acticity预留

    public static final int MainActivity = 01;         // 主框架

    public static final int MainActivity_BrandGroupFragmet = MainActivity * Activity + 3;//品牌团
    public static final int MainActivity_BrandGroupFragmet_TabS_0 = MainActivity_BrandGroupFragmet + 100 * 0;//这个只是标准
    public static final int MainActivity_BrandGroupFragmet_HeadBannerView =
            MainActivity_BrandGroupFragmet * 100 + 1 * 100 + 1;//品牌团第一个tab头部点击

    public static final int MainActivity_IntegralFragmet = MainActivity * Activity + 4;//积分商城
    public static final int MainActivity_UserCenterFragmet = MainActivity * Activity + 5;//个人中心


    public static final int MuYingMainActivity = 2;//母婴频道需要预留  最少30个频道
    public static final int NewBrandGroupActivityTrue = 3;//这个只是展示使用了


    public static final int MessageHisContactActivity = 4;  //联系人列表
    public static final int OneMuYingBrandGroupGoodsActivity = 5;//母婴商品详情页
    public static final int OneBrandGroupDetailActivity = 6;//品牌团商品详情

    public static final int BrandGroup_Hot_Foreshow_Avtivity3_hot = 7;//品牌团 最热
    public static final int BrandGroup_Hot_Foreshow_Avtivity3_foreast = 8;//品牌团 预告
    public static final int MessagesForSomeOneActivity = 9;//im聊天






//    ==================================================


    public static int Client_InitAc=1;
    public static int Client_StatusAc=2;
    public static int ShowDeviceCapacityAc=3;
    public static int ServerDeviceServiceStatusAc=4;

    public static int ScreenClientAc=5;
    public static int FlashlightClientAc=6;
    public static int TemperatureClientAc=7;
    public static int HumidityClientAc=8;
    public static int OnTopActivity=9;//任意
    public static int PhotographClientAc=10;
    public static int VibrationAc=11;
    public static int USBLinkClientAc=12;
    public static int BrightnessClientAc=13;
    public static int VolumeClientAc=14;
    public static int StorageClientAc=15;
    public static int MemoryClientAc=16;
    public static int RecordClientAc=17;
    public static int SUMediaPlayerClientAc=18;
    public static int HeadsetClientAc=19;
    public static int AccessoryFindAc=20;
    public static int FinderAc=21;
    public static int AccessoryTestAc=22;
    public static int USBSerializClientAc=23;
    public static int VoiceSynthesisClientAc=24;
    public static int VoiceRecognitionClientAc=25;
    public static int AccelerationSensorClientAc=26;
    public static int SceneClientAc=27;
    public static int USBGPIOClientAc=28;
    public static int SuVideoSenseClientAc=29;
    public static int Client_AccelerationSetting=30;
    public static int PluginHumiditySensorClientAc=31;
    public static int PluginInfrareBodySensorClientAc=32;
    public static int PluginTemperatureSensorClientAc=33;
    public static int PluginIOInSensorClientAc=34;
    public static int ApFindActivity=35;


    public static LinearLayout.LayoutParams getLinearLayoutP() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static void addIntoLin(int res,View view2, android.app.Activity m) {
      LinearLayout  mainView= (LinearLayout)m.findViewById(res);
        mainView.addView(view2,getLinearLayoutP());
    }

    public static void addIntoLin(View main,View view2,Context m) {
        ((LinearLayout)main).addView(view2, getLinearLayoutP());
    }
}
