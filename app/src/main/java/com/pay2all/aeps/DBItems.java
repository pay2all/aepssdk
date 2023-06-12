package com.pay2all.aeps;

public class DBItems {


    public String id;
    public String mobile_number;
    public String pan_number;
    public String first_name;
    public String last_name;
    public String email;
    public String pin_code;
    public String status_id;
    public String user_id;
    public String aadhar_number;
    public String company;
    public String address;
    public String created_at;
    public String updated_at;
    public String balance="00.00";
    public String aeps_balance="00.00";
    public String bank_account_number;
    public String ifsc;
    public String agent_id_code;
    public String agent_id_code_matm;

    public String getAgent_id_code_matm() {
        return agent_id_code_matm;
    }

    public void setAgent_id_code_matm(String agent_id_code_matm) {
        this.agent_id_code_matm = agent_id_code_matm;
    }

    public String getAgent_id_code() {
        return agent_id_code;
    }

    public void setAgent_id_code(String agent_id_code) {
        this.agent_id_code = agent_id_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAeps_balance() {
        return aeps_balance;
    }

    public void setAeps_balance(String aeps_balance) {
        this.aeps_balance = aeps_balance;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public DBItems(String id, String mobile_number, String pan_number,
                   String first_name, String last_name, String email, String pin_code, String status_id,
                   String user_id, String aadhar_number, String company, String address, String created_at,
                   String updated_at, String balance, String aeps_balance,String bank_account_number,
                   String ifsc,String agent_id_code,String agent_id_code_matm)
    {
        this.id=id;
        this.mobile_number=mobile_number;
        this.pan_number=pan_number;
        this.first_name=first_name;
        this.last_name=last_name;
        this.email=email;
        this.pin_code=pin_code;
        this.status_id=status_id;
        this.user_id=user_id;
        this.aadhar_number=aadhar_number;
        this.company=company;
        this.address=address;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.balance=balance;
        this.aeps_balance=aeps_balance;
        this.bank_account_number=bank_account_number;
        this.ifsc=ifsc;
        this.agent_id_code=agent_id_code;
        this.agent_id_code_matm=agent_id_code_matm;
    }

    public DBItems()
    {

    }
}
