package me.yummykang;

import me.yummykang.bean.FetcherUrl;
import me.yummykang.utils.HttpClientUtils;
import me.yummykang.utils.MongodbUtils;
import me.yummykang.utils.ProxyInfoFetcherTask;
import me.yummykang.utils.SimpleBloomFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * write some dec. here.
 * Created by Demon on 2016/11/30 0030.
 */
public class FetcherInit {

    private static ConcurrentLinkedQueue<FetcherUrl> urlQueue = new ConcurrentLinkedQueue<>();

    private static SimpleBloomFilter bloomFilter = new SimpleBloomFilter();

    static {
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

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
//        String url = "http://blog.csdn.net/code/newarticle.html";
//
//        Document document = Jsoup.parse(HttpClientUtils.doGet(url));
//        Elements elements = document.select("a[href~=http://blog.csdn.net/[0-9a-zA-z]*/article/details/[0-9]*]");
//        for (Element element : elements) {
//            System.out.println(element.text());
//        }
        fetchUrl("http://blog.csdn.net/", "http://blog.csdn.net/code/newarticle.html", "http://blog.csdn.net/[0-9a-zA-z]*/article/details/[0-9]*");
    }

    public static void fetchUrl(String domain, String seedUrl, String regex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
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
}
