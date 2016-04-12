package com.hxuehh.reuse_Process_Imp.appSetting.UrlConfigDomain;

import android.text.TextUtils;

import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.FaceHttpParamBuilder;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.utils.TaoCCUtil;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kait
 * Date: 12-5-19
 * Time: 下午4:23
 * To change this template use File | SettingsActivity | File Templates.
 */
public class TaoAPI {

    private final static int WORK_CONFIG = 0;
    private final static int TEST_CONFIG = 1;
    private static BaseNetwork mNetwork;

    public static BaseNetwork getNetwork() {
        if (mNetwork == null) {
            init();
        }
        return mNetwork;
    }

    public static void init() {
//        int netStatus = WORK_CONFIG;
//        InputStream is = null;
//        try {
//            Properties properties = new Properties();
//            is = SuApplication.getInstance().getResources().getAssets().open("network.properties");
//            properties.load(is);
//            netStatus = Integer.valueOf(properties.getProperty("test_environment", "0"));
//        } catch (IOException e) {
//            netStatus = TEST_CONFIG;
//            LogUtil.w(e);
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    LogUtil.w(e);
//                }
//            }
//        }

        int netStatus = AppConfig.getInstance().NETWORK_PROPERTIES_TEST_ENVIRONMENT;
        if (netStatus == TEST_CONFIG) {
            mNetwork = new TestNetwork();
        } else {
            mNetwork = new OfficialNetwork();
        }
    }

    /**
     * Build a complete request URL base on a request parameter list.
     *
     * @param params
     * @return
     */
    public static String parseGetUrl(List<NameValuePair> params, String path) {
        StringBuilder sBuffer = new StringBuilder();
        if (!TextUtils.isEmpty(path)) {
            sBuffer.append(path);
        }

        if (TaoCCUtil.isEmpty(params))
            return sBuffer.toString();

        int cntParams = params.size();
        for (int i = 0; i < cntParams; i++) {
            NameValuePair param = params.get(i);
            if (i == 0) {
                sBuffer.append("?");
            } else {
                sBuffer.append("&");
            }

            //如果是分类列表的KEY,则直接拼上Value即可，服务器返回的值里面已经拼好了的
            if(FaceHttpParamBuilder.class.equals(param.getName())){
                sBuffer.append(param.getValue());
            }else{
                sBuffer.append(param.getName()).append("=").append(android.net.Uri.encode(param.getValue()));
            }

        }
        LogUtil.d("url = " + sBuffer.toString());
        return sBuffer.toString();
    }

    /**
     * Build a complete request URL base on a request parameter list.
     *
     * @param params
     * @return
     */
    public static String parseBackJsonValue(List<NameValuePair> params) {
        StringBuilder sBuffer = new StringBuilder("{");
        if (TaoCCUtil.isEmpty(params)) return sBuffer.append("}").toString();

        int cntParams = params.size();
        for (int i = 0; i < cntParams; i++) {
            NameValuePair param = params.get(i);
            sBuffer.append('"')
                    .append(param.getName())
                    .append('"').append(":")
                    .append('"').append(param.getValue()).append('"');

            if (i != cntParams - 1) {
                sBuffer.append(",");
            }
        }

        return sBuffer.append("}").toString();
    }

}