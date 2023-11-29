package com.demo.apppay2all.FundRequesDetails;

public class Payment_Request_Report_Items {

    private String id;
    private String date;
    private String method;
    private String bank;
    private String refrenceno;
    private String amount;
    private String deposit_date;
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

    public String getDeposit_date() {
        return deposit_date;
    }

    public void setDeposit_date(String deposit_date) {
        this.deposit_date = deposit_date;
    }

    private String status;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRefrenceno() {
        return refrenceno;
    }

    public void setRefrenceno(String refrenceno) {
        this.refrenceno = refrenceno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
