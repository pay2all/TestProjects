package com.demo.apppay2all.CommissionDetail.MyComission;

import java.io.Serializable;

public class CommissionServiceItems implements Serializable {

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
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

    String service_id;
    String service_name;
    String service_image;
    String bbps;
}
