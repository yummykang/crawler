package me.yummykang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * write some dec. here.
 * Created by Demon on 2016/12/5 0005.
 */
public class ParserInit {
    public static ConcurrentSkipListSet<SourceData> textQueue = new ConcurrentSkipListSet<>();

    private static Logger logger = LoggerFactory.getLogger(ParserInit.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(20);

    static {
        textQueue.addAll(SolrUtils.search());
    }

    public void start() {
        logger.info("**********************解析目标数据开始执行************************");
        for (int i = 0; i < 20; i++) {
            Parser spider = new Parser();
            if (!executor.isShutdown()) {
                executor.submit(spider);
            }
        }
    }

    public static void main(String[] args) {
        FetcherInit fetcherInit = new FetcherInit();
        new Thread(fetcherInit).start();
        new SpiderInit().start();
        new ParserInit().start();
    }

}
