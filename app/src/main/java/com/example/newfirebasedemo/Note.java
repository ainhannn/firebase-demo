package com.example.newfirebasedemo;

import java.util.Date;

public class Note {
    private String id;
    private Date init;
    private String title;
    private String content;
    private String image;

    public Note() { }

    public Note(String id, Date init, String title, String content, String image) {
        this.id = id;
        this.init = init;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInit() {
        return init;
    }

    public void setInit(Date init) {
        this.init = init;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
