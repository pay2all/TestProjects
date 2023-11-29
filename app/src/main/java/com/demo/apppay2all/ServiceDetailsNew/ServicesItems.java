package com.demo.apppay2all.ServiceDetailsNew;

import org.json.JSONArray;

import java.io.Serializable;

public class ServicesItems implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    String id;
    String name;
    String data="";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
