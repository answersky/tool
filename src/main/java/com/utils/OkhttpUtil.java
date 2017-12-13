package com.utils;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author answer
 *         2017/11/23
 */
public class OkhttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String getRequest(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String getRequest(String url,Integer time) throws IOException {
        client.setConnectTimeout(time, TimeUnit.SECONDS);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postRequest(String url, Map<String, Object> param) throws IOException {
        String json = null;
        if (param != null && param.size() > 0) {
            json = JsonUtils.objectToJson(param);
        }
        if (json == null) {
            return null;
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void main(String[] args) {
        /*try {
            String result = getRequest("http://api.map.baidu.com/place/v2/search?query=ATM%E6%9C%BA&tag=%E9%93%B6%E8%A1%8C&region=%E5%8C%97%E4%BA%AC&output=json&ak=n1mGTnCWGFRA94q35ulIE4rT");
            System.out.println(JsonUtils.mapFormJSONStr(result));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("organCode", "14101");
        param.put("consNo1", "111255445614455");
        param.put("consNameDiv", "1111111");
        param.put("orgNoDiv", "11111");
        param.put("queryPwd1", "011111");
        param.put("code1", "1111");
        try {
            String result=postRequest("http://www.95598.cn/95598/per/shortCut/checkQueryPwd",param);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
