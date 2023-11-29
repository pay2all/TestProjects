package com.demo.apppay2all.PayoutServices;

import java.io.Serializable;

public class BeneficiaryItems implements Serializable {

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(String beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    String sr_no;
    String beneficiary_id;
    String mobile_number;
    String account_number;
    String holder_name;
    String bank_name;
    String ifsc_code;
    String status_id;
}
