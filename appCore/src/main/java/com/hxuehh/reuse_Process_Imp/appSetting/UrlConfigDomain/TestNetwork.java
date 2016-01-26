package com.hxuehh.reuse_Process_Imp.appSetting.UrlConfigDomain;

/**
 * Created by IntelliJ IDEA.
 * User: tianyanlei
 * Date: 2014 14-6-23
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates
 */
public class TestNetwork extends BaseNetwork {

    public TestNetwork() {
        BASE_API = "http://m.api.xiongmaoz.com/";
        //BASE_API = "http://192.168.10.142/";

//        BASE_Z_API = "http://zapi.zhe800.com/";

//        BASE_Z_API = "http://zapi.xiongmaoz.com/";
        BASE_Z_API = "http://zapi.zhe800.com/";
        BASE_BUY_API = "http://buy.m.xiongmaoz.com/";

        // zhe800wap页、apk下载地址、新浪、腾讯微博地址、用户反馈
        TAO800_WAP = "http://m.zhe800.com";
        APK_URL = "http://m.zhe800.com/download";
        APK_NEW_URL = "http://weibo.com/p/100404127430";
        TAO800_SINA_WEIBO = "http://weibo.com/tao800ju";
        TAO800_TENCENT_WEIBO = "http://t.qq.com/tao800wang";
        FEEDBACK_URL = "http://m.api.zhe800.com/user/feedback";

        // 砸蛋介绍
        ZADAN_URL = "http://h5.m.xiongmaoz.com/ms/zhe800hd/app/zadan/zadan_intro.html";
        // 签到
        SIGN_URL = BASE_Z_API + "checkins";

        // 获取启动海报数据
        START_INFO = BASE_API + "tao800/startinfo.json";

        // 获取分类数据
        CATEGORY_URL = BASE_API + "v6/tags";

        // 推荐分类
        RECOMEND_CATEGORY_URL = BASE_API + "v3/tags/recommend";

        // shop收藏/获取shop收藏列表/取消收藏
        SHOP_FAVOR_URL = BASE_Z_API + "cn/shop_concerns";

        // deal收藏/获取deal收藏列表/取消收藏
        DEAL_FAVOR_URL = BASE_Z_API + "cn/favorites";

        // deal收藏H5详情
        DEAL_H5_URL = BASE_API + "h5/shopdeal?id=";

        // 分类  v3.6.3全部升级成v4
       // CATEGORY_DEAL_URL = BASE_API + "v3/deals";

        // 分类 V4
        CATEGORY_DEAL_URL_V4 = BASE_API + "v4/deals";

        // 分类今日更新
        CATEGORY_DEAL_TODAY_URL = BASE_API + "v3/deals/today";

        // 品牌团
        BRAND_GROUP_URL = BASE_API + "v2/brand/branddeals";

        // 今日精选数据
        TADAY_DEALS_URL = BASE_API + "tao800/prdofcategory.json";

        // 手机周边
        PHONE_NEAR_URL = BASE_API + "v3/mobile/deals";
        PHONE_NEAR_CATEGORY_URL = BASE_API + "v3/mobile/tags";

        // 精品预告数据
        JINGPIN_DEALS_URL = BASE_API + "v2/forecast/deals";

        // 明日预告数据
        TOMORROW_DEALS_URL = BASE_API + "tao800/prdoftomorrow.json";

        // 根据ids获取deal列表
        SYNC_SELL_DEAL = BASE_API + "v4/deals/ids";

        SYNC_SELL_DEAL_V2 = BASE_API + "v5/deals/ids";

        // 根据ids获取值得逛专题cpc商品列表
        ZHI_CPC_DEAL = BASE_API + "v3/guang/ids";

        // 专题搜索商品列表
        TOPIC_SELL_DEAL = BASE_API + "v4/search";

        //商品搜索列表
        DEAL_SEARCH_LIST = BASE_API + "v4/search";

        // 我的礼品
        USER_GIFT_ORDER_DEAL = BASE_Z_API + "profile/orders/";

        // 我的返积分订单
        GET_TRADE_LIST = BASE_Z_API + "trade/list";

        // 标识返积分订单已读
        TRADE_CHANGE_STATUS = BASE_Z_API + "trade/change_status";

        //正在热卖
        GET_SELLING_DEAL = BASE_Z_API + "deals/hot_deals";
        //即将开卖
        TO_SELLING_DEAL = BASE_Z_API + "deals/will_hot_deals";

        // 统计开卖提醒次数
        SELL_DEAL_TIP_COUNT = "http://www.zhe800.com/cn/deal_subscibe";

        // 活动、push相关
        BANNER_V2_URL = BASE_API + "tao800/bannerv2.json";

        HOT_BANNER_URL = BASE_API + "tao800/hotbanner.json";

        PUSH_URL = BASE_API + "pushv3.2/public";

        // 注册邀请好友
//        ACTIVITE_REGISETER_URL = "http://w.tuan800.com/zhe800/yqhyzc/";
        ACTIVITE_REGISETER_URL = "http://m.zhe800.com/download_weixinshare/";

        // 邀请好友加积分
        ACTIVITE_REGISETER_ADD_INTEGRAL = "http://m.api.tuan800.com/integral/invite_award";

        // 值得逛相关
        ZHI_DEAL_URL = BASE_API + "v2/guang/deals";
        ZHI_CATEGORY_URL = BASE_API + "v2/guang/tags";

        // 商品竞拍详情
        AUCTION_DEAL_DETAIL = "http://zapi.zhe800.com/auction/";

        // 商品竞拍记录
        AUCTION_DEAL_RECORD = BASE_Z_API + "cn/tao_record_deals/auction";

        // 商品竞拍出价
        AUCTION_DEAL_INTEGRAL = "http://www.zhe800.com/jifen/auction/chujia";

        // 竞拍商品列表
        AUCTION_DEAL_LIST = "http://zapi.zhe800.com/auction";

        // 积分竞拍规则
//        AUCTION_RULE = " http://docs.tuan800-inc.com/ued-svn/mobile-wap/tao800/rules31883.html";
        AUCTION_RULE = "http://s.zhe800.com/ms/zhe800hd/app/client/jifenrules.html";

        // 巨能砍url
        CUT_PRICE_URL = "http://m.zhe800.com/hd/sale_acts";

        // 大促banner
        PROMOTION_SALE_URL = BASE_API + "tao800/salebanner.json";

        // 大促商品列表
        PROMOTION_SALE_DEAL_URL = BASE_API + "v3/deals/promotion";

        // 朝九晚八提示
        DATA_COUNT_TIP_URL = BASE_API + "v3/deals/count/zaojiuwanba";

        // 订单列表页url
        USER_ORDER_WEB_URL = "http://h5.m.xiongmaoz.com/orders/h5/get_order_list";

        // 我的优惠券列表
        USER_COUPON_WEB_URL = "http://h5.m.xiongmaoz.com/h5/coupons/my";

        // FAQ提示地址
        WEBVIEW_FAQ_URL = "http://s.zhe800.com/ms/zhe800hd/app/jfintro/helper.html";
        WEBVIEW_FAQ_URL_PRICE = "http://w.tuan800.com/zhe/faq#jiage";
        WEBVIEW_FAQ_URL_VIP = "http://w.tuan800.com/zhe/faq#vip";
        WEBVIEW_FAQ_URL_XIAJIA = "http://w.tuan800.com/zhe/faq#xiajia";


        // -------------------------------- passport ----------------------------------
//    // 登录passport地址

        //PASSPORT_BASE_URL = "https://passport.xiongmaot.com/m/";
        PASSPORT_BASE_URL = "http://passport.xiongmaot.com/m/";


        // 用户注册passport
        PASSPORT_REGISTER_URL_HTTPS = PASSPORT_BASE_URL + "users";

        // 获取验证码
        PASSPORT_LOGIN_CAPTCHA = PASSPORT_BASE_URL + "captcha_img.json?";

        // 用户登录passport
        PASSPORT_LOGIN_URL_HTTPS = PASSPORT_BASE_URL + "sessions";

        // 获取验证码passport
        PASSPORT_GET_VERIFY_CODE_HTTPS = PASSPORT_BASE_URL + "phone_confirmations";

        // 重设密码passport
        PASSPORT_RESET_PASSWORD_HTTPS = PASSPORT_BASE_URL + "passwords";


        // 绑定手机号
        PASSPORT_BIND_PHONE_HTTPS = PASSPORT_REGISTER_URL_HTTPS + "/bind_phone_number";

        // tuan800升级提醒地址
        TUAN800_SOFT_URL = "http://m.api.tuan800.com/api/checkconfig/v3/soft";


        // ------------------------------- 积分 ---------------------------------
        // 增加积分接口
        ADD_INTEGRAL_POINT = BASE_API + "integral/point?";

        // 获取我的积分
        GET_INTEGRAL_POINT = BASE_API + "integral/user?";

        // 积分攻略
        INTEGRAL_RULE = "http://m.api.zhe800.com/integral/rule?product=tao800";

        // 积分说明
        JIFENSHUOMING_URL = "http://h5.m.xiongmaoz.com/ms/zhe800hd/app/jfintro/index.html";

        // 积分历史
        INTEGRAL_HISTORY = BASE_API + "integral/bill?";

        // 积分历史(有效积分部分)
        INTEGRATION_HISTORY = BASE_Z_API + "cn/score_histories";

        //下载应用加积分
        DOWNLOADAPKFORINTEGRAL = "http://m.api.tuan800.com/recommendv2/downloadApp";

        // 有效积分信息
        INTEGRATION_ACCOUNTS = "http://m.api.tuan800.com/mobile_api/v3/score_accounts";

        INTEGRATION_PASSPORT_ACCOUNTS = "http://sso.zhe800.com/m/jump_to";

        //是否关注微信公共账号，未关注获得邀请码
        INVITATION_CODE = TAO800_WAP + "/hd/wx_follow_check";

        //是否关注QQ空间公共账号，未关注获得邀请码
        QQSPACE_INVITATION_CODE = "http://zapi.zhe800.com/user/qq_follow_check";

        // 获取积分兑换或抽奖规则说明
        GET_RULES_WELFARE_RAFFLE = BASE_API + "tao800/ruledesc.json";

        //积分现金购详情
        WELFARE_INTEGRAL_BUY_TYPE = BASE_API + "v3/jifendeal";

        // 获取积分兑换、积分抽奖商品列表
        WELFARE_BUY_LIST_TYPE = BASE_Z_API + "welfare/all";

        WELFARE_TYPE = BASE_API + "welfare/";
        RAFFLE_TYPE = BASE_API + "raffle/";
        GET_WELFARE_DEAL = WELFARE_TYPE + "all";
        GET_RAFFLE_DEAL = RAFFLE_TYPE + "all";

        // 获取用户等级
        GET_USER_GRADE = BASE_Z_API + "profile/grade";

        // 会员等级特权URL
        WEBVIEW_MEMBER_GRADE = "http://s.zhe800.com/ms/zhe800hd/app/client/jifendengji.html";

        //获取应用推荐
        RECOMMENDATION_URL = "http://m.api.tuan800.com/recommendv2/api/v1/recommendv2/";

        //获取搜索推荐
        SEARCH_RECOMMEND_URL = "http://m.api.tuan800.com/v2/suggestion";

        //搜索无商品时deal推荐
        SEARCH_GUESS_URL = BASE_API + "v3/deals/recommend";

        // 获取各个网站的信息，主要用于淘宝的相关跳转
        LOAD_SITE_INFO = "http://m.api.zhe800.com/tao800/clientcontrol/android/1/client.json";

        // 淘宝手机充值
        TAOBAO_PHONE_RECHARGE = "http://out.zhe800.com/m/chongzhi";

        // 淘宝买彩票
        TAOBAO_CAIPIAO = "http://out.zhe800.com/m/caipiao";

        // 淘宝帐号手机绑定查询
        TAOBAO_BIND_PHONE_LNQUIRE = "http://passport.tuan800.com/account/wap_merge/login";

        // 淘宝帐号合并
        TAOBAO_PHONE_MERGER = "http://sso.zhe800.com/m/jump_to?return_to=http://passport.tuan800.com/account/wap_merge/bind_info";

        // 获取用户推荐IDs
        LOAD_USER_LIKE_IDS = "http://zapi.zhe800.com/v1/cn/get_cid";

        // 手机充值创建订单接口
        RECHARGE_CREATE_ORDER_URL = BASE_BUY_API + "orders/create?";

        // 获取订单列表接口
        RECHARGE_ORDER_LIST_URL = BASE_BUY_API + "orders/get_order_list";

        // 手机充值获取支付信息接口
        RECHARGE_PAY_ORDER_URL = BASE_BUY_API + "orders/pay?";

        // 获取充值商品列表
        RECHARGE_LIST_URL = BASE_API + "recharge/";

        //订单相关

        //订单列表
        ORDER_LIST = BASE_BUY_API + "orders/credits/query";

        //订单详情
        ORDER_DETAIL = BASE_BUY_API + "orders/credits/detail";

        // 删除订单接口
        ORDER_DEL_URL = BASE_BUY_API + "orders/credits/delete";

        // 订单取消接口
        ORDER_CANCEL_URL = BASE_BUY_API + "orders/credits/cancel";

        // 确认收货接口
        CONFIRM_RECEIPT_URL = BASE_BUY_API + "orders/credits/confirm";

        // 收货地址相关
        //获取收货地址列表
        ADDRESS_LIST = BASE_BUY_API + "address";
        //添加新的收货地址
        ADD_NEW_ADDRESS = BASE_BUY_API + "address/add";
        //删除收货地址
        DELETE_ADDRESS = BASE_BUY_API + "address/delete";
        //设置默认收货地址
        DEFAULT_ADDRESS = BASE_BUY_API + "address/default";
        //编辑收货地址
        EDIT_ADDRESS = BASE_BUY_API + "address/edit";

        //曝光打点新接口
        EVENT_EXPOSE = "http://analysis.tuanimg.com/v1/global/img/a.gif";

        // 天猫登录页面
        TAOBAO_LOGIN_URL = "http://login.m.taobao.com/login.htm";

        // 品牌团列表
        BRAND_GROUP_URL = BASE_API + "v2/brand/branddeals";

        //品牌团商品列表
        BRAND_GROUP_DEALS_URL = BASE_API + "v3/brand/getdealsbyid";

        // 商品保证URL
        DEAL_INDEMENDITY_URL = "http://s.zhe800.com/ms/zhe800hd/app/xb/xb.html";

        //每日十件列表
        TODAY_TENDEALS_URL = BASE_API + "v3/tendeals";

        // 首页底部分类URL
        BOTTOM_CATEGORY_URL = BASE_API + /*"v3/homesetting"*/ "v4/homesetting";

        //获取用户优惠券信息URL
        GET_USER_COUPON_URL = "http://th5.m.zhe800.com/h5/api/coupons/usercoupons";


        // 检查是否是推广码
        CHECK_PROMO_CODE_URL = BASE_API + "campus/promotion/validatePromoCode";

        // 校园专题列表页
        SCHOOL_DEAL_URL = BASE_API + "v3/deals/taocampus";

        //校园激活
        ACTIVATE_SCHOOL_URL = "http://campus.tuan800.com/woss/campus?";

        //升级提醒
        REMOTE_VERSION_URL = BASE_API + "api/checkconfig/v3";

        //0元抽奖Banner
        GET_BANNER_OF_LOTTERY = BASE_API + "lottery/zero";

        //0元抽奖获取抽奖详情
        GET_LOTTERY_DETAIL_INFO = BASE_API + "lottery/";

        //0元抽奖参与抽检返回抽奖码
        DO_LOTTERY_GET_CODE = BASE_API + "lottery/draw";

        //查看我的抽奖列表
        GET_MY_LOTTERYLIST = BASE_API + "lottery/my";
        //查看我的详情
        GET_MY_LOTTERYDETAL = BASE_API + "lottery/my/detail";


        // 根据deal_id获取抽奖详情V2
        GET_LOTTERY_BY_ID_V2 = BASE_API + "lottery/v2";
        // 抽奖，返回抽奖码V2
        DRAW_LOTTERY_V2 = BASE_API + "lottery/draw/v2";
        // 查看我的抽奖列表V2
        GET_MY_LOTTERY_LIST_V2 = BASE_API + "lottery/my/v2";
        // 查看我的抽奖详情V2
        GET_MY_LOTTERY_DETAIL_V2 = BASE_API + "lottery/my/detail/v2";

        //查看我的抽奖列表v3
        GET_MY_LOTTERY_LIST_V3 = BASE_Z_API + "cn/inner/lottery/mylotteries";
        //抽奖列表点击查看抽奖订单详情v3
        GET_MY_LOTTERY_DETAIL_V3 = BASE_Z_API + "cn/inner/lottery/order/";
        //下期抽奖v3
        GET_NEXT_LOTTERY = BASE_Z_API + "cn/inner/lottery/comming";
        // 抽奖动作
        DRAW_LOTTERY_V3 = BASE_Z_API + "cn/inner/lottery/";
        // 抽奖详情
        GET_LOTTERY_DETAIL_INFO_V3 = BASE_Z_API + "cn/inner/lottery/processing";

        // 砸蛋配置
        GET_EGG_CONFIG = BASE_API + "hitegg/geteggconfig.json";

        // 砸蛋抽奖
        GET_EGG_PRIZE = BASE_API + "hitegg/geteggprize.json";

        // 中奖记录
        GET_PRIZE_INFO = BASE_API + "hitegg/getprizeinfo.json";

        // 中奖手机号添加
        SET_PRIZE_MOBILE = BASE_API + "hitegg/setprizemobile.json";

        // 我的购物车
        GET_USER_SHOP_CART = "  http://h5.m.xiongmaoz.com/h5/cart/list/my";

//     品牌
        BRAND_GROUP_One_URL=BASE_API+"v5/brand/getdealsbyid";
//        母婴品牌_商品列表页
        MuYing_BRAND_GROUP_One_URL = BASE_API + "v1/muying/brand/deal/default";

        //    母婴成都接口
        Bir_MuYing_BRAND_GROUP_One_URL=BASE_Z_API+"muying/brand/deal/custom";

//        母婴分类接口
        MuYing_CATEGORY_URL = BASE_API + "v1/muying/tags";

        Brand_Group_CATEGORY_URL=BASE_API+"v5/brand/tags";

        // 分享文案
        GET_SOCIAL_SHARE_CONTENT = BASE_API + "socialshare/content";

        GET_MUYING_NEW_COUNT = BASE_API + "v1/muying/count";

        // --------------心愿单 start------------------
        // 创建心愿单
        WISH_LISTS_CREATE = BASE_Z_API + "wish_lists/create";
        // 心愿单删除
        WISH_LISTS_DESTROY = BASE_Z_API + "wish_lists/destroy";
        // 心愿单列表
        WISH_LISTS_INDEX = BASE_Z_API + "cn/" + "wish_lists/index";
        // 是否有心愿单达成
        WISH_LISTS_REACHED = BASE_Z_API + "cn/" + "wish_lists/reached";
        // 心愿单轮询
        WISH_LISTS_PUSH_ANDROID = BASE_Z_API + "cn/" + "wish_lists/push_android";
        // 心愿单批量添加
        WISH_LISTS_BATCH_CREATE = BASE_Z_API + "wish_lists/batch_create";
        // 心愿单批量删除
        WISH_LISTS_BATCH_DESTROY = BASE_Z_API + "wish_lists/batch_destroy";
        // --------------心愿单 end------------------



        IM = "http://im.xiongmaoz.com/com.tuan800.im.userCenter";
        IM_IMAGE = "http://im.xiongmaoz.com/com.tuan800.im.fileManager";
        IM_Host = "im.xiongmaoz.com";

        //为WEB提供离线消息数量查询接口，responsecode=_200表示成功,其他情况返回错误标示的code
        offlineMessageCount = IM + "/im/chatMessage/offlineMessageCount";

        //message获取某人聊天历史纪录列表
//    用户和商户之间聊天记录查询（包括接受和发送的消息），消息按照最近时间进行排序，
//    可按照指定时间段进行查询，可设置查询页数和页码尺寸，responsecode=_200表示成功,其他情况返回错误标示的code
        queryBussmessage = IM + "/im/chatMessage/queryBussmessage";
        queryMessage = IM + "/im/chatMessage/queryMessage";

//    为WEB提供离线消息清空接口，responsecode=_200表示成功,其他情况返回错误标示的code,
// 返回数据格式：{"data":true,"errorinfo":"","responsecode":"_200"}
        clearOfflineMessage = IM + "/im/chatMessage/clearOfflineMessage";
        ImLoagin = IM + "/im/user/loginUser";
//    查询最近联系人信息，按照数组形式返回，包含聊天人jid和最后聊天时间responsecode=_200表示成功,其他情况返回错误标示的code
        recentContacts = IM + "/im/recentContacts/findRecentContacts";
//    IM系统创建普通用户
        userCreateAccount = IM + "/im/user/userCreateAccount";
        //    查询单个用户信息
        userInfo = IM + "/im/user/userInfo";
        //   图片上传
        uploadImage = IM_IMAGE + "/im/upload/uploadImage";
        //获取客服信息
        IM_ACCOUNT_INFO = BASE_API + "im/imaccountinfo.json";
        //获取im所需pwd和jid接口
        IM_PWD_URL = BASE_API + "im/getuserbycookies";


//电信流量
        DIANXIN_VILADECODE=BASE_API+"ctcc/flow/sms";
        DIANXIN_GETLIULIANG=BASE_API+"ctcc/flow/order";

        HOME_PROMOTION = BASE_API + "homepromotion/v1";

        // 首页虚拟试衣间入口
        DRESSROOM_BANNER = BASE_API +  "dressroom/banner";

        // -- 送礼订单
        GET_GIVE_NEW_GIFTS = "http://th5.m.zhe800.com/h5/gift/getorderslist";

        QIN_NIU_TOKEN = BASE_API + "qiniu/uptoken";

        SIGN_IS_NEED_REVIVAL = ""; // 签到复活状态获取地址

        INVITE_FRIEND=BASE_API +"h5/cn/hongbao/invite_page";//邀请好友

        ACTIVE_INFO = "http://192.168.10.165:8080/mobilelog/activelog/v2/activeinfo";


        //返回商品总数接口
        NEWCOUNT = BASE_API + "v3/deals/count/zaojiuwanba";

        // 小米push上传regId
        DEVICE_INFO = BASE_API + "push/deviceinfo";

        SWITCH_SOMEONE=BASE_API+"config/switch";

        //分类页主题馆获取
        CATEGORY_THEME_URL = BASE_API+"theme/hot";
        //分类页品牌墙获取
        CATEGORY_BRAND_URL = BASE_API+"v6/brand/wall";

        URL_CHECK_GIFT = BASE_API + "checkingift/screen";

        //精选预告H5页面
        HOME_JINGXUAN_ADVANCE = "http://h5.m.zhe800.com/m/forecast/foreshow?pub_page_from=zheclient";
    }
}
