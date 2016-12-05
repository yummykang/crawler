package me.yummykang;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * desc the file.
 *
 * @author demon
 * @Date 2016/12/5 16:11
 */
public class SolrUtils {
    private static final String urlString = "http://localhost:8983/solr/blogs";
    private static final SolrClient solr = new HttpSolrClient.Builder(urlString).build();

    public static void search() {
        SolrQuery params = new SolrQuery();
        // 查询关键词，*:*代表所有属性、所有值，即所有index
        // params.set("q", "*:*");
        params.set("q", "status:0");// 查询nickname是已chm开头的数据

        // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        params.set("start", 0);
        params.set("rows", 5);

        // 按nickname排序，asc升序 desc降序
        params.set("sort", "id asc");

        try {
            QueryResponse rsp = solr.query(params);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("文档个数：" + docs.getNumFound());
            System.out.println("查询时间：" + rsp.getQTime());
            for (SolrDocument doc : docs) {
                System.out.println(doc.getFieldValue("text"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        search();//实际应用过程中可以根据自身需要传参
    }
}
