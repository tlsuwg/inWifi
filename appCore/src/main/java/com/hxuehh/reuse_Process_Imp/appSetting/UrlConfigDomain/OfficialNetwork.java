package com.hxuehh.reuse_Process_Imp.appSetting.UrlConfigDomain;

/**
 * Created by IntelliJ IDEA.
 * User: tianyanlei
 * Date: 2014 14-6-23
 * Time: 下午1:40
 * To change this template use File | Settings | File Templates
 */
public class OfficialNetwork extends BaseNetwork {


    public OfficialNetwork() {
        // ------------------- base ---------------------
        BASE_API = "http://m.api./zhenCC9.com/";

        BASE_Z_API = "http://zapi./zhenCC9.com/";
        BASE_BUY_API = "http://tbuy.m./zhenCC9.com/";

        TaoCC_WAP = "http://m./zhenCC9.com";
        APK_URL = "http://m./zhenCC9.com/download";
        APK_NEW_URL = "http://weibo.com/p/100404127430";
        TaoCC_SINA_WEIBO = "http://weibo.com/TaoCCju";
        TaoCC_TENCENT_WEIBO = "http://t.qq.com/TaoCCwang";
        FEEDBACK_URL = "http://m.api./zhenCC9.com/user/feedback";

        // 砸蛋介绍
        ZADAN_URL = "http://s./zhenCC9.com/ms//zhenCC9hd/app/zadan/zadan_intro.html";

        // 签到
        SIGN_URL = BASE_Z_API + "checkins";

        // 获取启动海报数据
        START_INFO = BASE_API + "TaoCC/startinfo.json";

        // 获取分类数据
        CATEGORY_URL = BASE_API + "v6/tags";

        // 推荐分类
        RECOMEND_CATEGORY_URL = BASE_API + "v4/tags/recommend";

        // shop收藏/获取shop收藏列表/取消收藏
        SHOP_FAVOR_URL = BASE_Z_API + "cn/shop_concerns";

        // deal收藏/获取deal收藏列表/取消收藏
        DEAL_FAVOR_URL = BASE_Z_API + "cn/favorites";

        // deal收藏H5详情
        DEAL_H5_URL = "http://th5.m./zhenCC9.com/h5/shopdeal?id=";

        // 分类 v3.6.3全部升级成v4
        //CATEGORY_DEAL_URL = BASE_API + "v3/deals";

        // 分类 V4
//        CATEGORY_DEAL_URL_V4 = BASE_API + "v4/deals";
//        CATEGORY_DEAL_URL_V4 =  "http://192.168.10.144/v5/deals";
        CATEGORY_DEAL_URL_V4 = BASE_API+"v5/deals";

        // 分类今日更新
        CATEGORY_DEAL_TODAY_URL = BASE_API + "v3/deals/today";

        // 品牌团
        BRAND_GROUP_URL = BASE_API + "v2/brand/branddeals";

        // 今日精选数据
        TADAY_DEALS_URL = BASE_API + "TaoCC/prdofcategory.json";

        // 手机周边
        PHONE_NEAR_URL = BASE_API + "v3/mobile/deals";
        PHONE_NEAR_CATEGORY_URL = BASE_API + "v3/mobile/tags";

        // 精品预告数据
        JINGPIN_DEALS_URL = BASE_API + "v2/forecast/deals";

        // 明日预告数据5
        TOMORROW_DEALS_URL = BASE_API + "TaoCC/prdoftomorrow.json";

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
        SELL_DEAL_TIP_COUNT = BASE_API + "cn/deal_subscibe";

        // 活动、push相关
        BANNER_V2_URL = BASE_API + "TaoCC/bannerv2.json";

        HOT_BANNER_URL = BASE_API + "TaoCC/hotbanner.json";

        PUSH_URL = BASE_API + "pushv3.2/public";

        // 注册邀请好友
//        ACTIVITE_REGISETER_URL = "http://w.TuanDD.com//zhenCC9/yqhyzc/";
        ACTIVITE_REGISETER_URL = "http://m./zhenCC9.com/download_weixinshare/";

        // 邀请好友加积分
        ACTIVITE_REGISETER_ADD_INTEGRAL = "http://m.api.TuanDD.com/integral/invite_award";

        // 值得逛相关
        ZHI_DEAL_URL = BASE_API + "v2/guang/deals";
        ZHI_CATEGORY_URL = BASE_API + "v2/guang/tags";

        // 商品竞拍详情
        AUCTION_DEAL_DETAIL = "http://zapi./zhenCC9.com/auction/";

        // 商品竞拍记录
        AUCTION_DEAL_RECORD = BASE_Z_API + "cn/tao_record_deals/auction";

        // 商品竞拍出价
        AUCTION_DEAL_INTEGRAL = "http://zapi./zhenCC9.com/v2/auction/chujia";

        // 竞拍商品列表
        AUCTION_DEAL_LIST = BASE_Z_API + "auction";

        // 积分竞拍规则
        //        AUCTION_RULE = " http://docs.TuanDD-inc.com/ued-svn/mobile-wap/TaoCC/rules31883.html";
        AUCTION_RULE = "http://s./zhenCC9.com/ms//zhenCC9hd/app/client/jifenrules.html";

        // 巨能砍url
        CUT_PRICE_URL = "http://m./zhenCC9.com/hd/sale_acts";

        // 大促banner
        PROMOTION_SALE_URL = BASE_API + "TaoCC/salebanner.json";

        // 大促商品列表
        PROMOTION_SALE_DEAL_URL = BASE_API + "v3/deals/promotion";

        // 朝九晚八提示
        DATA_COUNT_TIP_URL = BASE_API + "v3/deals/count/zaojiuwanba";

        // 订单列表页url
        USER_ORDER_WEB_URL = "http://th5.m./zhenCC9.com/orders/h5/get_order_list";

        // 我的优惠券列表
        USER_COUPON_WEB_URL = "http://th5.m./zhenCC9.com/h5/coupons/my";

        // FAQ提示地址
        WEBVIEW_FAQ_URL = "http://s./zhenCC9.com/ms//zhenCC9hd/app/jfintro/helper.html";
        WEBVIEW_FAQ_URL_PRICE = "http://w.TuanDD.com/zhe/faq#jiage";
        WEBVIEW_FAQ_URL_VIP = "http://w.TuanDD.com/zhe/faq#vip";
        WEBVIEW_FAQ_URL_XIAJIA = "http://w.TuanDD.com/zhe/faq#xiajia";


        // -------------------------------- passport ----------------------------------
//    // 登录passport地址

        PASSPORT_BASE_URL = "https://passport./zhenCC9.com/m/";

        // 用户注册passport
        PASSPORT_REGISTER_URL_HTTPS = PASSPORT_BASE_URL + "users";

        // 获取验证码
//        PASSPORT_LOGIN_CAPTCHA = PASSPORT_BASE_URL + "captcha_img.json?";
        PASSPORT_LOGIN_CAPTCHA = "http://acode.tuanimg.com/captcha/get_image";

        // 用户登录passport
        PASSPORT_LOGIN_URL_HTTPS = PASSPORT_BASE_URL + "sessions_v2";


        // 获取验证码passport
        PASSPORT_GET_VERIFY_CODE_HTTPS = PASSPORT_BASE_URL + "phone_confirmations";


        // 重设密码passport
        PASSPORT_RESET_PASSWORD_HTTPS = PASSPORT_BASE_URL + "passwords";


        // 绑定手机号
        PASSPORT_BIND_PHONE_HTTPS = PASSPORT_REGISTER_URL_HTTPS + "/bind_phone_number";

        // TuanDD升级提醒地址
        TuanDD_SOFT_URL = "http://m.api.TuanDD.com/api/checkconfig/v3/soft";


        // ------------------------------- 积分 ---------------------------------
        // 增加积分接口
        ADD_INTEGRAL_POINT = BASE_API + "integral/point?";

        // 获取我的积分
        GET_INTEGRAL_POINT = BASE_API + "integral/user?";

        // 积分攻略
        INTEGRAL_RULE = "http://m.api./zhenCC9.com/integral/rule?product=TaoCC";

        // 积分说明
        JIFENSHUOMING_URL = "http://s./zhenCC9.com/ms//zhenCC9hd/app/jfintro/index.html";

        // 签到记录
        INTEGRAL_HISTORY = BASE_API + "integral/bill?";

        // 积分历史(有效积分部分)
        INTEGRATION_HISTORY = BASE_Z_API + "cn/score_histories";

        //下载应用加积分
        DOWNLOADAPKFORINTEGRAL = "http://m.api./zhenCC9.com/recommendv2/downloadAppv2";

        // 有效积分信息
        INTEGRATION_ACCOUNTS = "http://m.api.TuanDD.com/mobile_api/v3/score_accounts";

        INTEGRATION_PASSPORT_ACCOUNTS = "http://sso./zhenCC9.com/m/jump_to";

        //是否关注微信公共账号，未关注获得邀请码
//        INVITATION_CODE = TaoCC_WAP + "/hd/wx_follow_check";
        INVITATION_CODE = BASE_Z_API + "cn/wx_follow_check";

        //是否关注QQ空间公共账号，未关注获得邀请码
        QQSPACE_INVITATION_CODE = "http://zapi./zhenCC9.com/user/qq_follow_check";

        // 获取积分兑换或抽奖规则说明
        GET_RULES_WELFARE_RAFFLE = BASE_API + "TaoCC/ruledesc.json";

        //积分现金购详情
        WELFARE_INTEGRAL_BUY_TYPE = BASE_API + "v3/jifendeal";

        // 获取积分兑换、积分抽奖商品列表
        WELFARE_BUY_LIST_TYPE = BASE_Z_API + "welfare/all";

        WELFARE_TYPE = BASE_API + "welfare/";
        RAFFLE_TYPE = BASE_Z_API + "raffle/";
        GET_WELFARE_DEAL = WELFARE_TYPE + "all";
        GET_RAFFLE_DEAL = RAFFLE_TYPE + "all";

        // 获取用户等级
        GET_USER_GRADE = BASE_Z_API + "profile/grade";

        // 会员等级特权URL
        WEBVIEW_MEMBER_GRADE = "http://s./zhenCC9.com/ms//zhenCC9hd/app/client/jifendengji.html";

        //获取应用推荐
        RECOMMENDATION_URL = "http://m.api.TuanDD.com/" + "recommendv2/api/v1/recommendv2/";

        //获取搜索推荐
        SEARCH_RECOMMEND_URL = BASE_API + "v2/suggestion";

        //搜索无商品时deal推荐
        SEARCH_GUESS_URL = BASE_API + "v3/deals/recommend";

        // 获取各个网站的信息，主要用于淘宝的相关跳转
        LOAD_SITE_INFO = "http://m.api./zhenCC9.com/TaoCC/clientcontrol/android/1/client.json";

        // 淘宝手机充值
        TAOBAO_PHONE_RECHARGE = "http://out./zhenCC9.com/m/chongzhi";

        // 淘宝买彩票
        TAOBAO_CAIPIAO = "http://out./zhenCC9.com/m/caipiao";

        // 淘宝帐号手机绑定查询
        TAOBAO_BIND_PHONE_LNQUIRE = "http://passport.TuanDD.com/account/wap_merge/login";

        // 淘宝帐号合并
        TAOBAO_PHONE_MERGER = "http://sso./zhenCC9.com/m/jump_to?return_to=http://passport.TuanDD.com/account/wap_merge/bind_info";

        // 获取用户推荐IDs
        LOAD_USER_LIKE_IDS = BASE_API + "v1/cn/get_cid";

        // 手机充值创建订单接口
        RECHARGE_CREATE_ORDER_URL = BASE_BUY_API + "orders/create?";

        // 获取订单列表接口
        RECHARGE_ORDER_LIST_URL = BASE_BUY_API + "orders/get_order_list";

        // 手机充值获取支付信息接口
        RECHARGE_PAY_ORDER_URL = BASE_BUY_API + "orders/pay?";

        // 获取充值商品列表
        RECHARGE_LIST_URL = BASE_API + "recharge/";

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

        //首页品牌特卖
        BRAND_GROUP_TOPS_URL = BASE_API + "v6/brand/top";

        //每日十件列表
        TODAY_TENDEALS_URL = BASE_API + "v3/tendeals";

        // 商品保证URL
        DEAL_INDEMENDITY_URL = "http://s./zhenCC9.com/ms//zhenCC9hd/app/xb/xb.html";

        // 首页底部分类URL
        BOTTOM_CATEGORY_URL = BASE_API + /*"v3/homesetting"*/ "homesetting/v5";

        //获取用户优惠券信息URL
        GET_USER_COUPON_URL = "http://th5.m./zhenCC9.com/h5/api/coupons/usercoupons";



        // 检查是否是推广码
        CHECK_PROMO_CODE_URL = "http://campus.TuanDD.com/campus/promotion/validatePromoCode";

        // 校园专题列表页
        SCHOOL_DEAL_URL = BASE_API + "v3/deals/taocampus";

        //校园激活
        ACTIVATE_SCHOOL_URL = "http://campus.TuanDD.com/woss/campus?";

        //升级提醒
//        REMOTE_VERSION_URL = BASE_API + "api/checkconfig/v3";//老版本
        REMOTE_VERSION_URL = BASE_API + "config/update";//v4.0修改

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
        GET_USER_SHOP_CART = "http://th5.m./zhenCC9.com/h5/cart/list/my";


        BRAND_GROUP_One_URL = BASE_API + "v6/brand/getdealsbyid";
//        母婴品牌商品列表页
        MuYing_BRAND_GROUP_One_URL = BASE_API + "v1/muying/brand/deal/default";
        Bir_MuYing_BRAND_GROUP_One_URL = BASE_Z_API + "muying/brand/deal/custom/v2";


        //        母婴分类接口
        MuYing_CATEGORY_URL = BASE_API + "v1/muying/tags";
        Brand_Group_CATEGORY_URL = BASE_API + "brand/tab/v1";

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


        IM = "http://im./zhenCC9.com/com.TuanDD.im.userCenter";
        IM_IMAGE = "http://im./zhenCC9.com/com.TuanDD.im.fileManager";
        IM_Host = "im./zhenCC9.com";
        //为WEB提供离线消息数量查询接口，responsecode=_200表示成功,其他情况返回错误标示的code
        offlineMessageCount = IM + "/im/chatMessage/offlineMessageCount";

        //message获取某人聊天历史纪录列表
//    用户和商户之间聊天记录查询（包括接受和发送的消息），消息按照最近时间进行排序，
//    可按照指定时间段进行查询，可设置查询页数和页码尺寸，responsecode=_200表示成功,其他情况返回错误标示的code
        queryBussmessage = IM + "/im/chatMessage/queryBussmessage";
        queryGroupMessage = IM + "/im/chatMessage/queryGroupMessage";

        queryMessage = IM + "/im/chatMessage/queryMessage";
//    为WEB提供离线消息清空接口，responsecode=_200表示成功,其他情况返回错误标示的code,
// 返回数据格式：{"data":true,"errorinfo":"","responsecode":"_200"}
        clearOfflineMessage = IM + "/im/chatMessage/clearOfflineMessage";
        ImLoagin = IM + "/im/user/loginUser";
//    http://imtest1.imtest.com/com.TuanDD.im.userCenter/im/user/loginUser?userjid=suwg_3@imtest1.imtest.com&password=-890915866&resource=00000
//    http://imtest1.imtest.com/com.TuanDD.im.userCenter/im/user/loginUser?userjid=test2@imtest1.imtest.com&password=110251488&resource=00000
//    查询最近联系人信息，按照数组形式返回，包含聊天人jid和最后聊天时间responsecode=_200表示成功,其他情况返回错误标示的code
        recentContacts = IM + "/im/recentContacts/findRecentContacts";
//    IM系统创建普通用户
        userCreateAccount = IM + "/im/user/userCreateAccount";
        //    查询单个用户信息
        userInfo = IM + "/im/user/userInfo";
        //   图片上
        uploadImage = IM_IMAGE + "/im/upload/uploadImage";
        //获取客服信息
        IM_ACCOUNT_INFO = BASE_API + "im/imaccountinfo.json";
        //获取im所需pwd和jid接口
        IM_PWD_URL = IM + "/im/getuserbycookies";
//        获取IM 需要tooken  jid pass
        IM_JID_PASS_TOOKEN_URL = IM + "/im/thrift/loginUser";

        INSERTSELLERCOMMENTS=IM+"/im/statistics/insertSellerComments";
        IM_USER_INFO=IM+"/im/user/userInfo";
        OFFLINE_MESSAGE_COUNT=IM+"/im/chatMessage/v3/offlineMessageCount";

        IM_QUESTION_URL = "http://s./zhenCC9.com/ms//zhenCC9hd/app/im/question.html";

        insertUserComments = IM + "/im/statistics/insertUserComments";

        shiftserverelation=IM+"/im/shift/shiftserverelation";


//电信流量
        DIANXIN_VILADECODE=BASE_API+"ctcc/flow/sms";
        DIANXIN_GETLIULIANG=BASE_API+"ctcc/flow/order";


        HOME_PROMOTION = BASE_API + "homepromotion/v1";

        // 首页虚拟试衣间入口
        DRESSROOM_BANNER = BASE_API +  "dressroom/banner";

        // -- 送礼订单
        GET_GIVE_NEW_GIFTS = "http://th5.m./zhenCC9.com/h5/gift/getorderslist";

        QIN_NIU_TOKEN = BASE_API + "qiniu/uptoken";

        SIGN_IS_NEED_REVIVAL = "";// 签到复活状态获取地址

        INVITE_FRIEND="http://m./zhenCC9.com/h5/cn/hongbao/invite_page";//邀请好友

        ACTIVE_INFO = "http://api.TuanDD.com/mobilelog/activelog/v2/activeinfo";

        // 小米push上传regId
        DEVICE_INFO = BASE_API + "push/deviceinfo";

        SWITCH_SOMEONE=BASE_API+"config/switch";


        BRAND_TOPIC=BASE_API+"v6/brand/topic";

        //分类页主题馆获取
        CATEGORY_THEME_URL = BASE_API+"theme/hot";
        //分类页品牌墙获取
        CATEGORY_BRAND_URL = BASE_API+"v6/brand/wall";
        
        getBrandByID=BASE_API+"v6/brand";

        URL_HOT_SEARCHPAGE = BASE_API+"theme/recommend";



        //3.7.4
        UPLOAD_HEADVIEW = "https://passport./zhenCC9.com/m/account/avatar";
        UPDATE_NICKNAME = "https://passport./zhenCC9.com/m/account/user_name";
        JUDGE_LOGIN = "https://passport.TuanDD.com";
        NICKNAME_SETTING = "https://sso./zhenCC9.com/m/jump_to";
        JUDGE_LOGIN_HEAD = "https://sso./zhenCC9.com/m/generate_ticket";
        URL_CHECK_GIFT = BASE_API + "checkingift/screen";
        GET_USER_CART_COUNT_URL = "http://th5.m./zhenCC9.com/h5/api/cart/count";
        COLLEXT_LISTS_REACHED = "http://zapi./zhenCC9.com/cn/wl_favorites";

        USER_IDENTITY_GET = "http://m.api./zhenCC9.com/label";
        GET_TELCHARGE_EXCHANGESCORE="http://th5.m./zhenCC9.com/h5/api/gettelchargeexchangescore";
        GET_TELCHARGE_TEXT="http://m.api./zhenCC9.com/recharge/points/copywriting";
        GET_TELCHARGE_ORDER="http://buy.m./zhenCC9.com/orders/create_jf";
        GET_TELCHARGE_ISJOIN="http://buy.m./zhenCC9.com/orders/have_score_buy";
        GET_TELCHARGE_RULE="http://s./zhenCC9.com/ms//zhenCC9hd/app/client/duihuanrules.html";

        //首页一级分类顶部主题馆接口
        HOME_ONE_LEVEL_CLASSIFICATION_THEME=BASE_API+"theme/recommend/home";

        //V4.0.0
        HOME_HEAD = BASE_API+"homepromotion/v2";
        CATEGORY_ZUIHOUFENGQIANG = BASE_API + "deals/expire/v5";

        //包邮列表对应的分类数据
        CATEGORY_LIST_DATA_BAOYOU = BASE_API + "tags/baoyou/v6";
        //封顶列表对应的分类数据
        CATEGORY_LIST_DATA_FENGDING = BASE_API + "tags/fengding/v6";
        //今日上新列表对应的分类数据
        CATEGORY_LIST_DATA_SHANGXIN = BASE_API + "tags/today/v6";
        //今日上新列表数据接口
        CATEGORY_DEAL_LIST_SHANGXIN_URL = BASE_API + "deals/today/v4";
        //精选预告H5页面
        HOME_JINGXUAN_ADVANCE = "http://h5.m./zhenCC9.com/m/forecast/foreshow?pub_page_from=zheclient";
    }
}
