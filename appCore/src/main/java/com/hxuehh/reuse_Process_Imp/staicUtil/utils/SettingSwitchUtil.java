package com.hxuehh.reuse_Process_Imp.staicUtil.utils;

import android.util.Log;

import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.appSetting.UrlConfigDomain.TaoAPI;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.NetworkWorker;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.HttpRequester;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14-1-15
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class SettingSwitchUtil {

    public static int taoBaoConvenientStatus; // 淘宝充值，买彩票显示开关 0:充值彩票都没有 1:充值 2:彩票 3:充值彩票都有
    public static int weixinScore; // 关注微信添加积分数
    public static int qqSpaceScore;// 关注QQ空间加积分数
    public static int registerScore;  //邀请好友加积分数

    public static boolean wxAttendSwitch = false; //微信关注开关；
    public static String signRuleTip = "";

    public static int taoBaoPartnerApi = -1; // 淘宝登录API是否关闭：0 表示已经关闭， 1表示是开放的
    public static int taoBaoPwdSetted = -1; //淘宝用户合并之后是否设置了密码： 0 未设置 1 设置了
    public static String taoBaoMergedNumber = ""; //淘宝用户被合并的手机号

    public static boolean cookie_cet;


    public static int saleSwitch = 0;// 销量开关,0关闭,1打开

    public static int hitEggSwitch = 0;// 砸蛋开关,

    public static int hotCategorySwitch = 1;//一级分类首页的热键品类开关 1开 0关  默认开
    public static int categoryTopicSwitch = 1;//一级分类首页的分类专题开关 1开 0关  默认开
    public static int hotBrandSwitch = 1;//一级分类首页的品牌特卖开关 1开 0关  默认开
    public static int newUserGiftSwitch = 1;//首页的新人礼专题开关 1开 0关  默认开
    public static int recommendSwitch = 0;//应用推荐积分墙  1开 0关  默认关
    public static int xingePushSwitch = 0;//信鸽PUSH开关

    public static void initSwitch() {
        qqSpaceScore = 0;
        weixinScore = 0;
    }

    // 初始化淘宝绑定手机号状态
    public static void initTaoBaoBindStatus() {
        taoBaoPartnerApi = -1;
        taoBaoPwdSetted = -1;
        taoBaoMergedNumber = "";
    }

    public static int getNewUserGiftSwitch() {
        return newUserGiftSwitch;
    }

    public static void getSwitchConfig() {

        HttpRequester requester = new HttpRequester();
        HashMap<String, Object> params = new HashMap<String, Object>();
        String switchKeys = "TaoCC.categorysite.subcategory.switch," +
                "TaoCC.categorysite.filter.switch," +
                "TaoCC.categorysite.brands.switch," +
                "TaoCC.signin.newusergift.switch," +
                "TaoCC.apprecommend.switch," +
                "TaoCC.xinge.push.switch," +

                "TaoCC.weixin.interest.points," +
                "TaoCC.weixin.interest.force.switch," +
                "TaoCC.qzone.interest.points," +
                "TaoCC.friend.invite.points," +
                "TaoCC.taobao.tmall.sale.switch," +

                "TaoCC.taobaologin.url," +
                "TaoCC.firstorder.rebate.switch," +
                "TaoCC.firstorder.rebate.url," +
                "TaoCC.imageexposure.statistics.switch," +
                "TaoCC.handletaobaojumpfail.switch," +
                "TaoCC.customdetailurl.switch," +
                "TaoCC.shop.config," +
                "TaoCC.exitapp.push.switch," +
                "TaoCC.exitapp.push.url," +
                "TaoCC.telecom.presentedtraffic.switch," +
                "TaoCC.order.gift.switch," +
                "TaoCC.invitefriends.switch";

        params.put("keys", switchKeys);
        params.put("platform", "android");
        params.put("trackid", AppConfig.getInstance().PARTNER_ID);
        params.put("product", AppConfig.getInstance().PRODUCT_TAG);
        requester.setParams(params);

        NetworkWorker.getInstance().post(TaoAPI.getNetwork().SWITCH_SOMEONE, new NetworkWorker.ICallback() {
            @Override
            public void onResponse(int status, String result) {
                if (status != 200) {
                    return;
                }
                checkKeys(result);
            }
        }, requester);
    }

    private static void checkKeys(String result) {
        try {
            JSONArray JArray = new JSONArray(result);
            for (int i = 0, count = JArray.length(); i < count; i++) {
                JSONObject jObj = JArray.getJSONObject(i);
                String key = jObj.optString("key");
                if ("TaoCC.categorysite.subcategory.switch".equals(key)) {
                    hotCategorySwitch = jObj.optInt("content");
                }
                if ("TaoCC.categorysite.filter.switch".equals(key)) {
                    categoryTopicSwitch = jObj.optInt("content");
                }
                if ("TaoCC.categorysite.brands.switch".equals(key)) {
                    hotBrandSwitch = jObj.optInt("content");
                }
                if ("TaoCC.signin.newusergift.switch".equals(key)) {
                    newUserGiftSwitch = jObj.optInt("content");
                }
                if ("TaoCC.apprecommend.switch".equals(key)) {
                    recommendSwitch = jObj.optInt("content");
                }
                if ("TaoCC.xinge.push.switch".equals(key)) {
                    xingePushSwitch = jObj.optInt("content");
                }

                if ("TaoCC.weixin.interest.points".equals(key)) {
                    weixinScore = jObj.optInt("content");
                }

                if ("TaoCC.weixin.interest.force.switch".equals(key)) {
                    wxAttendSwitch = 1 == (jObj.optInt("content"));

                }

                if ("TaoCC.qzone.interest.points".equals(key)) {
                    qqSpaceScore = jObj.optInt("content");
                }

                if ("TaoCC.friend.invite.points".equals(key)) {
                    registerScore = jObj.optInt("content");
                }

                // 销量开关
                if ("TaoCC.taobao.tmall.sale.switch".equals(key)) {
                    // saleSwitch = jObj.optInt("content");
                    boolean isOpen = false;
                    isOpen = !StringUtil.isEmpty(jObj.optString("content")) && "1".equals(jObj.optString("content"));
                    if (isOpen) {
                        saleSwitch = 1;
                    }
                }


            }

        } catch (JSONException e) {
            Log.d("qjb","qjb----解析开关项异常");
        }
    }
}
