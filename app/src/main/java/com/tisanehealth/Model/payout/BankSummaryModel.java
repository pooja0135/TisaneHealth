package com.tisanehealth.Model.payout;

public class BankSummaryModel {

    String member_id,name,benificary_name,benificary_ifsc,beneficary_account,beneficary_bank,generate_date;
    String matching_income,direct_income,turnoverIncome,admin_charge,tds,payable_amount,transfer_date;
    String CrawnShipIncome,LeaderShipIncome,PANNo;


    public String getCrawnShipIncome() {
        return CrawnShipIncome;
    }

    public void setCrawnShipIncome(String crawnShipIncome) {
        CrawnShipIncome = crawnShipIncome;
    }

    public String getLeaderShipIncome() {
        return LeaderShipIncome;
    }

    public void setLeaderShipIncome(String leaderShipIncome) {
        LeaderShipIncome = leaderShipIncome;
    }

    public String getPANNo() {
        return PANNo;
    }

    public void setPANNo(String PANNo) {
        this.PANNo = PANNo;
    }



    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBenificary_name() {
        return benificary_name;
    }

    public void setBenificary_name(String benificary_name) {
        this.benificary_name = benificary_name;
    }

    public String getBenificary_ifsc() {
        return benificary_ifsc;
    }

    public void setBenificary_ifsc(String benificary_ifsc) {
        this.benificary_ifsc = benificary_ifsc;
    }

    public String getBeneficary_account() {
        return beneficary_account;
    }

    public void setBeneficary_account(String beneficary_account) {
        this.beneficary_account = beneficary_account;
    }

    public String getBeneficary_bank() {
        return beneficary_bank;
    }

    public void setBeneficary_bank(String beneficary_bank) {
        this.beneficary_bank = beneficary_bank;
    }

    public String getGenerate_date() {
        return generate_date;
    }

    public void setGenerate_date(String generate_date) {
        this.generate_date = generate_date;
    }

    public String getMatching_income() {
        return matching_income;
    }

    public void setMatching_income(String matching_income) {
        this.matching_income = matching_income;
    }

    public String getDirect_income() {
        return direct_income;
    }

    public void setDirect_income(String direct_income) {
        this.direct_income = direct_income;
    }

    public String getTurnoverIncome() {
        return turnoverIncome;
    }

    public void setTurnoverIncome(String roi_income) {
        this.turnoverIncome = roi_income;
    }

    public String getAdmin_charge() {
        return admin_charge;
    }

    public void setAdmin_charge(String admin_charge) {
        this.admin_charge = admin_charge;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getTransfer_date() {
        return transfer_date;
    }

    public void setTransfer_date(String transfer_date) {
        this.transfer_date = transfer_date;
    }






    public BankSummaryModel(String member_id,String name,String benificary_name,String benificary_ifsc,String beneficary_account,String beneficary_bank,String generate_date,String matching_income,String direct_income,String turnoverIncome,String admin_charge,String tds,String payable_amount,String CrawnShipIncome,String LeaderShipIncome,String PANNo)
    {
       this.member_id=member_id;
       this.name=name;
       this.benificary_name=benificary_name;
       this.benificary_ifsc=benificary_ifsc;
       this.beneficary_account=beneficary_account;
       this.beneficary_bank=beneficary_bank;
       this.generate_date=generate_date;
       this.matching_income=matching_income;
       this.direct_income=direct_income;
       this.turnoverIncome=turnoverIncome;
       this.admin_charge=admin_charge;
       this.tds=tds;
       this.payable_amount=payable_amount;
       this.payable_amount=payable_amount;
       this.CrawnShipIncome=CrawnShipIncome;
       this.LeaderShipIncome=LeaderShipIncome;
       this.PANNo=PANNo;


    }


}
