package me.yummykang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * desc the file.
 *
 * @author demon
 * @Date 2016/12/2 14:40
 */
public class SpiderInit {
    private static Logger logger = LoggerFactory.getLogger(SpiderInit.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(20);

    public void start() {
        logger.info("**********************抓取目标数据开始执行************************");
        for (int i = 0; i < 20; i++) {
            Spider spider = new Spider();
            if (!executor.isShutdown()) {
                executor.submit(spider);
            }
        }

    }

    public static void main(String[] args) throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        FetcherInit fetcherInit = new FetcherInit();
        new Thread(fetcherInit).start();
        new SpiderInit().start();
    }
}
