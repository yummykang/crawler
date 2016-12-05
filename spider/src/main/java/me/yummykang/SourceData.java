package me.yummykang;

import org.apache.solr.client.solrj.beans.Field;

/**
 * write some dec. here.
 * Created by Demon on 2016/12/5 0005.
 */
public class SourceData {
    @Field("id")
    private String id;

    @Field("url")
    private String url;

    @Field("text")
    private String text;

    @Field("status")
    private int status;

    public SourceData() {
    }

    public SourceData(String id, String url, String text, int status) {
        this.id = id;
        this.url = url;
        this.text = text;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
