package com.demo.apppay2all.FundRequesDetails;

public class BankListItems {

    public String getBankdetail_id() {
        return bankdetail_id;
    }

    public void setBankdetail_id(String bankdetail_id) {
        this.bankdetail_id = bankdetail_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    String bankdetail_id;
    String bank_name;
    String account_number;
    String ifsc_code;
    String branch;
}
