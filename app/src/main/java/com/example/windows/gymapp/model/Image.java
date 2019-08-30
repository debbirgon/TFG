package com.example.windows.gymapp.model;

/**
 * Created by windows on 29/08/2019.
 */

public class Image {
    private String url;
    private String name;

    public Image(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public Image() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
