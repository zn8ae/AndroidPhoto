package com.example.zihanni.photoapp;

/**
 * Created by zihanni on 12/7/17.
 */

public class Upload {
    public String name;
    public String url;
    public String description;

    public Upload() {

    }

    public Upload(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() { return description; }
}
