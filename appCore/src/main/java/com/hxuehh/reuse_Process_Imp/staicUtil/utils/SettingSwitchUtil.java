package com.hxuehh.reuse_Process_Imp.staicUtil.utils;

import android.util.Log;

import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.appSetting.UrlConfigDomain.Tao800API;
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
    public static int zhe800_h5_pj = 1;

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
        String switchKeys = "tao800.categorysite.subcategory.switch," +
                "tao800.categorysite.filter.switch," +
                "tao800.categorysite.brands.switch," +
                "tao800.signin.newusergift.switch," +
                "tao800.apprecommend.switch," +
                "tao800.xinge.push.switch," +

                "tao800.weixin.interest.points," +
                "tao800.weixin.interest.force.switch," +
                "tao800.qzone.interest.points," +
                "tao800.friend.invite.points," +
                "tao800.taobao.tmall.sale.switch," +

                "tao800.taobaologin.url," +
                "tao800.firstorder.rebate.switch," +
                "tao800.firstorder.rebate.url," +
                "tao800.imageexposure.statistics.switch," +
                "tao800.handletaobaojumpfail.switch," +
                "tao800.customdetailurl.switch," +
                "tao800.shop.config," +
                "tao800.exitapp.push.switch," +
                "tao800.exitapp.push.url," +
                "tao800.telecom.presentedtraffic.switch," +
                "tao800.order.gift.switch," +
                "tao800.invitefriends.switch";

        params.put("keys", switchKeys);
        params.put("platform", "android");
        params.put("trackid", AppConfig.getInstance().PARTNER_ID);
        params.put("product", AppConfig.getInstance().PRODUCT_TAG);
        requester.setParams(params);

        NetworkWorker.getInstance().post(Tao800API.getNetwork().SWITCH_SOMEONE, new NetworkWorker.ICallback() {
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
                if ("tao800.categorysite.subcategory.switch".equals(key)) {
                    hotCategorySwitch = jObj.optInt("content");
                }
                if ("tao800.categorysite.filter.switch".equals(key)) {
                    categoryTopicSwitch = jObj.optInt("content");
                }
                if ("tao800.categorysite.brands.switch".equals(key)) {
                    hotBrandSwitch = jObj.optInt("content");
                }
                if ("tao800.signin.newusergift.switch".equals(key)) {
                    newUserGiftSwitch = jObj.optInt("content");
                }
                if ("tao800.apprecommend.switch".equals(key)) {
                    recommendSwitch = jObj.optInt("content");
                }
                if ("tao800.xinge.push.switch".equals(key)) {
                    xingePushSwitch = jObj.optInt("content");
                }

                if ("tao800.weixin.interest.points".equals(key)) {
                    weixinScore = jObj.optInt("content");
                }

                if ("tao800.weixin.interest.force.switch".equals(key)) {
                    wxAttendSwitch = 1 == (jObj.optInt("content"));

                }

                if ("tao800.qzone.interest.points".equals(key)) {
                    qqSpaceScore = jObj.optInt("content");
                }

                if ("tao800.friend.invite.points".equals(key)) {
                    registerScore = jObj.optInt("content");
                }

                // 销量开关
                if ("tao800.taobao.tmall.sale.switch".equals(key)) {
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
