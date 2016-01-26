package com.hxuehh.reuse_Process_Imp.staticKey;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.hxuehh.reuse_Process_Imp.analytics.AbstractAnalyticsInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 12-05-31
 * Time: 上午9:23
 * To change this template use File | Settings | File Templates.
 */
public class AnalyticsStatic extends AbstractAnalyticsInfo {
    //点击发现更多
    public static final String EVENT_FINDMORE = "findmore";

    // 确认退出程序
    public static final String CONFIRM_EXIT = "ce";

    // 点击分享
    public static final String EVENT_SHARE = "s";

    // 点击分享
    public static final String EVENT_SHARE_SUCCESS = "suc";

    //push接收
    public static final String EVENT_POLL = "poll";

    //push触发事件
    public static final String EVENT_PUSH_CLICK = "pc";


    //mipush触发事件
    public static final String EVENT_MI_PUSH_CLICK = "xmpc";

    /************banner start***********/
    //首页banner位点击
    public static final String EVENT_BANNER_CLICK = "b";

    //逛分类banner位
    public static final String EVENT_BANNER_GFL = "gb";

    // 分类banner位跳转
    public static final String EVENT_BANNER_CATEGORY = "cb";
    /************banner end*************/

    //收藏店铺
    public static final String EVENT_FAVOR_SHOP = "fshop";

    //晒单
    public static final String EVENT_SHAI = "shai";

    //会员购登录
    public static final String EVENT_MEMBER_BUY_LOGIN = "t_buylogin";

    //返积分登录
    public static final String EVENT_BACK_INTEGRATION_LOGIN = "t_scorelogin";

    //积分回馈deal
    public static final String EVENT_INTEGRATION_DEAL = "dscore";

    //查看deal详情
    public static final String EVENT_DEAL_DETIAL = "webview";

    // 邀请好友积分(邀请好友注册)
    public static final String EVENT_INVITE = "invote";

    // 开卖提醒开关
    public static final String EVENT_SELL_SWITCH = "t_clockset";

    // 开卖提醒
    public static final String EVENT_SELL_NOTIFY = "t_clock";

    // 淘宝账户登录
    public static final String EVENT_TAOBAO_LOGIN = "tao";

    // 捆绑下载
    public static final String EVENT_DOWNLOAD_APP_BIND = "up";

    // 微信关注
    public static final String EVENT_WEIXIN_ATTENTION = "wx";

    //搜索框聚焦
    public static final String EVENT_SEARCH = "isearch";

    //精品预告设置提醒
    public static final String EVENT_CLOCK = "clock";

    //大促预告设置
    public static final String EVENT_CU_CLOCK = "cuclock";

    //二维码入口
    public static final String EVENT_ZXING_ENTER = "qr";

    //二维码扫描成功（设置提醒）
    public static final String EVENT_ZXING_SUCCESS = "qrs";

    //个人中心
    public static final String EVENT_USER_INFO = "userinfo";

    //分类入口(tab底部)
    public static final String EVENT_CATEGORY = "bsc";

    //切换列表模式
    public static final String EVENT_CHANGE_LIST = "clist";


    //安装后选择身份
    public static final String EVENT_MODEL = "model";

    //分类搜索（首页左上方）
    public static final String EVENT_SEARCH_MAIN = "is";

    //关注QQ空间
    public static final String EVENT_FOLLOW_QQ = "qqz";

    //我要登陆（out提示登录页面）
    public static final String EVENT_OLOGIN = "ologin";

    //列表收藏
    public static final String EVENT_LIST_FAVOR= "f";

    //宝宝生日选择
    public static final String EVENT_BIRTHDAY_SELECT = "baby";

    //母婴列表到生日选择
    public static final String EVENT_MUYING_TO_BIRTHDAY = "babyset";

    //母婴列表切换单品 品牌
    public static final String EVENT_MUYING_TO_BIRTHDAY_Change = "muying";

    //积分现金购
    public static final String EVENT_SCORE_BUY = "scorebuy";

    //积分商城 赚积分
    public static final String EVENT_EARN_SCORE = "earnscore";

    /*****category start******/
    //分类选择 全部
    public static final String EVENT_ALL_CATEGORY = "allc";
    //分类选择（分类页左侧）
    public static final String EVENT_CATEGORY_LEFT_DRAWER = "cc";
    //首页banner下面的方格入口
    public static final String EVENT_CATEGORY_MAIN_ACTIVITY = "ic";
    /*****category end******/

    //每日十件 关联搜索
    public static final String EVENT_DAY_SEARCH = "dsearch";

    //用户引导
    public static final String EVENT_GUIDE = "oobe";
     //用户引导跳过
    public static final String EVENT_GUIDE_SKIP = "skipbp";

    //打开淘宝客户端
    public static final String EVENT_TAOBAO = "taoapp";

    //0元抽奖无地址
    public static final String EVENT_LOTTERY_NO_ADDRESS = "t_newadr";
    //0元抽奖设置地址
    public static final String EVENT_LOTTERY_SET_ADDRESS = "t_saveadr";
    //0元抽奖查看抽奖单
    public static final String EVENT_LOTTERY_CHECK_LOTTERY_DETAIL = "0y_orderdp";
    //0元抽奖每日领取
    public static final String EVENT_LOTTERY_GET_CODE_EVERYDAY = "0y_everyday";
    //0元抽奖积分兑换抽奖码
    public static final String EVENT_LOTTERY_GET_CODE_EXCHANGE = "0y_score";
    //0元抽奖设置提醒
    public static final String EVENT_LOTTERY_SET_ALARM = "0y_remind";

    //进入启动页面
    public static final String EVENT_OPEN_SPLASH = "r";
    public static String DanPin="danpin";
    public static String PinPai="pinpai";
    //母婴点击选择宝宝
    public static final String MUYING_BABY_DIALOG_CLICK = "babyset";

    //开卖提醒
    public static final String EVENT_SELL_TIP_EMPTY_TO_BOUTIQUE_NOTICE = "rselect";
    public static final String EVENT_SELL_TIP_EDIT_COMPLETE_CLICK_CONFIRM = "redit";

    // 底部导航
    public static final String EVENT_BOTTOM_ICON = "bn";

    // 返回列表顶部
    public static final String EVENT_RETURN_TTP = "returnttp";

    //首页Banner下8个宫格新打点
    public static final String EVENT_BOUTIQUE_EIGHT_BUTTOM_CATEGORY_ICFT = "icft";
    //点击进入品牌团
    public static final String EVENT_BRAND_CLICK="brand";

    //点击进入主题馆
    public static final String EVENT_THEME_CLICK="theme";

    //scheme
    public static final String EVENT_SCHEME="scheme";

    public AnalyticsStatic(Context context) {
        super(context);
    }

    @Override
    public String getLogHeader() {
        return super.getLogHeader();
    }

    @Override
    protected String getLongitude() {
        return null;
    }

    @Override
    protected String getLatitude() {
        return null;
    }


    @Override
    protected String getUserId() {
        return null;
    }

    @Override
    protected String getCityId() {
        return null;
    }


    @Override
    protected String getPhoneNum() {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

}
