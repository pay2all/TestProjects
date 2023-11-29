package com.demo.apppay2all.LoginMVVMDetails;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginModel {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONObject getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(JSONObject broadcast) {
        this.broadcast = broadcast;
    }

    public JSONObject getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(JSONObject userdetails) {
        this.userdetails = userdetails;
    }

    public JSONObject getUserservices() {
        return userservices;
    }

    public void setUserservices(JSONObject userservices) {
        this.userservices = userservices;
    }

    public JSONObject getCompanydetails() {
        return companydetails;
    }

    public void setCompanydetails(JSONObject companydetails) {
        this.companydetails = companydetails;
    }

    public JSONArray getBanner() {
        return banner;
    }

    public void setBanner(JSONArray banner) {
        this.banner = banner;
    }

    public JSONObject getErrors() {
        return errors;
    }

    public void setErrors(JSONObject errors) {
        this.errors = errors;
    }

    public Status getStatus_type() {
        return status_type;
    }

    public void setStatus_type(Status status_type) {
        this.status_type = status_type;
    }

    String message;
    String status;
    JSONObject broadcast;
    JSONObject userdetails;
    JSONObject userservices;
    JSONObject companydetails;
    JSONArray banner;
    JSONObject errors;
    Status status_type;
}