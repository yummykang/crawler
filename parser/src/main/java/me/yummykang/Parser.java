package me.yummykang;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * write some dec. here.
 * Created by Demon on 2016/12/5 0005.
 */
public class Parser implements Runnable {
    @Override
    public void run() {
        while (true) {
            SourceData sourceData = ParserInit.textQueue.pollFirst();
            if (sourceData != null) {
                Document document = Jsoup.parse(sourceData.getText());
                Elements contentElements = document.select("div[class~=(.*)postBody(.*)]");
                Elements titleElements = document.select("h1[class~=(.*)postTitle(.*)]");
                Element content = contentElements.first();
                Element title = titleElements.first();
                PureData pureData = new PureData();
                pureData.setTitle(title.text());
                pureData.setContent(content.text());
                String urlString = "http://localhost:8983/solr/blogs_data";
                SolrClient solr = new HttpSolrClient.Builder(urlString).build();
                try {
                    solr.addBean(pureData);
                    solr.commit();

                    SolrUtils.update(sourceData.getId());
                    ParserInit.textQueue.addAll(SolrUtils.search());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SolrServerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Document document = Jsoup.parse("");
        Elements elements = document.select("div[class~=(.*)postBody(.*)]");
        Element element = elements.first();
        System.out.println(element.text());
    }
}
