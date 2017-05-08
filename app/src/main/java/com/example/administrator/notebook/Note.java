package com.example.administrator.notebook;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class Note extends DataSupport{
    private String date;
    private String bitmap;
    private String content;
    private String title;
    private String type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
