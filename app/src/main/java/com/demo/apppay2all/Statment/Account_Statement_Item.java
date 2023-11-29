package com.demo.apppay2all.Statment;

public class Account_Statement_Item {

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

    private String type;
    public  String getOl() {
        return ol;
    }

    public void setOl(String ol) {
        this.ol = ol;
    }

    private String ol;

    public String getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }

    private String opening_balance;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderimage() {
        return providerimage;
    }

    public void setProviderimage(String providerimage) {
        this.providerimage = providerimage;
    }

    private String name="";
    private String providerimage;

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


}
