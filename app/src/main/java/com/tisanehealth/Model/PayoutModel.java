package com.tisanehealth.Model;

public class PayoutModel {
    String dsd_id,dsd_name,bank_account,bank_name,ifsc;
    String total_income,admin_charges,tds,payable,date;
    String self_business,self_business_repurchase,direct_team_business,previous_business,team_business;
    String direct_team_income,gold_level_income,team_income,platinum_income,diamond_income,diamond_distribute_income,platinum_distribute_income;
    String company_turnover,cashback,platinum_acheiver,diamond_acheiver;
    String team_commission;


    public String getTeam_commission() {
        return team_commission;
    }

    public void setTeam_commission(String team_commission) {
        this.team_commission = team_commission;
    }


    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getSelf_business() {
        return self_business;
    }

    public void setSelf_business(String self_business) {
        this.self_business = self_business;
    }

    public String getSelf_business_repurchase() {
        return self_business_repurchase;
    }

    public void setSelf_business_repurchase(String self_business_repurchase) {
        this.self_business_repurchase = self_business_repurchase;
    }

    public String getDirect_team_business() {
        return direct_team_business;
    }

    public void setDirect_team_business(String direct_team_business) {
        this.direct_team_business = direct_team_business;
    }

    public String getPrevious_business() {
        return previous_business;
    }

    public void setPrevious_business(String previous_business) {
        this.previous_business = previous_business;
    }

    public String getTeam_business() {
        return team_business;
    }

    public void setTeam_business(String team_business) {
        this.team_business = team_business;
    }

    public String getDirect_team_income() {
        return direct_team_income;
    }

    public void setDirect_team_income(String direct_team_income) {
        this.direct_team_income = direct_team_income;
    }

    public String getGold_level_income() {
        return gold_level_income;
    }

    public void setGold_level_income(String gold_level_income) {
        this.gold_level_income = gold_level_income;
    }

    public String getTeam_income() {
        return team_income;
    }

    public void setTeam_income(String team_income) {
        this.team_income = team_income;
    }

    public String getPlatinum_income() {
        return platinum_income;
    }

    public void setPlatinum_income(String platinum_income) {
        this.platinum_income = platinum_income;
    }

    public String getDiamond_income() {
        return diamond_income;
    }

    public void setDiamond_income(String diamond_income) {
        this.diamond_income = diamond_income;
    }

    public String getDiamond_distribute_income() {
        return diamond_distribute_income;
    }

    public void setDiamond_distribute_income(String diamond_distribute_income) {
        this.diamond_distribute_income = diamond_distribute_income;
    }

    public String getPlatinum_distribute_income() {
        return platinum_distribute_income;
    }

    public void setPlatinum_distribute_income(String platinum_distribute_income) {
        this.platinum_distribute_income = platinum_distribute_income;
    }

    public String getCompany_turnover() {
        return company_turnover;
    }

    public void setCompany_turnover(String company_turnover) {
        this.company_turnover = company_turnover;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getPlatinum_acheiver() {
        return platinum_acheiver;
    }

    public void setPlatinum_acheiver(String platinum_acheiver) {
        this.platinum_acheiver = platinum_acheiver;
    }

    public String getDiamond_acheiver() {
        return diamond_acheiver;
    }

    public void setDiamond_acheiver(String diamond_acheiver) {
        this.diamond_acheiver = diamond_acheiver;
    }



    public String getDsd_id() {
        return dsd_id;
    }

    public void setDsd_id(String dsd_id) {
        this.dsd_id = dsd_id;
    }

    public String getDsd_name() {
        return dsd_name;
    }

    public void setDsd_name(String dsd_name) {
        this.dsd_name = dsd_name;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getBank_aname() {
        return bank_name;
    }

    public void setBank_aname(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }


    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public String getAdmin_charges() {
        return admin_charges;
    }

    public void setAdmin_charges(String admin_charges) {
        this.admin_charges = admin_charges;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public PayoutModel( String dsd_id, String dsd_name,String bank_account,String bank_name,String ifsc,String total_income,String admin_charges,String tds,String payable,String date, String self_business,String self_business_repurchase,String direct_team_business,String previous_business,String team_business,  String direct_team_income,String gold_level_income,String team_income,String platinum_income,String diamond_income,String diamond_distribute_income,String platinum_distribute_income,  String company_turnover,String cashback,String platinum_acheiver,String diamond_acheiver,String team_commission)
    {
        this.dsd_id=dsd_id;
        this.dsd_name=dsd_name;
        this.bank_account=bank_account;
        this.bank_name=bank_name;
        this.ifsc=ifsc;
        this.total_income=total_income;
        this.admin_charges=admin_charges;
        this.tds=tds;
        this.payable=payable;
        this.date=date;
        this.self_business=self_business;
        this.self_business_repurchase=self_business_repurchase;
        this.direct_team_business=direct_team_business;
        this.previous_business=previous_business;
        this.team_business=team_business;
        this.direct_team_income=direct_team_income;
        this.gold_level_income=gold_level_income;
        this.team_income=team_income;
        this.platinum_income=platinum_income;
        this.diamond_income=diamond_income;
        this.diamond_distribute_income=diamond_distribute_income;
        this.platinum_distribute_income=platinum_distribute_income;
        this.company_turnover=company_turnover;
        this.cashback=cashback;
        this.platinum_acheiver=platinum_acheiver;
        this.diamond_acheiver=diamond_acheiver;
        this.team_commission=team_commission;
    }


}
