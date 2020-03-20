package com.tisanehealth.Model.payout;

public class MatchIncomeModel {

    String member_id,cf_left_bv,cf_right_bv,entered_date,left_team_bv,matching_business,matching_incentive,overflow,payable_amount,right_team_bv;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCf_left_bv() {
        return cf_left_bv;
    }

    public void setCf_left_bv(String cf_left_bv) {
        this.cf_left_bv = cf_left_bv;
    }

    public String getCf_right_bv() {
        return cf_right_bv;
    }

    public void setCf_right_bv(String cf_right_bv) {
        this.cf_right_bv = cf_right_bv;
    }

    public String getEntered_date() {
        return entered_date;
    }

    public void setEntered_date(String entered_date) {
        this.entered_date = entered_date;
    }

    public String getLeft_team_bv() {
        return left_team_bv;
    }

    public void setLeft_team_bv(String left_team_bv) {
        this.left_team_bv = left_team_bv;
    }

    public String getMatching_business() {
        return matching_business;
    }

    public void setMatching_business(String matching_business) {
        this.matching_business = matching_business;
    }

    public String getMatching_incentive() {
        return matching_incentive;
    }

    public void setMatching_incentive(String matching_incentive) {
        this.matching_incentive = matching_incentive;
    }

    public String getOverflow() {
        return overflow;
    }

    public void setOverflow(String overflow) {
        this.overflow = overflow;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getRight_team_bv() {
        return right_team_bv;
    }

    public void setRight_team_bv(String right_team_bv) {
        this.right_team_bv = right_team_bv;
    }




    public MatchIncomeModel(String member_id,String cf_left_bv,String cf_right_bv,String entered_date,String left_team_bv,String matching_business,String matching_incentive,String overflow,String payable_amount,String right_team_bv )
    {
        this.member_id=member_id;
        this.cf_left_bv=cf_left_bv;
        this.cf_right_bv=cf_right_bv;
        this.entered_date=entered_date;
        this.left_team_bv=left_team_bv;
        this.matching_business=matching_business;
        this.matching_incentive=matching_incentive;
        this.overflow=overflow;
        this.payable_amount=payable_amount;
        this.right_team_bv=right_team_bv;

    }


}
