package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil;

import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.app.OSinfo.SuNetEvn;
import com.hxuehh.reuse_Process_Imp.staticKey.FaceHttpParamBuilderKeys;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceEcxeption.InternalServerException;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.HttpRequester;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.FaceHttpParamBuilder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

/**
 * Created by suwg .
 *
 * /将来会涉及使用一个cache
 //    这个是使用一个单例？  还是？static调用？
 //    此类只是加载abslist数据的 ，也就是deal数据   列表数据  结合304进行DB数据状态查询
 */

public class FaceHttpLoadForAbsViewGetProducer {

    static FaceHttpLoadForAbsViewGetProducer instance = null;

    public static FaceHttpLoadForAbsViewGetProducer getInstance() {
        if (instance == null)
            instance = new FaceHttpLoadForAbsViewGetProducer();
        return instance;
    }



//    private void handleMaxAge(Header cacheControl) {
//        if (cacheControl == null) return;
//
//        // 上层设置了cacheTime为-1，表示强制不缓存不处理max-age
//        if (((HttpCacher) cacher).getMaxAge() < 0) return;
//
//        String ccValue = cacheControl.getAidlValue();
//        if (StringUtil.isEmpty(ccValue)) return;
//
//        String[] controls = ccValue.split(",");
//        if (controls.length == 0) return;
//
//        String[] singleControl;
//
//        try {
//            for (String c : controls) {
//                if (!c.contains("max-age")) continue;
//
//                singleControl = c.split("=");
//                if (singleControl.length > 1) {
//                    // max-age 以秒为单位
//                    ((HttpCacher) cacher).setMaxAge(Long.parseLong(singleControl[1]) * 1000);
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(e);
//        }
//    }


    public boolean produceByHttp(String url, FaceHttpParamBuilder mLoadParameters, FaceLoadCallBack mFaceCallBack, boolean isPost05,long key) {

        if (!SuNetEvn.getInstance().isHasNet()) {
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_no_net, new Exception("没有网络"));
            return false;
        }
        String   getUrlString = mLoadParameters.parseGetUrl(url);
        Su.log("load 参数：" + getUrlString);

        if (null == getUrlString) {
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_no_url, new IllegalArgumentException("HttpGet need a string param as url."));
            return false;
        }

//        String lastModified = null;
//        if (cacher instanceof HttpCacher) {
//            lastModified = ((HttpCacher) cacher).getLastModified(params);
//        }

        HttpRequester requester = new HttpRequester();
//        if (requester == null) {//这里设置 request
//            requester = new HttpRequester();
//            HashMap<String, String> headers = new HashMap<String, String>();
//            headers.put("If-Modified-Since", lastModified);
//            requester.setRequestHeaders(headers);
//        } else {
//            requester.getRequestHeaders().put("If-Modified-Since", lastModified);
//        }


        if(mLoadParameters.isSetUserInfo()){
            mLoadParameters.setUserInfo();
        }

        if(mLoadParameters.getHead()!=null){
            requester.setRequestHeaders(mLoadParameters.getHead());
        }

        if (mLoadParameters.getCookie() != null) { // 设置cookie
            requester.setCookie(mLoadParameters.getCookie());
        }

        DefaultHttpClient client = NetworkWorker.getInstance().getHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000    );
        HttpResponse response = null;
        try {
            response = NetworkWorker.getInstance().getResponse2(client, getUrlString, isPost05, requester);
        } catch (Exception e) {
            LogUtil.w(e);
            if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_link, new FaceException(e,"201503191553"));
            } else if (e instanceof InternalServerException) {
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_server, new FaceException(e,"201503191554"));
            } else if(e instanceof UnknownHostException) {
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_UnknownHostException, new FaceException(e,"201503191556"));
            }else{
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_server, new FaceException(e,"201503191555"));
            }
            return false;
        }

        try {
            int status = response.getStatusLine().getStatusCode();
            if (200 == status) {
                HttpEntity entity = response.getEntity();
                String result;
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                    InputStream in = new GZIPInputStream(entity.getContent());
                    result = StringUtil.getFromStream(in);
                } else {
                    result = EntityUtils.toString(entity);
                }

                try{
                mFaceCallBack.onCallBackData(FaceLoadCallBack.FROM_HTTP, response,result, null,key);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
//            else if (304 == status) {
//                // 返回的是304，不理会max-age，直接使用应用自身的设置
//                handleLastModified(response.getFirstHeader("Last-Modified"));
//                return cacher.getCachedData(request.getHashKey());
//            }
              else if(status==304){
                return true;
            }
            else if (status == 404) {// 404
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_link, new InternalServerException(404+""));
                return false;
            } else if (status >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {//500
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_server, new InternalServerException(status+""));
                return false;
            } else {
                mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_server, new Exception("Network exception " + status));
                return false;
            }
        } catch (IOException e) {
//            LogUtil.d("http get producer excepton = " + e);
            LogUtil.w(e);
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_link,new FaceException(e,"201503191556"));
            return false;
//            throw e;
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }

    }


    public boolean produce(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, boolean isPost05,long key) {
        FaceHttpParamBuilder mLoadParameters = (FaceHttpParamBuilder) mLoadSetting.getLoadParameters();
        String url = mLoadSetting.getGetSelfByUrl();
        boolean isReLoadFromSart = mLoadSetting.isReLoadFromSart;//是不是从头记载 需要加载完成之后清理URL数据
        int page = isReLoadFromSart ? 1 : mLoadSetting.getLoadingpage();
        if(isReLoadFromSart&&
                (mLoadSetting.MustHybridizationLoadType
                        ==LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain
                ||mLoadSetting.MustHybridizationLoadType
                ==LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains)){
            mLoadSetting.setBaseAccurate();
        }

        if (mLoadSetting.MustLoadParmType == LoadCursorSetting.MustLoadParmType_Limit) {
            mLoadParameters.put(FaceHttpParamBuilderKeys.LIMIT, mLoadSetting.MustEverTimeNumber);
            mLoadParameters.put(FaceHttpParamBuilderKeys.OFFSET, (page - 1) * mLoadSetting.MustEverTimeNumber);
        } else if (mLoadSetting.MustLoadParmType == LoadCursorSetting.MustLoadParmType_Page) {
            mLoadParameters.put(FaceHttpParamBuilderKeys.PER_PAGE, mLoadSetting.MustEverTimeNumber);
            mLoadParameters.put(FaceHttpParamBuilderKeys.PAGE, page);
        }else if (mLoadSetting.MustLoadParmType == LoadCursorSetting.MustLoadParmType_HybridizationPage) {

            Su.logPullView(isReLoadFromSart+" v6 "+mLoadSetting.isLoadAccurateDataStatus);
            if(isReLoadFromSart||!mLoadSetting.isLoadAccurateDataStatus){
                mLoadParameters.put(FaceHttpParamBuilderKeys.PER_PAGE,100);
            }else{
                mLoadParameters.put(FaceHttpParamBuilderKeys.PER_PAGE, mLoadSetting.MustEverTimeNumber);
            }
            mLoadParameters.put(FaceHttpParamBuilderKeys.PAGE, page);

        } else if (mLoadSetting.MustLoadParmType == LoadCursorSetting.MustLoadParmType_Im_Page) {
            mLoadParameters.put(FaceHttpParamBuilderKeys.IM_Per_PAGE, mLoadSetting.MustEverTimeNumber);
            mLoadParameters.put(FaceHttpParamBuilderKeys.IM_PAGE, page - 1);
        } else if (mLoadSetting.MustLoadParmType == LoadCursorSetting.MustLoadParmType_Pram_Null) {

        } else {
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_http_url_pram, new Exception("加载参数错误"));
            return false;
        }

        return   produceByHttp(url,mLoadParameters, mFaceCallBack, isPost05,key);

    }



}
