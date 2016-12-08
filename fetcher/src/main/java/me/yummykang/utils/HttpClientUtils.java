package me.yummykang.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * httpclient工具类
 * <p>
 * Created by Demon on 2016/11/30 0030.
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * 执行get方法
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        String result = null;
        // 设置代理
        CloseableHttpClient httpClient;
        RequestConfig requestConfig;
        if (ProxyPool.proxyMap.size() != 0) {
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(ProxyPool.getRandomHttpHost());
            httpClient = HttpClients.custom().setRoutePlanner(routePlanner).build();
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                    .setSocketTimeout(5000).setProxy(ProxyPool.getRandomHttpHost()).build();
        } else {
            httpClient = HttpClients.createDefault();
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                    .setSocketTimeout(5000).build();
        }

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (SymbianOS/9.1; U; en-us) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.error("*********************返回状态码为：{}*********************", statusCode);
            if (statusCode != 200) {
                logger.error("*********************请求失败，状态码：{}*********************", statusCode);
            }
            InputStream inputStream = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // 实例化输入流，并获取网页代码
            String s; // 依次循环，至到读的值为空
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
            result = sb.toString();
        } catch (Exception e) {
            doGet(url);
            logger.error("*********************请求失败，重新请求*********************");
        }
        return result == null ? "" : result;
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://blog.csdn.net/u012660464/article/details/53463708").header("User-Agent", "Mozilla/5.0 (SymbianOS/9.1; U; en-us) AppleWebKit/413 (KHTML, like Gecko) Safari/413").get();
        System.out.println();
    }
}
