package me.yummykang;

import me.yummykang.bean.FetcherUrl;
import me.yummykang.utils.HttpClientUtils;
import me.yummykang.utils.MongodbUtils;
import me.yummykang.utils.SimpleBloomFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * write some dec. here.
 * Created by Demon on 2016/11/30 0030.
 */
public class FetcherInit implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(FetcherInit.class);

    public static ConcurrentLinkedQueue<FetcherUrl> urlQueue = new ConcurrentLinkedQueue<>();

    private static SimpleBloomFilter bloomFilter = new SimpleBloomFilter();

    public FetcherInit() {
        List<org.bson.Document> mongodbUrls = MongodbUtils.findAll();
        for (org.bson.Document document : mongodbUrls) {
            String url = document.getString("url");
            if (!bloomFilter.contains(url)) {
                bloomFilter.add(url);
                FetcherUrl fetcherUrl = new FetcherUrl(url, 0);
                urlQueue.add(fetcherUrl);
            }
        }
    }


    private void fetchUrl(String domain, String seedUrl, String regex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        String nextPageUrl = seedUrl;
        Document document = Jsoup.parse(HttpClientUtils.doGet(seedUrl));
        for (String nextPageText : Constants.NEXT_PAGE_TEXT) {
            Elements linkElements = document.select("a:contains(" + nextPageText + ")");
            for (Element element : linkElements) {
                nextPageUrl = element.attr("href");
            }
        }
        Elements elements = document.select("a[href~=" + regex);
        for (Element element : elements) {
            String href = element.attr("href");
            if (!bloomFilter.contains(href)) {
                FetcherUrl fetcherUrl = new FetcherUrl(href, 0);
                bloomFilter.add(href);
                urlQueue.add(fetcherUrl);
                MongodbUtils.insertOne(fetcherUrl);
            }
        }
        if (nextPageUrl != null) {
            if (!nextPageUrl.startsWith(domain)) {
                nextPageUrl = domain + nextPageUrl;
            }
            fetchUrl(domain, nextPageUrl, regex);
        }
    }

    @Override
    public void run() {
        try {
//            fetchUrl("http://blog.csdn.net/", "http://blog.csdn.net/code/newarticle.html", "http://blog.csdn.net/[0-9a-zA-z]*/article/details/[0-9]*");
            fetchUrl("http://www.cnblogs.com/", "http://www.cnblogs.com/", "http://www.cnblogs.com/[0-9a-zA-z]*/p/[0-9]*.html");
        } catch (NoSuchMethodException e) {
            logger.error("fetchUrl方法调用失败，失败原因：{}", e.getCause());
        } catch (IllegalAccessException e) {
            logger.error("fetchUrl方法调用失败，失败原因：{}", e.getCause());
        } catch (InvocationTargetException e) {
            logger.error("fetchUrl方法调用失败，失败原因：{}", e.getCause());
        } catch (IOException e) {
            logger.error("fetchUrl方法调用失败，失败原因：{}", e.getCause());
        }
    }
}
