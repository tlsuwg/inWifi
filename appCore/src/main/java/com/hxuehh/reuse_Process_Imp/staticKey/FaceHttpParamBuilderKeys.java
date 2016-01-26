package com.hxuehh.reuse_Process_Imp.staticKey;

import com.hxuehh.appCore.faceFramework.faceProcess.net.FaceBaseHttpParamBuilder;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.SessionCookie;

import java.util.HashMap;
import java.util.Map;

//http加载参数

public class FaceHttpParamBuilderKeys extends FaceBaseHttpParamBuilder{

    public static final String ID = "id";

    public static final String COUNT = "count";

    public static final String URL_NAME = "url_name";

    public static final String CHANNEL = "channelid";

    public static final String PLATFORM = "platform";

    public static final String PERPAGE_COUNT = "per_page_count";

    public static final String USER_TYPE = "user_type";

    public static final String USER_ROLE = "user_role";

    public static final String CATEGORY_TAG_URL = "tag_url";//商品所属分类

    public static final String HOT_BANNER_TYPE = "pagetype";

    public static final String USER_ID = "user_id";

    public static final String ACCESS_TOKEN = "access_token";

    public static final int PAGE_SIZE_WIFI = 20;
    public static final String PAGE = "page";
    public static final String COUNTS = "counts";
    public static final String TAG = "tag";


    public static final String DATE = "date";

    public static final String LIMIT = "limit";

    public static final String OFFSET = "offset";

    public static final String PAGE_SET = "page_set";

    public static final String PER_PAGE = "per_page";

    public static final String IM_Per_PAGE = "maxRow";
    public static final String  IM_PAGE= "nowPage";

    public static final String IMAGE_TYPE = "image_type";

    public static final String DATE_TOMORROW = "tomorrow";

    public static final String IMAGE_ALL = "all";

    public static final String IMAGE_WEBP = "webp";

    public static final String C_ID = "cids";

    public static final String CITY_ID = "cityid";

    public static final String CITY_NAME = "cityname";

    public static final String LAT_HISTORY = "lat_history";

    public static final String LNG_HISTORY = "lng_history";

    public static final String FORMAT = "format";

    public static final String JSON = "json";

    public static final String TAO_TAG_ID = "tao_tag_id";

    public static final String MAC = "mac";

    public static final String OOS = "oos";

    public static final String GRADE = "grade";

    public static final String INTEGRATION_PARAM = "return_to";

    public static final String BEGIN_TIME = "begin_time";
    public static final String SHOW_SALEOUT = "show_saleout";
    public static final String BOTTOM_SALEOUT = "bottom_saleout";

    public static final String IMAGE_MODEL = "image_model";

    public static final String BRAND_ID = "brand_id";

    //category
    public static final String CATEGORY_ORDER = "order";//排序规则

    // date
    public static final long MINUTE = 1000 * 60;
    public static final long HALF_HOUR = 30 * MINUTE;
    public static final long HOUR = 2 * HALF_HOUR;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;

    //收货地址
    public static final String PARA_CONSIGNEE_NAME = "receiver_name";
    public static final String PARA_PROVINCE_ID = "province_id";
    public static final String PARA_CITY_ID = "city_id";
    public static final String PARA_AREA_ID = "county_id";
    public static final String PARA_PROVINCE_NAME = "province_name";
    public static final String PARA_CITY_NAME = "city_name";
    public static final String PARA_AREA_NAME = "county_name";
    public static final String PARA_ADDRESS_INFO = "address";
    public static final String PARA_PHONE_NUMBER = "mobile";
    public static final String PARA_POSTCODE = "post_code";

    //登录domain
    public static  String DOMAIN = "zhe800.com";
    public static final String IDS = "ids";
    public static int PAGE_EVERDAT =10;
    public static String SearchKey="search_key";


    public static String IM_userjid="userjid";
    public static String IM_token="token";
    public static String IM_idType="idType";

//    http://192.168.90.62:8080/com.tuan800.im.userCenter/im/user/loginUser?userjid=suwg@im_test&password=3542194



    SessionCookie cookie;
    public void setCookie(SessionCookie s) {
        this.cookie=s;
    }
    public SessionCookie getCookie() {
        return cookie;
    }



    boolean isSetZhe800Filter;

    public void setZhe800Filter() {
        isSetZhe800Filter=true;
        setZhe800FilterParm();
    }

    public boolean isSetZhe800Filter() {
        return isSetZhe800Filter;
    }

    public void setZhe800FilterParm() {
        if (headers == null)
            headers = new HashMap<String, String>();
//        headers.put("X-Zhe800out", Zhe800Filter.getOutCount());
//        headers.put("X-Zhe800filter", Zhe800Filter.getFilterStr());
//        headers.put("X-Zhe800userid", Session2.isLogin() ? Session2.getLoginUser().getId() : "");
    }

    Map<String, String> headers;
    public Map<String, String> getHead() {
        return headers;
    }


    boolean setUserInfo;
    public void setUserInfoBoolean() {
        setUserInfo=true;
        setUserInfo();
    }

    public boolean isSetUserInfo() {
        return setUserInfo;
    }

    public void setUserInfo(){
//        put(ParamBuilder.USER_TYPE, Tao800Util.isOldUesr() ? 1 : 0);
//        put(ParamBuilder.USER_ROLE, Tao800Util.getUserRole());
//        put(ParamBuilder.STUDENT, (PreferencesUtils.getBoolean(IntentBundleFlag.ISSTUDENT) ? 1 : 0));
    }

}

