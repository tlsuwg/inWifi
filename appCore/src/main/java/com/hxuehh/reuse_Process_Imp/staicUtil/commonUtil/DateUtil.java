package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.hxuehh.com.R;
import android.os.Build;
import android.text.TextUtils;

import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.utils.Tao800Util;

import com.hxuehh.appCore.app.SuApplication;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: kait
 * Date: 4/2/13
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static SimpleDateFormat simpleDayDateFormat = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat simpleMonthDayFormat = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat simpleMDHSFormat = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat simpleHSFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat simpleYHMDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static int getDayCountOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static String getYearConcatDay() {
        Calendar calendar = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("000");
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        return String.valueOf(calendar.get(Calendar.YEAR))
                + df.format(day);
    }

    //1970.1.1 到现在的天数
    public static int getDayCount(){
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, 0, 0, 0);
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        long intervalMilli = now.getTimeInMillis() - cal.getTimeInMillis();
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));
    }

    public static String getYearConcatMonthDay() {
        Calendar calendar = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(calendar.get(Calendar.YEAR)) + df.format(month) + df.format(day);
    }



    public static boolean afterNow(String source) {
        Date now = new Date();
        Date sourceDate = null;
        try {
            sourceDate = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            LogUtil.w(e);
        }

        if (sourceDate != null && sourceDate.getTime() < now.getTime()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean beforeNow(String source) {
        try {
            Date date = simpleDateFormat.parse(source);
            return date.before(new Date());
        } catch (ParseException e) {
            LogUtil.w(e);
        }
        return false;
    }

    public static boolean afterOneDay(int dayTime) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);

        if (day != dayTime) {
            return true;
        } else {
            return false;
        }
    }

    public static int getPromotionHour(String source) {
//        Calendar calendar = Calendar.getInstance();
//        try {
//            calendar.setTime(simpleDateFormat.parseAll(source));
//            return calendar.get(Calendar.HOUR_OF_DAY);
//        } catch (Exception e) {
//            LogUtil.w(e);
//        }
//
//        return calendar.get(Calendar.HOUR_OF_DAY);
        try {
            return Integer.valueOf(source.substring(11, 13));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean dateCompare(String newTime, String oldTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        try {
            Date date = simpleDateFormat.parse(newTime);
            return date.getTime() > Long.parseLong(oldTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 判断是否是当天
    public static boolean isCurrentDay(String timeSource) {
        try {
            if (!TextUtils.isEmpty(timeSource) && !"-1".equals(timeSource)) {
                return simpleDayDateFormat.format(new Date().getTime()).equals(simpleDayDateFormat.format(new Date(Long.parseLong(timeSource))));
            }
        } catch (Exception e) {
            LogUtil.w(e);
            return false;
        }

        return false;
    }

    // 判断是否是同一天
    public static boolean isSameDay(String timeSource, String timeSource2) {
        try {
            if (!TextUtils.isEmpty(timeSource) && !"-1".equals(timeSource) && !TextUtils.isEmpty(timeSource2) && !"-1".equals(timeSource2)) {
                return simpleDayDateFormat.format(simpleDateFormat.parse(timeSource)).equals(simpleDayDateFormat.format(simpleDateFormat.parse(timeSource2)));
            }
        } catch (Exception e) {
            LogUtil.w(e);
            return false;
        }

        return false;
    }


    // 返回秒
    public static long getCommonTime(String source) {
        Long dis = 0L;
        Date souceDate;
        try {
            souceDate = simpleDateFormat.parse(source);
            dis = souceDate.getTime() / 1000;
        } catch (ParseException e) {
            // nothing
        }
        return dis;
    }

    // 返回秒
    public static long getCommonTime2(String source) {
        Long dis = 0L;
        Date souceDate;
        try {
            souceDate = simpleDateFormat.parse(source);
            dis = souceDate.getTime() / 1000;
        } catch (ParseException e) {
            // nothing
        }

        return dis;
    }

    public static boolean isNowDay(String longTime) {
        if (StringUtil.isNull(longTime)) {
            return false;
        }

        long timeSource = Long.valueOf(longTime);
        return isNowDay(timeSource);
    }

    public static boolean isNowDay(long longTime) {
        if (longTime <= 0) return false;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longTime);

        return calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    public static long getTimeMillis(String source) {
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getTimeMillis(String pattern, String source) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = format.parse(source);
        } catch (ParseException e) {

        }
        return date.getTime();
    }

    public static String getTime(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static long getExpeiresTime(String source) {
        Long dis = 0L;
        Date now = new Date();
        Date souceDate;
        try {
            souceDate = simpleDateFormat.parse(source);
            dis = souceDate.getTime() / 1000;
        } catch (ParseException e) {
            // nothing
        }

        return dis;
    }

    //与当前时间比较差
    public static long getDValueTime(String source) {
        Long dis = 0L;
        Date now = new Date();
        Date souceDate;
        try {
            souceDate = simpleDateFormat.parse(source);
            dis = souceDate.getTime() - now.getTime();
        } catch (ParseException e) {
            LogUtil.w(e);
        }

        return dis;
    }

    //和服务器时间的时间比较差
    public static long getDValueTimeBetTwoTime(String sourceTime, long sourceServer) {
        Long dis = 0L;
        Date sourceDateTime;
        try {
            sourceDateTime = simpleDateFormat.parse(sourceTime);
            dis = sourceDateTime.getTime() - sourceServer;
        } catch (ParseException e) {
            LogUtil.w(e);
        }

        return dis;
    }

    public static String getMDHSTime(String source) {
        try {
            Date date = simpleDateFormat.parse(source);
            return simpleMDHSFormat.format(date);
        } catch (ParseException e) {
            LogUtil.w(e);
        }
        return source;
    }

    public static String getMDHSTime(long source) {

        return simpleMDHSFormat.format(new Date(source));


    }

    public static String getYMDTime(long source) {
        return simpleYHMDateFormat.format(new Date(source));

    }

    public static String getHSTime(long source) {

        return simpleHSFormat.format(new Date(source));


    }

    public static String getImYMDHMMTime(long source) {

        return simpleDateFormat.format(new Date(source));


    }

    //SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy年M月d日");
    public static long getRestTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }

    public static String getCurrentTime() {
        return simpleDateFormat.format(new Date());
    }
    public static String getCurrentTimeForname() {
        return simpleDateFormat2.format(new Date());
    }

    public static String getYMDDate(Date date) {
        return simpleYHMDateFormat.format(date);
    }

    public static String getYMDDate(String source) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy年M月d日");
        Date date = new Date();
        try {
            date = simpleYHMDateFormat.parse(source);
        } catch (ParseException e) {
            LogUtil.w(e);
        }
        return simpleFormat.format(date);
    }

    public static String getActiveTime(Context context, long time) {
        time /= 1000;
        long s = time % 60;
        String ss;

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        time /= 60;
        long m = time % 60;
        String sm;
        if (m < 10) {
            sm = "0" + m;
        } else {
            sm = "" + m;
        }
        time /= 60;
        long h = time;
        String sh;
        if (h < 10) {
            sh = "0" + h;
        } else {
            sh = "" + h;
        }

        return sh + context.getResources().getString(R.string.hour)
                + sm + context.getResources().getString(R.string.minute)
                + ss + context.getResources().getString(R.string.second);
    }

    public static String getPromotionCountDownTime(Context context, long time) {
        time /= 1000;
        long s = time % 60;
        String ss;

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }

        time /= 60;
        long m = time % 60;
        String sm;
        if (m < 10) {
            sm = "0" + m;
        } else {
            sm = "" + m;
        }

        time /= 60;
        long h = time % 24;
        String sh;
        if (h < 10) {
            sh = "0" + h;
        } else {
            sh = "" + h;
        }

        time /= 24;
        long d = time;
        String sd;
        if (d < 10) {
            sd = "0" + d;
        } else {
            sd = "" + d;
        }
        // LogUtil.d("sd = " + sd + " sh = " + sh + " sm = " + sm + " ss = " + ss);
        if (d > 0) {
            return sd + context.getResources().getString(R.string.day)
                    + sh + context.getResources().getString(R.string.hour)
                    + sm + "分"
                    + ss + context.getResources().getString(R.string.second);
        } else { // 不到一天
            return sh + context.getResources().getString(R.string.hour)
                    + sm + "分"
                    + ss + context.getResources().getString(R.string.second);
        }

    }

    public static String getDayRestTime(Context context, long time) {
        time /= 1000;
        long s = time % 60;
        String ss;

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }

        time /= 60;
        long m = time % 60;
        String sm;
        if (m < 10) {
            sm = "0" + m;
        } else {
            sm = "" + m;
        }

        time /= 60;
        long h = time % 24;
        String sh;
        if (h < 10) {
            sh = "0" + h;
        } else {
            sh = "" + h;
        }

        time /= 24;
        long d = time;
        String sd;
        if (d < 10) {
            sd = "0" + d;
        } else {
            sd = "" + d;
        }
        // LogUtil.d("sd = " + sd + " sh = " + sh + " sm = " + sm + " ss = " + ss);
        return sd + context.getResources().getString(R.string.day)
                + sh + context.getResources().getString(R.string.hour)
                + sm + context.getResources().getString(R.string.minute)
                + ss + context.getResources().getString(R.string.second);
    }

    public static String getDayRestTimeMinOneWord(Context context, long time) {
        time /= 1000;
        long s = time % 60;
        String ss;

        if (s < 10) {
            ss = "" + s + context.getResources().getString(R.string.second);
        } else {
            ss = "" + s + context.getResources().getString(R.string.second);
        }

        time /= 60;
        long m = time % 60;
        String sm;
        if (m > 0) {
            sm = m + context.getResources().getString(R.string.minute_one_word);
        } else {
            sm = "";
        }

        time /= 60;
        long h = time % 24;
        String sh;
        if (h > 0) {
            sh = h + context.getResources().getString(R.string.hour);
        } else {
            sh = "";
        }

        time /= 24;
        long d = time;
        String sd;
        if (d > 0) {
            sd = d + context.getResources().getString(R.string.day);
        } else {
            sd = "";
        }
        // LogUtil.d("sd = " + sd + " sh = " + sh + " sm = " + sm + " ss = " + ss);

        return sd
                + sh
                + sm
                + ss;
    }

    public static String getDayLeftTime(Context context, long time) {
        time /= 1000;
        long s = time % 60;
        String ss;

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }

        time /= 60;
        long m = time % 60;
        String sm;
        if (m < 10) {
            sm = "0" + m;
        } else {
            sm = "" + m;
        }

        time /= 60;
        long h = time % 24;
        String sh;
        if (h < 10) {
            sh = "0" + h;
        } else {
            sh = "" + h;
        }

        time /= 24;
        long d = time;
        String sd;
        if (d < 10) {
            sd = "0" + d;
        } else {
            sd = "" + d;
        }

        return sd + ":"
                + sh + ":"
                + sm + ":"
                + ss;
    }

    public static String getBrandReminTime(String time) {
        if (Tao800Util.isNull(time)) {
            return "";
        }

        if (beforeNow(time)) {
            return "";
        }

        Date dEndTime = null;

        try {
            dEndTime = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            LogUtil.w(e);
        }

        long diffHours = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 60 * 1000);
        //毫秒ms
        if (diffHours >= 24) {
            return "剩" + diffHours / 24 + "天";
        } else if (diffHours < 24 && diffHours > 0) {
            if ((dEndTime.getTime() - System.currentTimeMillis()) % (60 * 60 * 1000) > 0) {
                diffHours += 1;
            }
            return "剩" + diffHours + "小时";
        } else if (diffHours == 0) {
            return "剩" + 1 + "小时";
        } else {
            return "";
        }
    }

    public static DateResult getMuYingBrandTimeOther(String startTime, String endTime) {
        //未开始 天数
        if (!beforeNow(startTime)) {
            return new DateResult(DateResult.DATE_RESULT_NO_SELL, "未开始");
        } else {
            return getMuYingBrandReminTime(endTime);
        }
    }

    public static int getNowYear() {
        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getNowMonth() {
        //获取当前年份
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static DateResult getMuYingBrandTime(String startTime, String endTime) {
        //未开始 天数
        if (!beforeNow(startTime)) {
            return getDayDistance(startTime);
        } else {
            return getMuYingBrandReminTime(endTime);
        }
    }


    public static DateResult getBrandForeastTime(String startTime, String endTime) {
        if (!beforeNow(startTime)) {
            return getDayDistanceBrandForeast(startTime);
        } else {
            return getMuYingBrandReminTime(endTime);
        }
    }

    private static DateResult getDayDistanceBrandForeast(String timeSource) {
        Calendar today = Calendar.getInstance();
        Calendar old = Calendar.getInstance();
        int xcs = 0;
        try {
            Date sourceDate = simpleDateFormat.parse(timeSource);
            old.setTime(sourceDate);
            int hour = sourceDate.getHours();
            String strmin = "";
            int min = sourceDate.getMinutes();
            if (min < 10) {
                strmin = "0" + min;
            } else {
                strmin = min + "";
            }

            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            old.set(Calendar.HOUR_OF_DAY, 0);
            old.set(Calendar.MINUTE, 0);
            old.set(Calendar.SECOND, 0);
            old.set(Calendar.MILLISECOND, 0);

            long intervalMilli = old.getTimeInMillis() - today.getTimeInMillis();
            xcs = (int) (intervalMilli / (24 * 60 * 60 * 1000));

            if (xcs == 0) {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, "今日" + hour + ":" + strmin + "点开卖");
            } else if (xcs == 1) {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, "明日" + hour + ":" + strmin + "点开卖");
            } else {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, xcs + "日后开卖");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
    }

    public static DateResult getBrandTime(String startTime, String endTime) {
        //未开始 天数
        if (!beforeNow(startTime)) {
            //return  getDayDistanceBrandForeast(startTime);
            return getMyBrandReminTime(startTime);
        } else {
            return getMyBrandReminTime(endTime);
        }
    }

    private static DateResult getMyDayDistance(String timeSource) {
        Calendar today = Calendar.getInstance();
        Calendar old = Calendar.getInstance();
        int xcs = 0;
        try {
            Date sourceDate = simpleDateFormat.parse(timeSource);
            old.setTime(sourceDate);
            int hour = sourceDate.getHours();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            old.set(Calendar.HOUR_OF_DAY, 0);
            old.set(Calendar.MINUTE, 0);
            old.set(Calendar.SECOND, 0);
            old.set(Calendar.MILLISECOND, 0);

            long intervalMilli = old.getTimeInMillis() - today.getTimeInMillis();
            xcs = (int) (intervalMilli / (24 * 60 * 60 * 1000));

            if (xcs == 0) {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, "今日" + hour + "点开卖");
            } else if (xcs == 1) {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, "明日" + hour + "点开卖");
            } else {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, xcs + "日后开卖");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
    }

    public static DateResult getRestTimeCatgoryFengQiang() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long diff = cal.getTimeInMillis() - System.currentTimeMillis();
        long diffDay = (diff) / (24 * 60 * 60 * 1000);//天
        long diffHours = (diff) / (60 * 60 * 1000);//小时
        long diffMinus = (diff) / (60 * 1000);//分
        long diffMiao = (diff) / (1000);//秒
        if (diffMiao > 0) {
            String hours = "";
            if (diffHours % 24 >= 10) {
                hours = diffHours % 24 + "";
            } else {
                hours = "0" + diffHours % 24;
            }

            String min = "";
            if (diffMinus % 60 >= 10) {
                min = diffMinus % 60 + "";
            } else {
                min = "0" + diffMinus % 60;
            }

            String mao = "";
            if (diffMiao % 60 >= 10) {
                mao = diffMiao % 60 + "";
            } else {
                mao = "0" + diffMiao % 60;
            }
            return new DateResult(DateResult.DATE_RESULT_LAST_DAY, diffDay + "," + hours + "," + min + "," + mao + "");
        } else {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }

    }

    private static DateResult getMyBrandReminTime(String endTime) {

        if (Tao800Util.isNull(endTime)) {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }

        if (beforeNow(endTime)) {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }

        Date dEndTime = null;

        try {
            dEndTime = simpleDateFormat.parse(endTime);
        } catch (ParseException e) {
            LogUtil.w(e);
        }
        long diffDay = (dEndTime.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);//天
        long diffHours = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 60 * 1000);//小时
        long diffMinus = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 1000);//分
        long diffMiao = (dEndTime.getTime() - System.currentTimeMillis()) / (1000);//秒


        if (diffMiao > 0) {
            String hours = "";
            if (diffHours % 24 >= 10) {
                hours = diffHours % 24 + "";
            } else {
                hours = "0" + diffHours % 24;
            }

            String min = "";
            if (diffMinus % 60 >= 10) {
                min = diffMinus % 60 + "";
            } else {
                min = "0" + diffMinus % 60;
            }

            String mao = "";
            if (diffMiao % 60 >= 10) {
                mao = diffMiao % 60 + "";
            } else {
                mao = "0" + diffMiao % 60;
            }
            if (diffDay >= 1) {
                return new DateResult(DateResult.DATE_RESULT_LAST_DAY, diffDay + "," + hours + "," + min + "," + mao + "");
            } else if (diffHours >= 1) {
                return new DateResult(DateResult.DATE_RESULT_LAST_DAY, diffDay + "," + hours + "," + min + "," + mao + "");
            } else if (diffMinus >= 1) {
                return new DateResult(DateResult.DATE_RESULT_LAST_DAY, diffDay + "," + hours + "," + min + "," + mao + "");
            } else {
                return new DateResult(DateResult.DATE_RESULT_LAST_DAY, diffDay + "," + hours + "," + min + "," + mao + "");
            }

        } else {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }
   /*     //毫秒ms
        if (diffHours >= 24) {
            return new DateResult(DateResult.DATE_RESULT_LAST_DAY, "剩" + diffHours / 24 + "天");


        } else if (diffHours < 24 && diffHours > 0) {
            //有小时时，有分钟 直接抹掉
            return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "剩" + diffHours + "小时");
        } else if (diffHours == 0) {
            //小于一小时，大于一分钟时，显示分钟数
            long diffMinutes = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 1000);
            if (diffMinutes >= 1) {
                return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "剩" + diffMinutes + "分钟");
            } else {
                return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "即将下架");
            }
        } else {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }*/
    }


//    public static DateResult getMuYingBrandReminTimeOther(String time) {
//        if (Tao800Util.isNull(time)) {
//            new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
//        }
//
//        if (beforeNow(time)) {
//            new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
//        }
//
//        Date dEndTime = null;
//
//        try {
//            dEndTime = simpleDateFormat.parseAll(time);
//        } catch (ParseException e) {
//            LogUtil.w(e);
//        }
//
//        long diffHours = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 60 * 1000);
//        //毫秒ms
//        if (diffHours >= 24) {
//            return new DateResult(DateResult.DATE_RESULT_LAST_DAY, "剩" + diffHours / 24 + "天");
//        } else if (diffHours < 24 && diffHours > 0) {
//            if ((dEndTime.getTime() - System.currentTimeMillis()) % (60 * 60 * 1000) > 0) {
//                diffHours += 1;
//            }
//            return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "剩" + diffHours + "小时");
//        } else if (diffHours == 0) {
//            return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "剩" + 1 + "小时");
//        } else {
//            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
//        }
//    }

    private static DateResult getDayDistance(String timeSource) {
        Calendar today = Calendar.getInstance();
        Calendar old = Calendar.getInstance();
        int xcs = 0;
        try {
            Date sourceDate = simpleDateFormat.parse(timeSource);
            old.setTime(sourceDate);
            int hour = sourceDate.getHours();

            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            old.set(Calendar.HOUR_OF_DAY, 0);
            old.set(Calendar.MINUTE, 0);
            old.set(Calendar.SECOND, 0);
            old.set(Calendar.MILLISECOND, 0);

            long intervalMilli = old.getTimeInMillis() - today.getTimeInMillis();
            xcs = (int) (intervalMilli / (24 * 60 * 60 * 1000));

            if (xcs == 0) {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, "今日" + hour + "点开卖");
            } else if (xcs == 1) {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, "明日" + hour + "点开卖");
            } else {
                return new DateResult(DateResult.DATE_RESULT_NO_SELL, xcs + "日后开卖");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
    }

    public static DateResult getMuYingBrandReminTime(String time) {
        if (Tao800Util.isNull(time)) {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }

        if (beforeNow(time)) {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }

        Date dEndTime = null;

        try {
            dEndTime = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            LogUtil.w(e);
        }

        long diffHours = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 60 * 1000);
        //毫秒ms
        if (diffHours >= 24) {
            return new DateResult(DateResult.DATE_RESULT_LAST_DAY, "剩" + diffHours / 24 + "天");
        } else if (diffHours < 24 && diffHours > 0) {
            //有小时时，有分钟 直接抹掉
            return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "剩" + diffHours + "小时");
        } else if (diffHours == 0) {
            //小于一小时，大于一分钟时，显示分钟数
            long diffMinutes = (dEndTime.getTime() - System.currentTimeMillis()) / (60 * 1000);
            if (diffMinutes >= 1) {
                return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "剩" + diffMinutes + "分钟");
            } else {
                return new DateResult(DateResult.DATE_RESULT_LAST_HOUR, "即将下架");
            }
        } else {
            return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
        }
    }


    public static class DateResult {

        public static final int DATE_RESULT_NO_SELL = 0;
        public static final int DATE_RESULT_LAST_DAY = 1;
        public static final int DATE_RESULT_LAST_HOUR = 2;
        public static final int DATE_RESULT_NO_NULL = 3;
        public static final int DATE_RESULT_LAST_MIAO = 4;
        public static final int DATE_RESULT_LAST_MIN = 5;
        private int type;
        private String result;

        public DateResult(int type, String result) {
            this.type = type;
            this.result = result;
        }

        public int getType() {
            return type;
        }

        public String getResult() {
            return result;
        }

    }

    /**
     * 是今天还是明天
     *
     * @param timeSource
     * @return 0今天 ,1 明天, -1异常
     */
    public static int isTomorrowDay(String timeSource) {
        if (Tao800Util.isNull(timeSource)) {
            return -1;
        }
        try {
            Date date = simpleDateFormat.parse(timeSource);
            if (simpleDayDateFormat.format(new Date().getTime()).equals(simpleDayDateFormat.format(date))) {
                return 0;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            if (simpleDayDateFormat.format(new Date().getTime()).equals(simpleDayDateFormat.format(date))) {
                return 1;
            }
        } catch (ParseException e) {
            LogUtil.d("getYesterdayTodayTomorrow -- error" + e.getMessage());
        }

        return -1;
    }

    public static boolean isYesterday(long longTime) {
        if (longTime <= 0) return false;

        //消息时间+1天如果等于当前时间，那么消息时间就是昨天
        Calendar calendarMsg = Calendar.getInstance();
        calendarMsg.setTimeInMillis(longTime);
        calendarMsg.add(calendarMsg.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        Date dateMsgMsg = calendarMsg.getTime(); //这个时间就是日期往后推一天的结果
        if (simpleDayDateFormat.format(new Date().getTime()).equals(simpleDayDateFormat.format(dateMsgMsg))) {
            return true;
        }
        return false;
    }

    public static String getSellTipListItemTime(String timeSource) {
        int timeStatue = isTomorrowDay(timeSource);
        if (timeStatue == 0) {
            return "今日" + (timeSource.substring(timeSource.indexOf(" ") + 1, timeSource.lastIndexOf(":"))) + "开抢";
        } else if (timeStatue == 1) {
            return "明日" + (timeSource.substring(timeSource.indexOf(" ") + 1, timeSource.lastIndexOf(":"))) + "开抢";
        } else {
            return (getMDHSTime(timeSource)) + "开抢";
        }
    }

    /**
     * 计算剩余时间
     * 显示倒计时：该品牌距离下架的时间。超过24小时的，显示X天；不足24小时的，显示X小时。举两个栗子：
     * 1、剩余4天05小时15分35秒，显示为“剩4天”
     * 2、剩余5小时15分35秒，显示为“剩5小时”
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String getfakeDealTime(long endTime) {
        long residueTime = endTime - System.currentTimeMillis();
        long time = 0;
        String unit = "天";
        if (SuApplication.getInstance().getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.GINGERBREAD) {
            time = residueTime / (1000 * 3600 * 24);
            if (time <= 0) {
                unit = "小时";
                time = residueTime / (1000 * 3600);
                if (time <= 0) {
                    unit = "分";
                    time = residueTime / (1000 * 60);
                    if (time <= 0) {
                        unit = "秒";
                        time = residueTime / 1000;
                    }
                }
            }
        } else {
            time = TimeUnit.MILLISECONDS.toDays(residueTime);
            if (time <= 0) {
                unit = "小时";
                time = TimeUnit.MILLISECONDS.toHours(residueTime);
                if (time <= 0) {
                    unit = "分";
                    time = TimeUnit.MILLISECONDS.toMinutes(residueTime);
                    if (time <= 0) {
                        unit = "秒";
                        time = TimeUnit.MILLISECONDS.toSeconds(residueTime);
                    }
                }
            }
        }
        return "剩余" + time + unit;
    }

}
