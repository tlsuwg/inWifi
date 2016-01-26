package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil;

import android.os.Handler;

import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.HttpRequester;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by IntelliJ IDEA.
 * Date: 13-1-5
 * Time: 下午1:58
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequestUtils {

    public static DefaultHttpClient getDefaultHttpClient() {

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 60000);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", getSocketFactory(), 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(cm, params);
    }



    public static void get(final String url, final NetworkWorker.ICallback callback, final Object... params) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                int status = NetworkWorker.NATIVE_ERROR;
                DefaultHttpClient client = getDefaultHttpClient();

                try {
                    HttpRequester requester = generalRequester(params);
                    HttpResponse response =getResponse(url,client,requester);

                    HttpEntity entity = response.getEntity();
                    status = response.getStatusLine().getStatusCode();
                    Header contentEncoding = response.getFirstHeader("Content-Encoding");

                    if (requester.isSaveHeaders()) {
                        requester.setResponseHeaders(response.getAllHeaders());
                    }

                    if (null != contentEncoding && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
                        InputStream in = new GZIPInputStream(entity.getContent());
                        result = StringUtil.getFromStream(in);
                    } else {
                        result = EntityUtils.toString(entity);
                    }
                } catch (Exception e) {
                    LogUtil.w(e);

                    result = e.getMessage();
                    if (e instanceof UnknownHostException) {
                        status = NetworkWorker.UNKNOWN_HOST;
                    } else if (e instanceof InterruptedIOException) {
                        status = NetworkWorker.SOCKET_TIMEOUT;
                    }
                }finally {
                    if (client != null) {
                        client.getConnectionManager().shutdown();
                    }
                    callbackForUI(handler,callback,status,result);
                }

            }
        }).start();
    }

    private static void callbackForUI(Handler handler, final NetworkWorker.ICallback callback, final int status, final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(status, msg);
            }
        });
    }


    public static HttpResponse getResponse(String url, DefaultHttpClient client, HttpRequester requester) throws Exception{
        HttpGet m = new HttpGet(url);
        requester.handlerHttpHeader(client, m);
        return client.execute(m);
    }

    public static HttpRequester generalRequester(Object... params) throws Exception {

        if (params == null || params.length == 0) {
            return new HttpRequester();
        }

        if (params.length > 0 && !(params[0] instanceof HttpRequester)) {
            throw new IllegalArgumentException("Http request need a HttpRequester param in the first");
        }

        return (HttpRequester) params[0];
    }

    private static final String CLIENT_KET_PASSWORD = "123456";//私钥密码
    private static final String CLIENT_TRUST_PASSWORD = "123456";//信任证书密码

    public static SSLSocketFactory getSocketFactory() {

        SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

//            trustStore.load(mContext.getBaseContext()
//                    .getResources()
//                    .openRawResource(R.drawable.lt_client),CLIENT_TRUST_PASSWORD.toCharArray());

//            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("X509");
//            trustManager.select_device_type(trustStore);

            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sf;
    }

    public static class MySSLSocketFactory extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");


        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);


//            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("X509");
//            trustManager.select_device_type(truststore);
//            sslContext.select_device_type(null,trustManager.getTrustManagers(),null);

        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }


}
