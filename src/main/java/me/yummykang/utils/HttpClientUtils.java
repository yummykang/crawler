package me.yummykang.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * httpclient工具类
 *
 * Created by Demon on 2016/11/30 0030.
 */
public class HttpClientUtils {
    /**
     * 执行get方法
     *
     * @param url
     * @return
     */
    public static String doGet(String url) throws IOException {
        String result = null;
        // 设置代理
        CloseableHttpClient httpClient;
        if (ProxyPool.proxyMap.size() != 0) {
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(ProxyPool.getRandomHttpHost());
            httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (SymbianOS/9.1; U; en-us) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // 实例化输入流，并获取网页代码
            String s; // 依次循环，至到读的值为空
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s + " ");
            }
            reader.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
