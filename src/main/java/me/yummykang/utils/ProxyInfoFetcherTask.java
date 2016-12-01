package me.yummykang.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimerTask;

/**
 * 获取代理信息的任务.
 *
 * @author demon
 * @Date 2016/12/1 14:04
 */
public class ProxyInfoFetcherTask extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(ProxyInfoFetcherTask.class);

    private static final String API_URL = "http://api.xicidaili.com/free2016.txt";

    @Override
    public void run() {
        logger.info("*******************获取代理Ip及端口，并添加到代理池中***************");
        String result = null;
        try {
            result = HttpClientUtils.doGet(API_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 这个地方最好用正则匹配一下
        if (result != null) {
            String[] hostPorts = result.split(" ");
            for (String hostPort : hostPorts) {
                String[] hostAndPort = hostPort.split(":");
                if (hostAndPort.length == 2) {
                    ProxyPool.proxyMap.put(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
                }
            }
        }
    }

    public static void main(String[] args) {
//        System.out.println(HttpClientUtils.doGet(API_URL));
        System.out.println("118.114.41.65:8998 39.50.207.159:8080".matches("(\\d{1,3}\\.\\d{1,3}.\\d{1,3}.\\d{1,3}:\\d{4}\\s{1}){1,}"));
    }
}
