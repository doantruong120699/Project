package com.example.hello;

public class dataNews {
    int id;
    String name;
    String type;
    String linkrss;

    public dataNews(int id, String name, String type, String linkrss) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.linkrss = linkrss;
    }

    public dataNews(String name, String type, String linkrss) {
        super();
        this.name = name;
        this.type = type;
        this.linkrss = linkrss;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getlinkrss() {
        return linkrss;
    }

    public void setlinkrss(String linkrss) {
        this.linkrss = linkrss;
    }
}


