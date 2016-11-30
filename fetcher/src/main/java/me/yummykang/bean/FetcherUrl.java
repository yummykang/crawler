package me.yummykang.bean;

/**
 * desc the file.
 *
 * @author demon
 * @Date 2016/11/30 15:29
 */
public class FetcherUrl {
    private String url;

    private int status;

    public FetcherUrl() {
    }

    public FetcherUrl(String url, int status) {
        this.url = url;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
