package me.yummykang;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * desc the file.
 *
 * @author demon
 * @Date 2016/12/5 16:11
 */
public class SolrUtils {
    private static final String urlString = "http://localhost:8983/solr/blogs";
    private static final SolrClient solr = new HttpSolrClient.Builder(urlString).build();

    /**
     * 查询index
     */
    public static List<String> search() {
        List<String> result = new ArrayList<>();
        SolrQuery params = new SolrQuery();
        // 查询关键词，*:*代表所有属性、所有值，即所有index
        // params.set("q", "*:*");
        params.set("q", "status:0");// 查询nickname是已chm开头的数据

        // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
//        params.set("start", 0);
//        params.set("rows", 5);

        // 按nickname排序，asc升序 desc降序
        params.set("sort", "id asc");

        try {
            QueryResponse rsp = solr.query(params);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("文档个数：" + docs.getNumFound());
            System.out.println("查询时间：" + rsp.getQTime());
            for (SolrDocument doc : docs) {
                result.add((String) ((ArrayList)doc.getFieldValue("text")).get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 修改index的状态值
     *
     * @param id
     * @throws IOException
     * @throws SolrServerException
     */
    public static void update(String id) throws IOException, SolrServerException {
        HashMap<String, Object> oper = new HashMap<>();
        oper.put("set", 1);

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("status", oper);
        doc.addField("id", id);

        solr.add(doc);
        solr.commit();
    }

    public static void main(String[] args) throws IOException, SolrServerException {
        System.out.println(search().size());//实际应用过程中可以根据自身需要传参

//        update("9c32a1c3-f3cb-4ef0-9625-e6f5cf9196e9");
    }
}
