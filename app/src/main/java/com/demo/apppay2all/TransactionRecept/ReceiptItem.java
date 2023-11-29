package com.demo.apppay2all.TransactionRecept;

public class ReceiptItem {

    private String receipt_id;
    private String receipt_bank_ref;
    private String receipt_status;
    private String receipt_profit;
    private String receipt_amount;

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getReceipt_bank_ref() {
        return receipt_bank_ref;
    }

    public void setReceipt_bank_ref(String receipt_bank_ref) {
        this.receipt_bank_ref = receipt_bank_ref;
    }

    public String getReceipt_status() {
        return receipt_status;
    }

    public void setReceipt_status(String receipt_status) {
        this.receipt_status = receipt_status;
    }

    public String getReceipt_profit() {
        return receipt_profit;
    }

    public void setReceipt_profit(String receipt_profit) {
        this.receipt_profit = receipt_profit;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

}
