package com.hxuehh.reuse_Process_Imp.staticKey;

/**
 * Created by IntelliJ IDEA.
 * User: kait
 * Date: 12-5-19
 * Time: 下午4:46
 * To change this template use File | SettingsActivity | File Templates.
 */

//intent传递时候传输的东西，不再包括本地存储的东西
public class IntentBundleFlagStatic {

    public static final String APP_PUSH_FLAG = "app_push_flag";
    public static final String APP_RECOMMEND_FLAG = "app_recommend_flag";
    public static final String APP_SELL_FLAG = "app_sell_flag";

    public static final String APP_SHARE_SUCCESS_FLAG = "app_share_success_flag";
    public static final String APP_SHARE_FAILED_FLAG = "app_share_failed_flag";

    public static final String IMAGEURL = "imageurl";
    public static final String DESCRIPTION = "description";
    public static final String WEBVIEW_URL = "webview_url";
    public static final String WEBVIEW_TITLE = "webview_title";
    public static final String PUSH_TADAY_CATEGORY = "push_category";
    public static final String RECOM_UPDATE_TIME = "recom_update_time";
    public static final String CATEGORY_URL_NAME = "category_url_name";
    public static final String SOURCE_TYPE_ID = "start_info_id";
    public static final String IS_ORDER_TYPE = "is_order_type";
    public static final String BANNER_ID = "banner_id";

    public static final String DEAL = "deal";
    public static final String ADDRESS = "address";
    public static final String DEAL_FROM = "deal_from";
    public static final String SHOP = "shop";
    public static final String GIFT_ORDER = "gift_order";
    public static final String DEAL_ID = "dealid";
    public static final String WEIBO_TYPE = "weibo_type";
    public static final String INTEGRAL_DEAL_TYPE = "integral_type";
    public static final String ORDER_TYPE = "order_type";
    public static final String INVITE_CODE = "invite_code";
    public static final String INTEGRAL_FLAG = "integral_flag";
    public static final String USER_ACCOUNT_NUMBER = "user_account_number";
    public static final String IS_RECEIVE_ADDRESS_TABLE_FIRST_IN = "is_receive_address_table_first_in";

    public static final String POLL_PUSH_EVENT = "poll_push_event";

    public static final String WEIBO_CONTENT = "weibo_content";
    public static final String WEIBO_DEAL_URL = "weibo_deal_url";
    public static final String WEIBO_IMAGE_URL = "weibo_image_url";

    // 淘宝cookie黑名单24小时更新一次
    public static final String TAOBAO_COOKIE_TIME = "taobao_cookie_time";

    public static final String TADAY_12TIP = "taday12tip";

    public static final String NEED_SHOW_BRAND_TIP = "brand_show_tip";
    public static final String NEED_SHOW_BRAND_TIP_DAY = "brand_show_tip_day";
    public static final String TAB_RED_POINT_COUPON = "tab_red_point_coupon";
    public static final String TAB_RED_POINT_WISH = "tab_red_point_wish";

    public static final String MAIN_TAB_INDEX = "main_tab_index";
    public static final String CHILD_TAB_INDEX = "child_tab_index";

    public static final String INTEGRATION_NOTICE_SHARE = "integration_notice_share";
    public static final String INTEGRATION_NOTICE_SHARE_DEAL = "share_deal";
    public static final String INTEGRATION_NOTICE_SHARE_SHOP = "share_shop";
    public static final String INTEGRATION_NOTICE_SHARE_SOFT = "share_soft";
    public static final String INTEGRATION_NOTICE_SHARE_REGISTER = "share_register";
    public static final String INTEGRATION_NOTICE_SHARE_SIGN = "share_sign";
    public static final String INTEGRATION_NOTICE_SHARE_HUODONG = "share_huodong";

    // 校园推广码
    public static final String SCHOOL_CODE = "school_code";

    //各种第一次判断
    public static final String NEW_USER_CHECK = "new_user_check"; //新用户判断
    public static final String OLD_USER_FLAG = "-1"; //老用户标示
    public static final String UPDATE_USER_SIGN_MOVE_TIP = "update_user_sign_move_tip"; //老用户标示
    public static final String NEW_USER_INTEGRATION_TIP = "new_user_integration_tip"; //全新用户积分用处提示
    public static final String INTEGRATION_TIP_SHOWED = "1"; //　积分用处提示过
    public static final String SHOW_ZHI_TIP = "show_zhi_tip";
    // public static final String FIRST_FAVOR_TIP = "first_favor_tip";

    public static final String STRINGG_PROMOTION_TIME_TAB = "promotion_time_tab"; // 大促时间tab

    // 朝九晚八
    public static final String DATA_COUNT_REFRESH_TIME = "data_count_refresh_time"; // 隔天更新
    public static final String DATA_COUNT_DETAIL = "data_count_detail"; // 昨晚20点，今日9点，今日20点，明日9点个数(以,分割)


    // deal，shop收藏KEY
    public static final String DEAL_FAVOR_KEY = "DEAL_FAVOR_KEY";
    public static final String SHOP_FAVOR_KEY = "SHOP_FAVOR_KEY";

    // 首单返利标示
    public static final String FIRST_REBATE_FLAG = "first_rebate_flag"; // 首页首单返利页面只提示一次
    public static final String FIRST_REBATE_USER_CENTER_FLAG = "first_rebate_user_center_flag"; // 个人中心个人页面小红点只显示一次
    public static final String SHOW_REBATE_FLAG = "show_rebate_flag"; // 个人中心图标小红点只显示一次


    // 列表，大图模式状态
    public static final String MODE_STATUS = "mode_status";

    // 用户地址省份Id
    public static final String USER_PROVINCE = "user_province";

    // 加积分类别
    public static final String EXIT = "exit";
    public static final String HAS_GUIDE = "has_guide";
    public static final String HAS_GENDER = "has_gender";
    public static final String HAS_ADD_91_INTEGRAL = "has_add_91";
    public static final String IS_OLD_USER = "has_add_91";
    public static final String IS_UPGRADE = "is_upgrade";
    public static final String IS_UPGRADE_FOR_ACTIVE = "is_upgrade_for_active";

    public static final String FROM_SPLASH = "from_splash";
    public static final String FROM_SELL_TIP = "from_sell_tip";
    public static final String FROM_NOTIFY = "isFromNotify";

    public static final String ZHI_DE_MAI_TYPE = "_zhidemai";
    public static final String TAOBAO_RECHARGE_TYPE = "_chongzhi";
    public static final String TAOBAO_CAIPIAO_TYPE = "_caipiao";


    public static final String ZHI_CATEGORY_TAG = "zhi_category_tag";
    public static final String ZHI_CATEGORY_SORT_TAG = "zhi_category_srot_tag";
    public static final String ZHI_BANNER_TAG = "zhi_banner_tag";
    public static final String NO_UPDATE_NOTICE_TAG = "no_update_notice_tag";

    public static final String ZHI_CATEGORY_SORT_TYPE_TAG = "zhi_category_sort_type_tag";//suwg 值得逛 分类 type

    // 首页强制刷新时间
    public static final String REFRESH_HOME_TIME = "refresh_home_time";

    // 首页分类字符串
    public static final String GRID_CATEGORY_STRING = "grid_category_string";

    //曝光数据
    public static final String EXPOSE_DATA = "expose_data";

    // 用户等级
    public static final String USER_GRADE = "user_grade";


    public static final String ADDRESS_PROVANCE = "address_provance";
    public static final String ADDRESS_CITY = "address_city";
    public static final String ADDRESS_PROVANCE_CODE = "address_provance_code";
    public static final String ADDRESS_CITY_CODE = "address_city_code";


    //分辨率
    public static final String RESOLUTION = "resolution";


    // 注册成功
    public static final int REGISTER_SUCCESS = 1;
    // 登录成功
    public static final int LOGIN_SUCCESS = 2;
    // 激活成功
    public static final int ACTIVE_SUCCESS = 3;

    //分享分类
    public static final String SHARE_ENTRANCE_TYPE = "share_entrance_type";

    /*-------------------第三方登录，淘宝帐号合并--------------------------*/
    // 新浪微博登录类型
    public static final int TYPE_LOGIN_SINA = 0;
    //　QQ登录类型
    public static final int TYPE_LOGIN_QQ = 1;
    // 淘宝登录类型
    public static final int TYPE_LOGIN_TAOBAO = 4;

    // 淘宝帐号合并重新登录
    public static final int TAOBAO_BIND_LOGIN = 1;
    // 淘宝帐号合并登出
    public static final int TAOBAO_BIND_LOGOUT = 2;
    // 淘宝帐号合并注册
    public static final int TAOBAO_BIND_REGISTER = 3;
    // 淘宝帐号合并注册
    public static final int TAOBAO_BIND_SUCCESS = 4;
    // 淘宝帐号合并密码设置成功
    public static final int TAOBAO_BIND_PWD_SUCCESS = 5;
    // 淘宝帐号被合并过
    public static final int TAOBAO_BIND_MERGED_NUMBER = 6;


    // 淘宝帐号合并重新登录ReslutCode
    public static final int TAOBAO_BIND_LOGIN_RESLUT_CODE = 1001;
    // 淘宝帐号合并登出
    public static final int TAOBAO_BIND_LOGOUT_RESLUT_CODE = 1002;
    // 淘宝帐号合并注册
    public static final int TAOBAO_BIND_REGISTER_RESLUT_CODE = 1003;
    // 淘宝帐号绑定成功
    public static final int TAOBAO_BIND_SUCCESS_RESLUT_CODE = 1004;
    // 淘宝帐号绑定密码设置功能
    public static final int TAOBAO_BIND_PWD_RESLUT_CODE = 1005;

    // 帐号绑定RequestCode
    public static final int TAOBAO_BIND_REQUEST_CODE = 1007;

    //首页跳转购物车 resultCode
    public static final int TAOBAO_BIND_RESULT_CODE= 1008;

    public static final String TAOBAO_BIND_LOGIN_PHONE_NUMBER = "taobao_phone_number";


    // 打点列表分类
    public static final int TAB_TODAY = 0;            // 聚频道
    public static final int TAB_ZHI = 1;              // 逛频道
    public static final int TAB_BANNER = 2;           // 首页banner（专题页打点）
    public static final int TAB_BIG_PIC = 3;          // 开机大图(专题页打点)
    public static final int TAB_PUSH = 4;             // push
    public static final int TAB_PHONE_NEAR = 5;       // 手机周边
    public static final int TAB_BRAND_GROUP = 6;      // 品牌团
    public static final int TAB_TODAY_UPDATE = 7;     // 今日更新
    public static final int TAB_PROMOTION_SALE = 8;    // 大促页面
    public static final int TAB_USER_CENTER = 9;      // 个人中心
    public static final int TAB_BOUTIQUE_NOTICE = 10; // 精品预告
    public static final int TAB_SEARSH_RESULT = 11;   // 搜索结果页
    public static final int TAB_SPECIAL_TOPIC = 12;    // 专题页面
    public static final int TAB_JIFEN = 13;           // 积分商城
    public static final int TAB_TODAY_DEAL = 14;      // 每日十件
    public static final int TAB_JU_CATEGORY = 15;     // 聚分类banner
    public static final int TAB_GUANG_JX = 16;        // 逛精选banner
    public static final int TAB_GUANG_CATEGORY = 17;  // 逛分类banner
    public static final int TAB_DEAL_FAVOR = 18;      // 从收藏跳转详情
    public static final int TAB_SCHOOL_TOPIC = 19;    // 校园专区
    public static final int TAB_SCAN_RESULT = 20;     // 扫描跳转详情
    public static final int TAB_WISH = 21;     // 心愿单点击
    public static final int TAB_PROMOTE = 22;// 大促入口(勿改)
    //    public static final int TAB_PROMOTE = 22;// 大促入口(勿改)
    public static final int TAB_MUYING = 24;//母婴
    public static final int TAB_CATEGORY_OUT = 26;     // 分类
    public static final int TAB_PROMOTION = 25;// 大促打点


    //朝九晚八时间提示
    public static final int YESTERDAY_TWENTY_TIME = 1; //昨晚20点
    public static final int TODAY_NINE_TIME = 2; //今天9点
    public static final int TODAY_TWENTY_TIME = 3; //今天20点
    public static final int TOMORROW_NINE_TIME = 4; //明日9点

    /*-------------------第三方登录，淘宝帐号合并--------------------------*/


    public static final String FRAGMENT_ARGMENT = "fragment_argments";
    public static final String FRAGMENT_DETAIL_ARGMENT = "fragment_detail_argments";

    // 签到页面判断是否绑定手机号
    public static final int FROM_SIGN_TOACCOUNT_BIND = 1;

    //只带应用下载还是淘宝SDK
    public static final int APP_RECOMMEND = 0;
    public static final int APP_FROM_TAOBAO = 1;
    //应用下载页面到登录
    public static final int DOWNLOAD_TO_LOGIN = 0;

    //签到页面到登录
    public static final int NEW_TO_LOGIN = 0;

    //有效积分
    public static final int INTEGRATION_ALL = 0;
    public static final int INTEGRATION_INCOME = 1;
    public static final int INTEGRATION_EXPENSE = 2;
    public static final int INTEGRATION_EXPIRED = 3;

    // 我的礼品
    public static final int GIFT_EXCHANGE = 0;
    public static final int GIFT_EXCHANGE_BUY = 1;
    public static final int GIFT_LOTTERY = 2;
    public static final int GIFT_INTEGRATION_AUCTION = 3;

    public static final int SIGN_TO_LOGIN = 4;
    public static final int LOGIN_TO_REGISTER = 5;
    public static final int INTEGRAL_TO_LOGIN = 6;
    public static final int REQUEST_CODE_NEW_ADDRESS = 6;

    public static final int REQUEST_CODE_SHARE = 0x227;

    public static final int LEFT_FLAG = 0;
    public static final int RIGHT_FLAG = 1;


    public static final int GO_TO_MY_COUPON = 100;
    public static final int GO_TO_GIFT = 101;
    public static final int GO_TO_BACK_INTEGRAL_ORDER = 102;
    public static final int GO_TO_INVITE = 103;
    public static final int GO_TO_ADRESS = 104;
    public static final int GO_TO_MEMBER_WEB_DEAL = 105;
    public static final int GO_TO_BACK_SCORE_WEB_DEAL = 106;
    public static final int GO_TO_USER_INTEGRATION = 107;
    public static final int GO_TO_SELECT_IDENTITY = 108;
    public static final int GO_TO_USER_ORDER = 109;

    // 积分竞拍出价
    public static final int SUBMIT_AUCTION_INTEGRATION = 108;

    public static final int BIND_PHONE_FROM_AUCTION = 109;
    public static final int BIND_PHONE_FROM_EXCHANGE = 110;
    public static final int BIND_PHONE_FROM_LOTTERY = 111;

    // 首单返利
    public static final int SHOW_REBATE_TIP = 112;

    //身份选择
    public static final String GUIDE_TO_GENDER_FLAG = "guide_to_gender";
    public static final String ISSTUDENT = "isstudent";

    public static final String GENDER_KEY = "gender_key";//保存的key
    public static final String GENDER_KEY_OLD = "gender_key_old";//上一次保存的身份
    public static final String GENDER_ID_FLAG = "gender_flag";//传递的flag

    //push
    public static final String PUSH_MSG_KEY = "push_msg_key";

    public static final String SIGN_ALARM_SWITCH = "sign_alram_switch";

    public static final String DOWNLOAD_HOT_APP_CHECKED = "down_app_checked";

    public static final int SPLASH_TO_GENDER_REQUEST_CODE = 112;

    // 收藏请求码
    public static final int FAVORITE_REQUEST_CODE = 113;
    public static final int FAVORITE_RESULT_CODE = 114;

    public static final int GO_TO_MY_FAVOR = 115;

    //获取联系人信息
    public static final int GET_CONTACT_DATA_FROM_RECHARGE = 116;
    public static final int GET_CONTACT_DATA_FROM_HISTORY = 117;
    public static final int GO_TO_RECHARGE_HISTORY_CODE = 118;
    public static final int GO_TO_RECHARGE_RECODE_CODE = 119;

    public static final int CLASS_TO_RECHARGE_CODE = 120;

    public static final int TO_BIRTH_SELECT = 130;

    public static final int IDENTITY_TO_BIRTHDAY = 131;

    public static final int ADDRESS_LIST_TO_DETAIL = 132;
    public static final int ADDRESS_LIST_TO_ADD = 133;
    public static final int RESULT_DEFAULT = 134;
    public static final int RESULT_DELETE = 135;

    public static final int ALIPAY_REQUEST_CODE = 136;
    public static final int WEIXIN_REQUEST_CODE = 137;

    public static final int ORDER_LIST_2_DETAIL = 138;
    public static final int ORDER_CONFIRM_2_DETAIL = 139;

    public static final int UPDATE_ADDRESS = 140;
    public static final int GET_ADDRESS = 141;

    public static final int ORDERS_TO_DETAIL = 142;
    public static final int DETAIL_TO_BUY = 143;

    public static final int SIGN_WX_LOGIN = 144;//未登陆->手动跳转->关注
    public static final int SIGN_QQ_LOGIN = 145;//同上

    public static final int SIGN_WX_FOLLOW = 146;//个人中心->已登陆->手动跳转->关注
    public static final int SIGN_QQ_FOLLOW = 147;//同上

    public static final int SIGN_WX_SPECIAL_FOLLOW = 148;//登陆->已登陆->自动跳转->关注
    public static final int SIGN_QQ_SPECIAL_FOLLOW = 149;//同上

    public static final int ORDER_WAP_LOGIN = 150;

    public static final int GO_TO_USER_LOTTERY = 151; // 跳转到抽奖界面
    public static final int GO_TO_USER_WISH = 152; // 跳转到心愿单界面
    public static final int GO_TO_USER_CENTER_LOTTEYL = 153; // 个人中心跳转到抽奖界面
    public static final int GO_TO_USER_SHOP_CART = 154; // 个人中心跳转到我的购物车



    public static final int COMMON_RELOAD = 155; // 通用wap页面返回

    public static final int GO_TO_USER_TELECOM = 156; // 个人中心跳转电信反流量

    // im聊天界面
    public static final int GO_TO_IM_CHAT = 157;

    public static final int GO_TO_USER_NEW_GIFTS = 158; // 个人中心跳转送礼需求
    public static final int GO_TO_INVITE_FRIENDS = 159; // 个人中心送礼


    public static final int COMMON_WEB_VIEW_LOAD_PAGE = 160; // 活动页面跳转
    public static final int COMMON_WEB_VIEW_LOGIN = 161; // 活动页面跳转登录

    public static final int GO_TO_MessageCenter = 161; // 消息中心

    public static final int GO_TO_IM_LOGIN = 162; // 消息中心

    public static final int NEW_USER_RETURN_INTEGRAL_LOGIN = 163; // 首页50积分跳转登录
    public static final int BIND_PHONE_FROM_NEW_USER_RETURN_INTEGRAL = 164; // 首页50积分跳转绑定手机号
    public static final int PHONECHARGE_GOTO_LOGIN=165;
    public static final  int PHONECHARGE_GOTO_SIGN=166;
    public static final  int FAVORITE_GOTO_LOGIN=167;
    public static final int GO_TO_COUPON_LIST = 168; // 跳转优惠券使用专区
    public static final int GO_TO_MAIN_REFREHS_FAVOR = 169; // 跳转优惠券使用专区


    public static final String FOLLOW_WX = "follow_wx";
    public static final String FOLLOW_QQ = "follow_qq";

    public static final String PHONE_NUMBER_FLAG = "phone_number_flag";

    public static final String ADDRESS_ID = "address_id";

    //签到 今天是否签到对应的时间
    public static final String SIGN_TODAY_DATE = "sign_today_date";
    //签到连续签到几天的缓存
    public static final String SIGN_CONTINUE_CACHE = "sign_continue_cache";
    //签到历史 缓存
    public static final String SIGN_HISTORY_CACHE = "sign_history_cache";

    public static final String BABY_BIRTHDAY = "baby_birthday";
    public static final String BABY_OLD_BIRTHDAY = "baby_old_birthday";
    public static final String BABY_SEX = "baby_sex";
    public static final String BABY_OLD_SEX = "baby_old_sex";


    //订单信息
    public static final String ORDER_INFO = "order";
    public static final String ORDER_ID_TAG = "order_id";
    public static final String FROM_ORDER_LIST = "from_order_list";

    public static final String LAST_TIME_KEY = "l_t_k";
    public static final String FILTER_KEY = "f_k";
    public static final String OUT_COUNT_KEY = "o_c_k";

    //值得逛
    public static final String LAST_TIME_KEY_ZHI = "l_t_k_z";
    public static final String FILTER_KEY_ZHI = "f_k_z";

    public static final String WIDGET_STREAM = "widget_stream";
    public static final String WIDGET_EVENT = "widget_event";

    // 砸蛋当前out数
    public static final String SHARED_PREFERENCE_KEY_EGG_CURRENT_OUT = "shared_preference_key_egg_current_out";
    public static final String KEYWORDS = "keyword";


    public static String obj = "obj";//传递的参数

    // 当前是否有心愿单达成
    public static String HAS_WISH_REACHED = "has_wish_reached";

    // 首页分类ｔａｂ选中状态发生变化
    public static String MAIN_TAB_CATEGORY_CHECK_CHANGED = "main_tab_category_check_changed";

    // 首页tab选中了分类，标记为true为了打点用，是从底部tab进入的分类商品列表out出去的cType参数为26
    public static String MAIN_TAB_CATEGORY_CHECKED = "main_tab_category_checked";
    //首页大促分类点击更多选中分类tab
    public static String MAIN_TAB_CATEGORY_CHECKED_BY_MORE_CLICK = "main_tab_category_checked_by_more_click";

    // 发生过崩溃事件
    public static String HAS_CRASHED = "has_crashed";

    // 大促数据缓存
    public static String PROMOTION_CACHE = "promotion_cache";


    //曝光，页面位置字段标记
    public static final String EXPOSE_FLAG_REFER = "expose_refer_flag";
    public static final String EXPOSE_FLAG_POSTYPE = "expose_type_flag";
    public static final String EXPOSE_FLAG_POSVALUE = "expose_value_flag";
    public static final String EXPOSE_PAGE_FLAG = "expose_page_info_flag";

    // 商品数量,首页使用
    public static String GOODS_SUM = "goods_sum";

    //sp中保存分类页主题馆信息的key
    public static String THEME_IN_CATEGORY = "theme_in_category_key";
    //sp中保存分类页品牌团信息的key
    public static String BRAND_IN_CATEGORY = "brand_in_category_key";

    //是否显示过新手50积分的Dialog
    public static String HAS_SHOW_NEW_USER_INTEGRAL = "has_show_new_user_integral";
    //新手50积分的Bitmap
    public static String NEW_USER_INTEGRAL_BITMAP = "new_user_integral_bitmap";

    //上次获取开机大图信息的身份相关信息
    public static String LAST_STARTINFO_IDENTITY = "last_startinfo_identity";

    //V4.0.0 缓存数据
    public static String HOME_HEAD_CACHE = "home_head_cache";
    //手机周边照片缓存
    public static String MOBILE_AROUND_PIC = "mobile_around_pic";

    //sp中保存首页一级分类主题馆信息的key
    public static String THEME_IN_HOME_ONE_LEVEL_CATEGORY = "theme_in_home_one_level_category_key";
    //sp中保存首页一级分类品牌团信息的key
    public static String BRAND_IN_HOME_ONE_LEVEL_CATEGORY = "brand_in_home_one_level_category_key";

    // 商品数量,首页使用
    public static String GOODS_SUM_NEW = "goods_sum_new";

    //朝九晚八提示
    public static String ZHAO9_WAN8 = "zhao9_wan8";

    //首页中Banner数据缓存
    public static String BANNER_CACHE = "banner_cache";
    //首页中Brand数据缓存
    public static String BRAND_CACHE = "brand_cache";

    public static String WIDGET_IS_ON_DESK = "widget_is_on_desk";

    public static String IS_FROM_EXIT = "is_from_exit";

    public static String ALARM = "alarm";

    /*
    V4.0版本首页中数据对比缓存策略需要使用，看是否需要替换现有的图片
     */
    public static String HOME_TOP_TAB_BG_COLOR = "home_top_tab_bg_color";
    public static String PUSHTYPE = "pushtype";

    //点击banner进入专题活动,存入bannerid
    public static String BANNER_TOPIC_ID = "banner_topic_id_tag";

    public static int AVOID_IAMGE_LOAD_ANIMATION = 0;



}
