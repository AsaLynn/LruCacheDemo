package com.it.bawei.listview;

/**
 *
 */

public class NewsInfo {
    public NewsInfo() {
    }
    //图片地址(不完整)
    private String cover;
    //标题
    private String subject;
    //内容.
    private String summary;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
