package com.demo.apppay2all.LedgerDetail;

public class Ledger_Item {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvider_image() {
        return provider_image;
    }

    public void setProvider_image(String provider_image) {
        this.provider_image = provider_image;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }

    private String id;
    private String provider_image;
    private String txnid;
    private String date;
    private String description;
    private String debit;
    private String credit;
    private String opening_balance;

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    private String commission;

    public String getClosing_balance() {
        return closing_balance;
    }

    public void setClosing_balance(String closing_balance) {
        this.closing_balance = closing_balance;
    }

    private String closing_balance;




}
