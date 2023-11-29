package com.demo.apppay2all.ServiceDetailSub;

import com.demo.apppay2all.ServiceDetailsNew.ServicesItems;

import java.io.Serializable;

public class SubServiceItem implements Serializable {

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

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getReport_url() {
        return report_url;
    }

    public void setReport_url(String report_url) {
        this.report_url = report_url;
    }

    public String getReport_is_static() {
        return report_is_static;
    }

    public void setReport_is_static(String report_is_static) {
        this.report_is_static = report_is_static;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    String service_id;
    String service_name;
    String service_image="";
    String bbps;
    String report_title;
    String report_url;
    String report_is_static;
    int image=0;

    ServicesItems servicesItems=null;

    public ServicesItems getServicesItems() {
        return servicesItems;
    }

    public void setServicesItems(ServicesItems servicesItems) {
        this.servicesItems = servicesItems;
    }
}