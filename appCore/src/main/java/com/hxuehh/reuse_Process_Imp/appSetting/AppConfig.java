package com.hxuehh.reuse_Process_Imp.appSetting;

import android.content.res.AssetManager;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-4-11
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 * <p/>
 * DO NOT change the static variables!
 */

//配置参数  主要是app运行的一些固定参数
//
public class AppConfig implements Serializable {

//    应该是使用方法提取 aidl区域
//    AppStaticSetting  全部是静态



    public String CLIENT_TAG = "TaoCC";
    public String PRODUCT_TAG = "TaoCC";

    public static String PARTNER_ID = "6x0eae";
    public static String SHOW_ZIFEI = "0";
    public static String UMENG = "0";


    public static String LOG_TAG = "/zhenCC9_android";

    public boolean LOG_ERR_FEED = AppStaticSetting.LOG_ERR_FEED == 1;
    public static boolean LOG_CLOSED = AppStaticSetting.LOG_CLOSED == 1;
    public boolean LOG_ERR_SAVE = AppStaticSetting.LOG_ERR_SAVE == 1;



    public int userGrade = 1;
    public int NETWORK_PROPERTIES_TEST_ENVIRONMENT = 0;
    public long POLLING_INTERVAL = AppStaticSetting.POLLING_INTERVAL;

    private static AppConfig feedBack;
    public static AppConfig getInstance() {
        if (feedBack == null) {
            feedBack = new AppConfig();
            feedBack.init();
        }
        return feedBack;
    }

    public static void setInstance(AppConfig mAppConfig) {
        feedBack = mAppConfig;
    }


    public AppConfig init() {
        AssetManager am = SuApplication.getInstance().getAssets();
        try {
            // readConfigXml(am);
            readPartnerXml(am);
        } catch (IOException e) {
            LogUtil.w(e);
        }
        return this;
    }



    private static void readPartnerXml(AssetManager am) throws IOException {
        InputStream in = null;
        try {
            in = am.open("partner.xml");
            SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
            sp.parse(in, new PartnerXmlHandler());
        } catch (IOException e) {
            LogUtil.w(e);
        } catch (Exception e) {
            LogUtil.w(e);
        } finally {
            if (null != in) in.close();
        }
    }

    private static class PartnerXmlHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if ("partner".equalsIgnoreCase(localName)) {
                PARTNER_ID = StringUtil.getValueOrDefault(attributes.getValue("id"), PARTNER_ID);
                SHOW_ZIFEI = StringUtil.getValueOrDefault(attributes.getValue("zifei"), SHOW_ZIFEI);
                UMENG = StringUtil.getValueOrDefault(attributes.getValue("umeng"), UMENG);
            }
        }
    }


}