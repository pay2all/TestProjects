package com.demo.apppay2all.ProviderDetail;


import com.demo.apppay2all.ServiceDetailSub.SubServiceItem;
import com.demo.apppay2all.ServicesDetails.ServicesItems;

import java.io.Serializable;

/**
 * Created by admin on 3/29/2018.
 */

public class Operators_Items {
    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator_code() {
        return operator_code;
    }

    public void setOperator_code(String operator_code) {
        this.operator_code = operator_code;
    }

    public String getOperator_image() {
        return operator_image;
    }

    public void setOperator_image(String operator_image) {
        this.operator_image = operator_image;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(SubServiceItem data) {
        this.data = data;
    }

    public String getOperator_message() {
        return operator_message;
    }

    public void setOperator_message(String operator_message) {
        this.operator_message = operator_message;
    }

    public String getOperator_lenth() {
        return operator_lenth;
    }

    public void setOperator_lenth(String operator_lenth) {
        this.operator_lenth = operator_lenth;
    }

    private String operator_id;
    private String operator_code;
    private String operator_image;
    private String operator_name;
    private String from;
    private String service_id;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    private SubServiceItem data;

    private String operator_message;
    private String operator_lenth;


}
