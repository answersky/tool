package com.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author answer
 *         2017/11/13
 */
public class HttpClientUtil {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        Map<String, String> param = new LinkedHashMap<>();
        String url = "http://www.95598.cn/95598/per/shortCut/checkQueryPwd";
//        param.put("action","yhqfcx");
//        param.put("yhbh","0947160005510216");
//        param.put("sjh","18576435724");
//        param.put("sjyzm","4381");
        httpsRequest(param, url);
    }

    public static String httpsRequest(Map<String, String> map, String url) {
        HttpPost httpPost = null;
        String responseStr = "";
        try {
            String param = JsonUtils.objectToJson(map);

            httpPost = new HttpPost(url);
            HttpEntity requestEntity = new StringEntity(param);
            httpPost.setEntity(requestEntity);
            Header[] headers = {};
            httpPost.setHeaders(headers);
            HttpClient httpClient = createHttpsClient(10000, 10000);
            HttpResponse resp = httpClient.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            responseStr = EntityUtils.toString(entity, "UTF-8");
            if (entity.getContentType() == null) {
                responseStr = new String(responseStr.getBytes("iso-8859-1"), "UTF-8");
            }
            EntityUtils.consume(entity);
//            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }


    public static HttpClient createHttpsClient(int connectionTimeout, int soTimeout) {
        try {
            HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
            HttpConnectionParams.setSoTimeout(params, soTimeout);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            return httpClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
