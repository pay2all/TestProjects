package com.demo.apppay2all.RechargeReportMVVM;

public class ReportModel {

    private String id;
    private String date;
    private String provider;
    private String txnid;
    private String commisson;
    private String status;
    private String total_balance;
    private String number;
    private String amount;
    private String status_icon;
    private String type;
    private String name;
    private String ol;
    private String openingbalance;
    private String providerimage;

    public ReportModel(String id, String date, String provider, String txnid, String commisson, String status, String total_balance, String number, String amount, String status_icon, String type, String name, String ol, String openingbalance, String providerimage) {
        this.id = id;
        this.date = date;
        this.provider = provider;
        this.txnid = txnid;
        this.commisson = commisson;
        this.status = status;
        this.total_balance = total_balance;
        this.number = number;
        this.amount = amount;
        this.status_icon = status_icon;
        this.type = type;
        this.name = name;
        this.ol = ol;
        this.openingbalance = openingbalance;
        this.providerimage = providerimage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getCommisson() {
        return commisson;
    }

    public void setCommisson(String commisson) {
        this.commisson = commisson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus_icon() {
        return status_icon;
    }

    public void setStatus_icon(String status_icon) {
        this.status_icon = status_icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOl() {
        return ol;
    }

    public void setOl(String ol) {
        this.ol = ol;
    }

    public String getOpeningbalance() {
        return openingbalance;
    }

    public void setOpeningbalance(String openingbalance) {
        this.openingbalance = openingbalance;
    }

    public String getProviderimage() {
        return providerimage;
    }

    public void setProviderimage(String providerimage) {
        this.providerimage = providerimage;
    }
}
