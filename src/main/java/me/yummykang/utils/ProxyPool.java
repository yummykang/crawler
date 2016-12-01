package me.yummykang.utils;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 代理池，从http://api.xicidaili.com/free2016.txt取数据.
 * 每隔10分钟取一次
 *
 * @author demon
 * @Date 2016/12/1 13:36
 */
public class ProxyPool {
    private static Logger logger = LoggerFactory.getLogger(ProxyPool.class);

    public static Map<String, Integer> proxyMap = new HashMap<>();

    static {
        Timer timer = new Timer();
        timer.schedule(new ProxyInfoFetcherTask(), 1000, 1000 * 60 * 15);
    }

    /**
     * 随机获取一个代理
     *
     * @return
     */
    public static HttpHost getRandomHttpHost() {
        if (proxyMap.size() == 0) {
            return null;
        } else {
            Random random = new Random();
            List<String> keys = new ArrayList<>(proxyMap.keySet());
            String randomKey = keys.get(random.nextInt(keys.size()));
            Integer value = proxyMap.get(randomKey);
            logger.info("**************使用代理{}:{}****************", randomKey, value);
            return new HttpHost(randomKey, value, "http");
        }
    }
}
