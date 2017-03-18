package com.lulu.weichatshake.door;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class DemoHttps {
    public static HttpClient getHttpsClient() {
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "ISO-8859-1");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setConnectionTimeout(params, 3000);    // 设置连接超时
        HttpConnectionParams.setSoTimeout(params, 6000);           // 设置请求超时

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("https", SSLTrustAllSocketFactory.getSocketFactory(), 9099));
        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);
        return new DefaultHttpClient(connMgr, params);
    }

    public static JSONObject connectPost(String url, JSONObject parameters) {
        if (url == null || parameters == null) {
            return null;
        }
        HttpClient httpClient = getHttpsClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject json = null;
        try {
            HttpEntity entity_content = new StringEntity(parameters.toString(), "UTF-8"); //设置字符集
            httpPost.setEntity(entity_content);
            HttpResponse httpRes = httpClient.execute(httpPost);
            json = new JSONObject(EntityUtils.toString(httpRes.getEntity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    public static JSONObject connectGet(String url, String parameters) {
        //建立新的httpClient对象和httpPost对象
        HttpClient httpClient = getHttpsClient();

        //处理get_url
        String get_url = url + "?" + parameters;
        HttpGet httpGet = new HttpGet(get_url);
        JSONObject json = null;
        try {
            HttpResponse httpRes = httpClient.execute(httpGet);
            json = new JSONObject(EntityUtils.toString(httpRes.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
