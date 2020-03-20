package com.tisanehealth.Model.payout;

public class LeaderIncomeModel {

    String LeftTeamMBP;
    String MemberId;
    String Name;
    String PaidDate;
    String PayableAmount;
    String RightTeamMBP;



    public String getLeftTeamMBP() {
        return LeftTeamMBP;
    }

    public void setLeftTeamMBP(String leftTeamMBP) {
        LeftTeamMBP = leftTeamMBP;
    }

    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPaidDate() {
        return PaidDate;
    }

    public void setPaidDate(String paidDate) {
        PaidDate = paidDate;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        PayableAmount = payableAmount;
    }

    public String getRightTeamMBP() {
        return RightTeamMBP;
    }

    public void setRightTeamMBP(String rightTeamMBP) {
        RightTeamMBP = rightTeamMBP;
    }



    public LeaderIncomeModel(String LeftTeamMBP,String MemberId,String Name,String PaidDate,String PayableAmount,String RightTeamMBP)
    {
        this.LeftTeamMBP=LeftTeamMBP;
        this.MemberId=MemberId;
        this.Name=Name;
        this.PaidDate=PaidDate;
        this.PayableAmount=PayableAmount;
        this.RightTeamMBP=RightTeamMBP;



    }

}
