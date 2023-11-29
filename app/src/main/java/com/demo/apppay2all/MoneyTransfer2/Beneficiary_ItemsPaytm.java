package com.demo.apppay2all.MoneyTransfer2;

import java.io.Serializable;

/**
 * Created by Basant on 6/10/2017.
 */

public class Beneficiary_ItemsPaytm implements Serializable {


    private String beneficiry_name;
    private String accountno;
    private String bank;
    private String recepient_id;
    private String ifsc;

//    here sender number is not getting from sever, we are setting its as per user enter
    private String sender_number;


    public String getSender_number() {
        return sender_number;
    }

    public void setSender_number(String sender_number) {
        this.sender_number = sender_number;
    }



    public String getRecepient_id() {
        return recepient_id;
    }

    public void setRecepient_id(String recepient_id) {
        this.recepient_id = recepient_id;
    }



    public String getBeneficiry_name() {
        return beneficiry_name;
    }

    public void setBeneficiry_name(String beneficiry_name) {
        this.beneficiry_name = beneficiry_name;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
}
