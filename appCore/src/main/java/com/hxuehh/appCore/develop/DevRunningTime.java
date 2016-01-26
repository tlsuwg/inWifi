package com.hxuehh.appCore.develop;

import android.content.Context;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;

import java.util.List;

/**
 * Created by suwg on 2014/7/11.
 * 内存使用状态，
 * 增加一个自动加载功能直接加载1900
 */

@FaceTestforDlp
public class DevRunningTime {

    static final int MinCache = 64;//低于这个内存不开启
    static final Double type = 1024.0d * 1024;

    static final String typename = "MB";
    public static boolean isShowImageErr = false;//展示图片


    public static boolean isShowAppusedTime = false;//展示时间

    public static boolean isSuTestM_C = false;//自动加载
    public static boolean isShowEnd = false;//滑到最后
    private static int laodMax = 1900;
    public static int isMustEverTimeLoadNumber = 40;

    //http
    public static boolean isShowHttPData = true;//dayin  http字符
    public static boolean isShowHttPDataForShort = true;//展示http加载的消息

    //IM
    public static boolean isShowIMJID = false;
    public static boolean isShowIMLast = false;
    public static int ImShortTime = 10;
    public static boolean IMShortTime = false;//im时间  5分钟 30秒钟
    public static boolean IMDevGroup = AppStaticSetting.IM_TEST == 2;//开发组
    public static boolean isShowImLoginInfo = false;

    public static boolean isCacheImage = false;//自己默认的去加载URL 主要是测试image cache

    public static boolean isShowBabyDialog = true;


    public static boolean isShowRunningTime = false;//打印控件占用
    public static Runtime run = Runtime.getRuntime();
    public static Double max = run.maxMemory() / type;
    public static boolean isCacheFull = (max >= MinCache);
    static final Double Danger = max / 8;//根据一个bitmap+Ac的开销测算，小手机必崩

    //public static boolean isCacheFull =  max-run.totalMemory() / type+run.freeMemory() / type>1/8*max;
    // public static boolean isCacheFull=false;
    public static boolean isSleepForBrand = false;//品牌分类加载延迟


    //    展示进程开销
    public static boolean isShowProessInfo = false;

    public static boolean isTaoBaoProessNotDBinit = true;//不进行webprosss开销


    //    ==========================
    public static boolean isShowRec = true;
    public static boolean isShowSend = true;
    public static boolean isSendHeartbeat = true;
    public static boolean isSend100=false;
    public static boolean isSetSharedPreferencesForType=true;
    public static boolean isCanDevServerClientInThis=true;//三体

    public static boolean lastSuApplication=false;


    public static void showRunningTime() {
        if (AppConfig.getInstance().LOG_CLOSED && !isShowRunningTime) return;
        Double total = run.totalMemory() / type;
        Double free = run.freeMemory() / type;
        Double used = total - free;
        Double usable = max - total + free;
        Su.log("当前进程:" + SuApplication.getInstance().getCurProcessName());
        Su.log("最大内存 = " + max + typename);
        Su.log("已分配内存 = " + total + typename);
        Su.log("已使用 = " + used + typename);
        Su.log("已分配内存中的剩余空间 = " + free + typename);
        if (usable <= Danger) {
            isDanger = true;
            Su.logE("危险，危险，危险，可使用内存 = " + usable + typename);
        } else {
            isDanger = false;
            Su.log("可使用内存 = " + usable + typename);
        }
    }

    //    是不是继续加载
    public static boolean isGoonLoadByRunningTime(List list, Context mContext) {
        Su.log("加载数据个数：" + list.size());
        showRunningTime();
        if (!AppConfig.getInstance().LOG_CLOSED && isSuTestM_C && list != null && list.size() < laodMax) {
            Su.logE("继续去加载");

            return true;
        }
        Su.log("不需要继续加载");
        return false;
    }

    public static boolean isDanger;

    public static boolean isHasCache() {
        showRunningTime();
        if (isDanger) {
            throw new RuntimeException("没有足够空间了");
        }
        return isDanger;
    }

}
