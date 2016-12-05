package me.yummykang;

import me.yummykang.bean.FetcherUrl;
import me.yummykang.utils.HttpClientUtils;
import me.yummykang.utils.MongodbUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * desc the file.
 *
 * @author demon
 * @Date 2016/12/2 14:37
 */
public class Spider implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Spider.class);

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FetcherUrl fetcherUrl = FetcherInit.urlQueue.poll();
            String result = HttpClientUtils.doGet(fetcherUrl.getUrl());
            if (result != null && !"".equals(result)) {
                logger.info("********添加一条抓取的数据到solr,url:{}************", fetcherUrl.getUrl());
                String urlString = "http://localhost:8983/solr/blogs";
                SolrClient solr = new HttpSolrClient.Builder(urlString).build();
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", UUID.randomUUID());
                document.addField("url", fetcherUrl.getUrl());
                document.addField("text", result);
                // 未被解析
                document.addField("status", 0);
                try {
                    solr.add(document);
                    solr.commit();

                    MongodbUtils.update(fetcherUrl.getUrl());
                    logger.info("********修改url:{}的使用状态为已使用************", fetcherUrl.getUrl());
                } catch (SolrServerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("********抓取数据失败，将url重新添加至队尾************", fetcherUrl.getUrl());
                FetcherInit.urlQueue.add(fetcherUrl);
            }
        }
    }
}
