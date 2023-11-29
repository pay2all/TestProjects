package com.demo.apppay2all.ServicesDetails;

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

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getBbps() {
        return bbps;
    }

    public void setBbps(String bbps) {
        this.bbps = bbps;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    String id;
    String name;
    String service_image;
    String bbps;
    int image=0;
}
