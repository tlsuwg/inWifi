package com.hxuehh.reuse_Process_Imp.staicUtil.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Paint;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.analytics.Analytics;
import com.hxuehh.reuse_Process_Imp.analytics.IAnalyticsInfo;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.image.image13.Image13lLoader;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.DB.DatabaseManager;
import com.hxuehh.reuse_Process_Imp.staticKey.AnalyticsStatic;
import com.hxuehh.reuse_Process_Imp.staticKey.IntentBundleFlagStatic;

import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.FROYO;

/**
 * Created by IntelliJ IDEA.
 * User: kait
 * Date: 12-5-19
 * Time: 下午2:24
 * To change this template use File | SettingsActivity | File Templates.
 */
public class Tao800Util {

    public static boolean isNull(String str) {
        return TextUtils.isEmpty(str) || "null".equals(str);
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isEmpty(Object... objects) {
        return (objects == null || objects.length <= 0);
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void initAnalytics() {
        IAnalyticsInfo analytics = Analytics.getAnalyticsInfo();
        if (analytics == null) {
            Analytics.setAnalyticsInfo(new AnalyticsStatic(SuApplication.getInstance()));
        }
    }



    public static void setPaintFlags(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static String getDiscount(float cur, float pre) {
        DecimalFormat format = new DecimalFormat("0.0");
        if (cur == 0 || pre == 0) {
            return "";
        }

        String zhe = format.format(10 * cur / pre);

        if (zhe != null && zhe.endsWith(".0")) zhe = zhe.substring(0, zhe.length() - 2);

        return zhe;
    }

    public static String getPrice(float price) {
        float mPrice = price / 100;
        if (mPrice == (int) mPrice) {
            return String.valueOf((int) mPrice);
        } else {
            return String.valueOf(mPrice);
        }
    }

    public static int generaRandom(int size) {
        return (int) (Math.random() * size);
    }


    public static String getStudentCode() {
        return SharedPreferencesUtils.getBoolean(IntentBundleFlagStatic.ISSTUDENT) ? "1" : "0";
    }



    public static boolean isOldUesr() {
        String userCheckTime = SharedPreferencesUtils.getString(IntentBundleFlagStatic.NEW_USER_CHECK);

        if (!TextUtils.isEmpty(userCheckTime)) {
            if (IntentBundleFlagStatic.OLD_USER_FLAG.equals(userCheckTime)) {
                return true;
            }
        }
        return false;
    }

    // 获取包里带的推广码
    public static String getSpreadCode() {
        String spread = "";
        try {
            InputStream is = SuApplication.getInstance().getAssets().open("control.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            if (!TextUtils.isEmpty(json)) {
                JSONObject object = new JSONObject(json);
                if (object.has("spread_code")) {
                    spread = object.optString("spread_code");
                }
            }
        } catch (FileNotFoundException fx) {
            LogUtil.d("access file txt is not exist");
        } catch (Exception ex) {
            LogUtil.d("access file txt get wrong");
            ex.printStackTrace();
        }

        return spread;
    }


    //coform qq
    public static boolean isQQ(String qq) {
        Pattern p = Pattern.compile("[1-9][0-9]{4,14}");
        Matcher m = p.matcher(qq);
        return m.matches();
    }

    //coform modilephone
    public static boolean isMobilePhone(String tel) {
        if (tel.contains(" ")) {
            tel = tel.replaceAll(" ", "");
        }

        if (tel.contains("-")) {
            tel = tel.replaceAll("-", "");
        }
        Pattern p = Pattern.compile("(1)\\d{10}$");
        return p.matcher(tel).matches();
    }

    //coform postcode
    public static boolean isPostCode(String postcode) {
        Pattern p = Pattern.compile("\\p{Digit}{6}");
        boolean isExist = false;
        if (p.matcher(postcode).matches()) isExist = true;
        return isExist;
    }

    //coform email
    public static boolean isEmail(String str) {
        Pattern p1 = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p1.matcher(str);
        return m.matches();
    }

    public static void cleanCache() {
        try {
            // SELECT name FROM sqlite_master WHERE type='table' AND name='table_name';
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase(AppStaticSetting.DEFAULT_DATABASE).getDb();
            String sql = "DELETE FROM dpc";
            db.execSQL(sql);

            sql = "DELETE FROM image";
            db.execSQL(sql);
        } catch (SQLiteException e) {
            LogUtil.w(e);
        }
    }

    public static void removeCookie() {
        CookieSyncManager.createInstance(SuApplication.getInstance());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    //检查用户省份是不是可以邮寄
    public static boolean isRightAddress(String province) {
        if (TextUtils.isEmpty(province)) {
            return false;
        }
        if (!province.contains("香港") && !province.contains("澳门") && !province.contains("台湾") && !province.contains("新疆")
                && !province.contains("甘肃") && !province.contains("青海") && !province.contains("西藏") && !province.contains("内蒙")) {
            return true;
        }

        return false;
    }

    @Deprecated // need use GET_TASKS permission
    public static boolean isRuning(Activity context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfos = am.getRunningTasks(50);
        for (RunningTaskInfo taskInfo : taskInfos) {
            if (taskInfo.topActivity.getPackageName().equals(context.getPackageName()) && taskInfo.numActivities != 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Count string 's length.
     *
     * @param str
     * @return
     */
    public static int getWordCount(CharSequence str) {
        int length = 0;
        if (str != null) {
            int size = str.length();
            int num = 0;
            for (int i = 0; i < size; i++) {
                final int ascii = Character.codePointAt(str, i);
                if (ascii >= 255) {
                    length++;
                } else if (ascii >= 0 && ascii < 255) {
                    num++;
                    if (num != 2) {
                        length++;
                    } else {
                        num = 0;
                    }
                }
            }
        }

        return length;
    }

    public static String formatFileSize(long length) {
        String result = null;
        int sub_string;
        if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + "M";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + "K";
        } else if (length < 1024)
            result = Long.toString(length) + "B";

        return result;
    }

    // 二维码扫描dealID取得
    public static String getDealId(String dealUrl) {
        String baseUrl = "dealId=";
        String dealId = "";
        if (dealUrl.contains(baseUrl)) {
            dealId = dealUrl.split(baseUrl)[1].trim();
        }
        return dealId;
    }





    public static String getChannel() {
        StringBuilder channel = new StringBuilder();
        channel.append(AppConfig.getInstance().CLIENT_TAG).append("|")
                .append(DeviceUtil.getimei()).append("|")
                .append("Android").append("|")
                .append(SuApplication.getInstance().getVersionName()).append("|")
                .append(AppConfig.PARTNER_ID);
        return channel.toString();
    }








    /**
     * 4.0.0 新增out参数
     *
     * @param url
     * @param pos_type
     * @param pos_value
     * @param position
     * @param dealId
     * @return
     */
    public static String addActivityValueInOutUrl(String url, String pos_type, String pos_value, int position, String dealId) {
        if (TextUtils.isEmpty(url)) return url;

        StringBuilder standardUrl = new StringBuilder(url);
        if (url.indexOf("?") > -1) {
            if (!url.endsWith("?")) {
                standardUrl.append("&");
            }
        } else {
            standardUrl.append("?");
        }
        standardUrl.append("pos_type").append("=").append(pos_type);
        standardUrl.append("&pos_value").append("=").append(pos_value);
        standardUrl.append("&model_name").append("=").append("deallist");
        standardUrl.append("&model_item_index").append("=").append(position + 1);
        standardUrl.append("&model_id").append("=").append(dealId);
        standardUrl.append("&model_index").append("=").append("");
        return standardUrl.toString();
    }






    public static String shareConcatMid(String shareUrl) {
        if (TextUtils.isEmpty(shareUrl)) return "";

        /*if (shareUrl.contains("/deal/")) {
            String[] strings = shareUrl.split("/deal/");
            shareUrl = strings[0] + "/i" + "/deal/" + strings[1];
        }*/

        StringBuilder sb = new StringBuilder(shareUrl);
        if (shareUrl.contains("?")) {
            if (shareUrl.trim().indexOf("?") != shareUrl.trim().length() - 1) {
                sb.append("&");
            }
        } else {
            sb.append("?");
        }

        return sb.append("mId=").append(SharedPreferencesUtils.getString(IntentBundleFlagStatic.INVITE_CODE)).toString();
    }

    private static String generaTTID() {
        return "400000_21428298@zbbwx_Android_" + SuApplication.getInstance().getVersionName();
    }




    public static PackageInfo getTuan800Info() {
        PackageInfo tuan800Info = null;
        try {
            tuan800Info = SuApplication.getInstance().getPackageManager().getPackageInfo(
                    "com.tuan800.android", 0);
        } catch (Exception e) {
            LogUtil.w(e);
        }

        if (tuan800Info != null) {
            LogUtil.d("--------tuan800--------" + tuan800Info.versionCode);
        }

        return tuan800Info;
    }

    public static PackageInfo getTaoBaoInfo() {
        PackageInfo taoBaoInfo = null;
        try {
            taoBaoInfo = SuApplication.getInstance().getPackageManager().getPackageInfo(
                    "com.taobao.taobao", 0);
        } catch (Exception e) {
            LogUtil.w(e);
        }

        if (taoBaoInfo != null) {
            LogUtil.d("--------taobao--------" + taoBaoInfo.versionCode);
        }

        return taoBaoInfo;
    }


    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+"); // 多个空格转译 \\s+
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
            LogUtil.w(e);
        }
        return initial_memory;
    }

    public static boolean isThresholdMemory() {
        long start = System.currentTimeMillis();
        long totalMemory = getTotalMemory();
        if (totalMemory == 0) return false;
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) SuApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(outInfo);
        long nativeThreshold = (long) ((outInfo.threshold / (totalMemory * 1.0) + 0.01) * totalMemory); // 内存临界值加大1%
        if (outInfo.availMem <= nativeThreshold) {
            Image13lLoader.getInstance().flushCache();
            //releaseMemory();
            return true;
        }
        return false;
    }

    public static void releaseMemory() {
        Image13lLoader.getInstance().flushCache();
        ActivityManager activityManager = (ActivityManager) SuApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo apinfo = null;
        String[] pkgList;
        if (!isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                apinfo = list.get(i);
                pkgList = apinfo.pkgList;
                if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    for (int j = 0; j < pkgList.length; j++) {
                        if (SDK_INT < FROYO) {
                            activityManager.restartPackage(pkgList[j]);
                        }

                        activityManager.killBackgroundProcesses(pkgList[j]);
                    }
                }
            }
        }
    }



    //启动其他应用
    public static boolean openApp(Activity context, String packageName) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        if (apps != null) {
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName1, className);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cn);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }




    //显示软键盘
    public static void showSoftInputMethod(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    //隐藏软键盘
    public static void hideSoftInputMethod(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static String getFormatPhone(String phone, String regular) {
        String tempPhone = "";
        char[] chars = phone.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i == 3 || i == 7) {
                tempPhone += regular;
            }
            tempPhone += c;
        }

        return tempPhone;
    }

    /**
     * 将元单位转换成分单位
     *
     * @return 分
     */
    public static Integer change2int(String price) {
        BigDecimal decimal = new BigDecimal(price);
        BigDecimal decimal1 = new BigDecimal(100);
        return decimal.multiply(decimal1).intValue();
    }

    /**
     * 将元单位转换成分单位
     *
     * @return 分
     */
    public static String change2String(String price) {
        BigDecimal decimal = new BigDecimal(price);
        BigDecimal decimal1 = new BigDecimal(100);
        return decimal.multiply(decimal1).toString();
    }

    /**
     * 将分单位转换元单位
     *
     * @param cent
     * @return
     */
    public static String change2String(int cent) {
        BigDecimal decimal = new BigDecimal(cent);
        BigDecimal decimal1 = new BigDecimal(100);
        return decimal.divide(decimal1).toString();
    }

    //过滤字符串，比如过滤“.0”
    public static String filterStr(String src, String str) {
        if (StringUtil.isEmpty(src)) return "";

        int index = src.lastIndexOf(str);
        if (index > 0) {
            if (src.substring(index, src.length()).equals(str)) {
                return src.substring(0, index);
            }
        }

        return src;
    }

    //截取字符串
    public static String filterTopStr(String src, String str) {

        if (isEmpty(str)) {
            return "";
        }

        int index = src.indexOf(str);
        if (index > 0) {

            return src.substring(0, index);
        }

        return src;
    }



    public static void sendShareSuccessBroadcast() {
        Intent intent = new Intent(IntentBundleFlagStatic.APP_SHARE_SUCCESS_FLAG);
        SuApplication.getInstance().sendBroadcast(intent);
    }

    public static void setWeiXinLoginFastBroadcast() {
        Intent intent = new Intent("com.weixin.loginfast");
        SuApplication.getInstance().sendBroadcast(intent);

    }

    public static String getTopActivity(Activity context)

    {

        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)

            return (runningTaskInfos.get(0).topActivity).toString();

        else

            return null;

    }

    public static void sendShareFailedBroadcast() {
        Intent intent = new Intent(IntentBundleFlagStatic.APP_SHARE_FAILED_FLAG);
        SuApplication.getInstance().sendBroadcast(intent);
    }

    public static int getSourceFromAnalytic(String analytic) {
        if (AnalyticsStatic.EVENT_BANNER_CLICK.equals(analytic)) {//首页banner
            return IntentBundleFlagStatic.TAB_BANNER;
        } else if (AnalyticsStatic.EVENT_BANNER_CATEGORY.equals(analytic)) {//分类banner
            return IntentBundleFlagStatic.TAB_JU_CATEGORY;
        } else if (AnalyticsStatic.EVENT_BANNER_GFL.equals(analytic)) {//逛分类
            return IntentBundleFlagStatic.TAB_GUANG_CATEGORY;
        }
        return IntentBundleFlagStatic.TAB_BANNER;
    }

    public static String getSalesCount(int number) {
        if (0 == AppStaticSetting.SALES_COUNT_SHOW_WAN) {
            return number + "";
        }
        if (number < 10000) {
            return number + "";
        }

        int numOfHundred = number % 1000 / 100;
        int numOfThousand = number % 10000 / 1000;
        int numBeforeThousand = number / 10000;

        if (numOfHundred >= 5) {
            numOfThousand += 1;
            if (numOfThousand == 10) {
                numBeforeThousand += 1;
            }
        }

        if (numOfThousand == 0 || numOfThousand == 10) {
            return numBeforeThousand + "万";
        } else {
            return numBeforeThousand + "." + numOfThousand + "万";
        }
    }


    public static int getRandomNum(int maxNum) {
        return (int) (Math.random() * maxNum);
    }


    public static String getNewType() {
        StringBuilder type = new StringBuilder();
        try {
            ConnectivityManager cm = (ConnectivityManager) SuApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            if ("wifi".equalsIgnoreCase(typeName)) {
                type.append(typeName);
            } else {
                type.append(typeName);
                type.append("-");
                type.append(info.getSubtypeName());
                type.append("-");
                type.append(info.getExtraInfo());
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }

        return type.toString();
    }


    public static boolean isNeedSaveUrlAfterLoadFinish(String url) {
        if (url.startsWith("zhe800")) {//自己的Scheme跳转
            return false;
        }

        //mqq://im/chat?chat_type=wpa&uin=3103049266&version=1&src_type=web&web_src=h5.m.zhe800.com cmp=com.tencent.mobileqq/.activity.JumpActivity
        if (url.startsWith("mqq")) {//商城点击QQ客户跳转到QQ聊天
            return false;
        }
        return true;
    }







    /**
     * 设置商品去向的view的内容  优先级：品牌和主题显示对应的，其余的显示淘宝天猫或者商城
     *
     * @param tvStore   显示内容的view
     * @param flag      商品的归属
     * @param deal_type
     * @param shop_type
     */
    public static void setDealStore(TextView tvStore, int flag, String deal_type, int shop_type) {
        tvStore.setVisibility(View.VISIBLE);
        if (flag == 4) {
            tvStore.setText("主题馆");
        } else if (flag == 5) {
            tvStore.setText("品牌特卖");
        } else {
            if ("1".equals(deal_type)) {
                tvStore.setText("特卖商城");
            } else if (shop_type == 1) {
                tvStore.setText("去天猫");
            } else if (shop_type == 0) {
                tvStore.setText("去淘宝");
            } else {
                tvStore.setVisibility(View.GONE);
            }
        }
    }




    /**
     * 摄像头是否可用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            // TODO camera驱动挂掉,处理??
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }

        return canUse;
    }


    /**
     * 逆向解析URL，获取参数值
     *
     * @param URL
     * @return
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    //生成9位随机码//好像不能保证唯一性
    public static String generateRandomString(int len) {
        String all = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < len; i++) {
            s.append(all.charAt(getRandomNum(36)));
        }

        return s.toString();
    }

    //检查应用是否有此权限
    public static boolean checkPermission(Context context, String permission) {
        return (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(permission));
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @return true 表示开启
     */
    public static boolean isGpsOpen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps || network;
    }


    public static boolean isGpsEnable(final Context context) {

        return isGpsOpen(context) &&
                //下面部分有些手机不是很管用
                (Tao800Util.checkPermission(context, "android.permission.ACCESS_COARSE_LOCATION")
                        || Tao800Util.checkPermission(context, "android.permission.ACCESS_FINE_LOCATION"));

    }

    public static String encodeUrl(String key) {
        return URLEncoder.encode(key);
    }
}
