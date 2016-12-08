package me.yummykang;

import org.apache.solr.client.solrj.beans.Field;

/**
 * desc the file.
 *
 * @author demon
 * @Date 2016/12/8 15:27
 */
public class PureData {
    @Field("title")
    private String title;

    @Field("content")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
