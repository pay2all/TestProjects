package com.demo.apppay2all.ui.reports;

public class AllReportItem {

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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    String id="";
    String name;
    String service_image="";
    int image=0;

    String sub_report="";

    public String getSub_report() {
        return sub_report;
    }

    public void setSub_report(String sub_report) {
        this.sub_report = sub_report;
    }
}