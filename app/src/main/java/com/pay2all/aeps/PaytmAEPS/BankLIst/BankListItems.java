package com.pay2all.aeps.PaytmAEPS.BankLIst;

public class BankListItems {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_imps() {
        return is_imps;
    }

    public void setIs_imps(String is_imps) {
        this.is_imps = is_imps;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String id;
    private String bank_name;
    private String ifsc;
    private String bank_code;
    private String status;
    private String is_imps;
    private String icon;
}
