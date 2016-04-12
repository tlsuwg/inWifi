package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.ProgressOutHttpEntity;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.HttpRequester;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.SSLCustomSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: chenjishi
 * Date: 11-4-11
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class NetworkWorker {

    private static final int CONNECTION_TIMEOUT = 25 * 1000;
    private static final int SO_SOCKET_TIMEOUT = 60 * 1000;

    public static final int NATIVE_ERROR = 600;
    public static final int UNKNOWN_HOST = 601;
    public static final int SOCKET_TIMEOUT = 602;

    public static ConnectivityManager sConnectivityManager;

    private ExecutorService threadPool;

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    private static NetworkWorker inst ;
    public static NetworkWorker getInstance() {
        if(inst==null)inst = new NetworkWorker();
        return inst;
    }



    public NetworkWorker() {
        // fixed thread pool
        threadPool = Executors.newFixedThreadPool(5,new ThreadFactory() {
            int i=0;
            @Override
            public Thread newThread(Runnable r) {
                Thread mThread=       new Thread(r,"NetworkWorker "+i++);
                mThread.setPriority(Thread.MIN_PRIORITY );
//                mThread.setPriority(5);
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                return mThread;
            }
        });
    }



    public DefaultHttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SO_SOCKET_TIMEOUT);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        schemeRegistry.register(new Scheme("https", SSLCustomSocketFactory.getSocketFactoryByBKS("TuanDD_1.bks","BKS"), 443));
//        schemeRegistry.register(new Scheme("https", SSLCustomSocketFactory.getSocketFactoryByBKS("TuanDD_2.bks","BKS"), 443));
//        schemeRegistry.register(new Scheme("https", SSLCustomSocketFactory.getSocketFactoryByCer("TuanDD2.cer"), 443));
//        schemeRegistry.register(new Scheme("https", SSLCustomSocketFactory.getSocketFactoryByCer("sso2.cer"), 443));
        schemeRegistry.register(new Scheme("https", SSLCustomSocketFactory.getSocketFactoryDef(), 443));

        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(cm, params);
    }

    private synchronized void closeHttpClientConnection(DefaultHttpClient client) {
        if (client != null) {
            client.getConnectionManager().shutdown();
        }
    }


    /**
     * Simple asynchronous get with response, must be used in a looped thread(like ui thread)
     *
     * @param url
     * @param callback
     */

    @Deprecated
    public void get(final String url, final ICallback callback, final Object... params) {

        if (callback == null) throw new IllegalArgumentException("callback must not be empty");

        final Handler handler = new Handler(Looper.getMainLooper());
        final DefaultHttpClient client = getHttpClient();

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                String result = null;
                int status = NATIVE_ERROR;

                try {
                    HttpGet m = new HttpGet(url);
                    HttpRequester requester = generalRequester(params);
                    requester.handlerHttpHeader(client, m);

                    final HttpResponse response = client.execute(m);
                    HttpEntity entity = response.getEntity();
                    status = response.getStatusLine().getStatusCode();

                    Header contentEncoding = response.getFirstHeader("Content-Encoding");

                    if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                        InputStream in = new GZIPInputStream(entity.getContent());
                        result = StringUtil.getFromStream(in);
                    } else {
                        result = EntityUtils.toString(entity);
                    }
                } catch (final Exception e) {
                    LogUtil.w(e);

                    result = e.getMessage();
                    if (e instanceof UnknownHostException) {
                        status = UNKNOWN_HOST;
                    } else if (e instanceof InterruptedIOException) {
                        status = SOCKET_TIMEOUT;
                    }
                } finally {
                    closeHttpClientConnection(client);
                    callbackForUI(handler, callback, status, result);
                }
            }
        });
    }
    /**
     * Simple asynchronous get without response
     * @param url
     */
    public void get(final String url, final Object... params) {

        final DefaultHttpClient client = getHttpClient();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpGet m = new HttpGet(url);
                    HttpRequester requester = generalRequester(params);
                    requester.handlerHttpHeader(client, m);
                    client.execute(m);
                } catch (Exception e) {
                    LogUtil.w(e);
                } finally {
                    closeHttpClientConnection(client);
                }
            }
        });
    }

    /**
     * Synchronous get string result from url
     *
     * @param url
     * @return
     */
    public String getSync(String url, final Object... params) throws Exception {

        String result;
        DefaultHttpClient client = getHttpClient();

        try {
            HttpResponse response = getResponse(client, url, params);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");

                if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                    InputStream in = new GZIPInputStream(entity.getContent());
                    result = StringUtil.getFromStream(in);
                } else {
                    result = EntityUtils.toString(entity);
                }
                return result;
            } else {
                throw new NetworkException(status, "status");
            }
        } finally {
            closeHttpClientConnection(client);
        }

    }

    /**
     * Synchronous get string result from url
     *
     * @param url
     * @return
     */
    public void getCallBack(String url, ICallback callback, final Object... params) {
        if (callback == null) throw new IllegalArgumentException("callback must not be empty");
        String result;
        DefaultHttpClient client = getHttpClient();
        int status = NATIVE_ERROR;
        try {
            HttpResponse response = getResponse(client, url, params);
            status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");

                if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                    InputStream in = new GZIPInputStream(entity.getContent());
                    result = StringUtil.getFromStream(in);
                } else {
                    result = EntityUtils.toString(entity);
                }
                callback.onResponse(status, result);
                return ;
            } else {
                callback.onResponse(status, "");
                throw new NetworkException(status, "status");
            }
        } catch (Exception e) {
            result = e.getMessage();
            if (e instanceof UnknownHostException) {
                status = UNKNOWN_HOST;
            } else if (e instanceof InterruptedIOException) {
                status = SOCKET_TIMEOUT;
            }
            LogUtil.w(e);
            callback.onResponse(status, "");
        } finally {
            closeHttpClientConnection(client);
        }

    }

    /**
     * Synchronous get response with last modified header
     *
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public HttpResponse getResponse(DefaultHttpClient client, String url, final Object... params) throws Exception {
        HttpGet m = new HttpGet(url);
        HttpRequester requester = generalRequester(params);
        requester.handlerHttpHeader(client, m);
        return client.execute(m);
    }

    /**
     * Asynchronous post with response callback, must be used in a looped thread(like ui thread)
     *
     * @param url
     * @param callback
     * @param params
     */
    public void post(final String url, final ICallback callback, final Object... params) {
        if (callback == null) throw new IllegalArgumentException("callback must not be empty");

        final Handler handler = new Handler();
        final DefaultHttpClient client = getHttpClient();

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                String result = null;
                int status = NATIVE_ERROR;
                try {
                    HttpPost m = new HttpPost(url);
                    HttpRequester requester = generalRequester(params);
                    requester.handlerHttpHeader(client, m);
                    final HttpResponse response = client.execute(m);

                    if (requester.isSaveHeaders()) {
                        requester.setResponseHeaders(response.getAllHeaders());
                    }

                    HttpEntity entity = response.getEntity();
                    status = response.getStatusLine().getStatusCode();
                    result = EntityUtils.toString(entity);
                } catch (final Exception e) {
                    LogUtil.w(e);

                    result = e.getMessage();
                    if (e instanceof UnknownHostException) {
                        status = UNKNOWN_HOST;
                    } else if (e instanceof InterruptedIOException) {
                        status = SOCKET_TIMEOUT;
                    }
                } finally {
                    closeHttpClientConnection(client);
                    callbackForUI(handler, callback, status, result);
                }
            }
        });
    }



    /**
     * Simple asynchronous post without response
     *
     * @param url
     * @param params
     */
    public void post(final String url, final Object... params) {

        final DefaultHttpClient client = getHttpClient();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpPost m = new HttpPost(url);
                    HttpRequester requester = generalRequester(params);
                    requester.handlerHttpHeader(client, m);
                    client.execute(m);
                } catch (Exception e) {
                    LogUtil.w(e);
                } finally {
                    closeHttpClientConnection(client);
                }
            }
        });
    }

    /**
     * Synchronous post
     *
     * @param url
     * @param params
     * @return
     */
    public String postSync(String url, Object... params) {

        String result = null;
        DefaultHttpClient client = getHttpClient();
        try {
            HttpPost m = new HttpPost(url);
            HttpRequester requester = generalRequester(params);
           // requester.setCookie(CookieTable.getInstance().getCookieByDomain(ParamBuilder.DOMAIN));
            requester.handlerHttpHeader(client, m);
            HttpResponse response = client.execute(m);
            result = EntityUtils.toString(response.getEntity());

            if (requester.isSaveHeaders()) {
                requester.setResponseHeaders(response.getAllHeaders());
            }
        } catch (Exception e) {
            LogUtil.w(e);
        } finally {
            closeHttpClientConnection(client);
        }

        return result;
    }

    /**
     * Synchronous post
     *
     * @param url
     * @param params
     * @return
     */
    public void postCallBack(String url, ICallback callback, Object... params) {
        if (callback == null) throw new IllegalArgumentException("callback must not be empty");

        String result = null;
        DefaultHttpClient client = getHttpClient();
        int status = NATIVE_ERROR;
        try {
            HttpPost m = new HttpPost(url);
            HttpRequester requester = generalRequester(params);
            requester.handlerHttpHeader(client, m);

            HttpResponse response = client.execute(m);
            status = response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity());

            if (requester.isSaveHeaders()) {
                requester.setResponseHeaders(response.getAllHeaders());
            }

            callback.onResponse(status, result);
        } catch (Exception e) {
            result = e.getMessage();
            if (e instanceof UnknownHostException) {
                status = UNKNOWN_HOST;
            } else if (e instanceof InterruptedIOException) {
                status = SOCKET_TIMEOUT;
            }

            callback.onResponse(status, result);
            LogUtil.w(e);
        } finally {
            closeHttpClientConnection(client);
        }
    }

    public HttpResponse getResponse2(DefaultHttpClient client, String url, boolean isPost, final Object... params) throws Exception {

        HttpRequestBase m = null;
        if (isPost) {
            m = new HttpPost(url);
        } else {
            m = new HttpGet(url);
        }
        HttpRequester requester = generalRequester(params);
        requester.handlerHttpHeader(client, m);
        return client.execute(m);
    }



    public HttpRequester generalRequester(Object... params) throws Exception {

        if (params == null || params.length == 0) {
            return new HttpRequester();
        }

        if (params.length > 0 && !(params[0] instanceof HttpRequester)) {
            throw new IllegalArgumentException("Http request need a HttpRequester param in the first");
        }

        return (HttpRequester) params[0];
    }

    private void callbackForUI(Handler handler, final ICallback callback, final int code, final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(code, msg);
            }
        });
    }

    /**
     * Check current net connection type.E,g wifi, cmnet ,cmwap etc.
     *
     * @return
     */
    public static String getNetMode() {
        String netMode = "";
        if (sConnectivityManager == null) {
            sConnectivityManager = (ConnectivityManager) SuApplication.getInstance().getSystemService("connectivity");
        }
        final NetworkInfo mobNetInfo = sConnectivityManager.getActiveNetworkInfo();

        if (mobNetInfo != null && mobNetInfo.isAvailable()) {
            int netType = mobNetInfo.getType();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                netMode = mobNetInfo.getTypeName();
            } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                netMode = mobNetInfo.getExtraInfo();
            }
        }

        return netMode;
    }

    /**
     * Asynchronous http get/post callback
     */
    public interface ICallback {
        public void onResponse(int status, String result);
    }

    public class NetworkException extends Exception {

        private static final long serialVersionUID = -2841294936395077461L;

        public int status = NATIVE_ERROR;

        public NetworkException() {
            super();
        }

        public NetworkException(int status) {
            super();
            this.status = status;
        }

        public NetworkException(String message) {
            super(message);
        }

        public NetworkException(int status, String message) {
            super(message);
            this.status = status;
        }

        public NetworkException(String message, Throwable cause) {
            super(message, cause);
        }

        public NetworkException(Throwable cause) {
            super(cause);
        }

        @Override
        public String getMessage() {
            return "status："+status+" "+super.getMessage();
        }
    }


    /**
     * 提交数据到服务器
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.xxx.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
//    public static String postFile(String path, Map<String, String> params, FormFile file, FaceCommCallBack faceCommCallBackPro) throws Exception {
//        return post(path, params, new FormFile[]{file}, faceCommCallBackPro);
//    }


    public static String postFile(String path,Map<String, String> params, File file, FaceCommCallBack faceCommCallBackPro) throws Exception {
        return uploadFile(path, params, file, faceCommCallBackPro);
    }




    /**
     * 上传文件到服务器
     * @param file 需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String CHARSET = "utf-8"; // 设置编码

    public static String uploadFile(String RequestURL, Map<String, String> param, File file, FaceCommCallBack faceCommCallBackPro)
            throws Exception {

        String result = null;
        URL url = new URL(RequestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIME_OUT);
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST"); // 请求方式
        conn.setRequestProperty("Charset", CHARSET); // 设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
// conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

/**
 * 当文件不为空，把文件包装并且上传
 */
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        StringBuffer sb = null;
        String params = "";

/***
 * 以下是用于上传参数
 */
        if (param != null && param.size() > 0) {
            Iterator<String> it = param.keySet().iterator();
            while (it.hasNext()) {
                sb = null;
                sb = new StringBuffer();
                String key = it.next();
                String value = param.get(key);
                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                sb.append(value).append(LINE_END);
                params = sb.toString();

                dos.write(params.getBytes());
// dos.flush();
            }
        }

        sb = null;
        params = null;
        sb = new StringBuffer();
/**
 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
 * filename是文件的名字，包含后缀名的 比如:abc.png
 */
        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
        sb.append("Content-Disposition:form-data; name=\"" +file.getName()
                + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
        sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
        sb.append(LINE_END);
        params = sb.toString();
        sb = null;
        dos.write(params.getBytes());

        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;
        int allLen = 0;
        long filebytes = file.length();
        while ((len = is.read(bytes)) != -1) {
            dos.write(bytes, 0, len);
            faceCommCallBackPro.callBack((int) (allLen * 100d / filebytes));
        }
        is.close();
        dos.write(LINE_END.getBytes());
        byte[] end_data = (PREFIX + PREFIX + LINE_END)
                .getBytes();
        dos.write(end_data);
        dos.flush();
        faceCommCallBackPro.callBack(99);
        int res = conn.getResponseCode();
        if (res == 200) {
            InputStream input = conn.getInputStream();
            StringBuffer sb1 = new StringBuffer();
            int ss;
            while ((ss = input.read()) != -1) {
                sb1.append((char) ss);
            }
            result = sb1.toString();
        } else {
            throw new Exception("!200");
        }

        faceCommCallBackPro.callBack(100);
        return result;
    }



    public static String uploadFileClient(String url, File file, final FaceCommCallBack faceCommCallBackPro) { // 这个是使用了Appache 的上传组件来进行的， 已经封装好了的上传

        // 保存需上传文件信息
        MultipartEntity entitys = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
                null, Charset.forName(HTTP.UTF_8));

        entitys.addPart("file", new FileBody(file));
        HttpEntity httpEntity = entitys;
        final long totalSize = httpEntity.getContentLength();
        ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity( // 这里封装了下，可以显示进度信息， 就是在数据写入到流的write方法中， 添加进度回调
                httpEntity, new ProgressOutHttpEntity.ProgressListener() {
            @Override
            public void transferred(long transferedBytes) {
                int curProgress = (int) (100 * transferedBytes / totalSize);
                if(curProgress > 99){
                    curProgress = 99;
                }
                faceCommCallBackPro.callBack(curProgress);
            }
        }
        ); // 在进行文件上传时，进度会很快，及数据很快就写到了流中了，

        String result = null;

        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        // 设置连接超时时间
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
                Charset.forName("UTF-8"));
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(progressHttpEntity);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 这里会阻塞了很久，获取不到状态响应码
                result = EntityUtils.toString(resEntity, HTTP.UTF_8);
                faceCommCallBackPro.callBack(100);
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        faceCommCallBackPro.callBack(100);
        return result;
    }



}