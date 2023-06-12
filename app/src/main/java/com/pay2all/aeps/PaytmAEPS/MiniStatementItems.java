package com.pay2all.aeps.PaytmAEPS;

public class MiniStatementItems {
    private String amount;
    private String date;
    private String narration;
    private String txnType;

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public String getTxnType() {
        return this.txnType;
    }

    public void setTxnType(String str) {
        this.txnType = str;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String str) {
        this.amount = str;
    }

    public String getNarration() {
        return this.narration;
    }

    public void setNarration(String str) {
        this.narration = str;
    }
}
