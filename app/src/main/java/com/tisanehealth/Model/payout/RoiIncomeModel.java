package com.tisanehealth.Model.payout;

public class RoiIncomeModel {

    String member_id,payment_month,payment_per_month,paid_month,paid_amount,entered_date;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPayment_month() {
        return payment_month;
    }

    public void setPayment_month(String payment_month) {
        this.payment_month = payment_month;
    }

    public String getPayment_per_month() {
        return payment_per_month;
    }

    public void setPayment_per_month(String payment_per_month) {
        this.payment_per_month = payment_per_month;
    }

    public String getPaid_month() {
        return paid_month;
    }

    public void setPaid_month(String paid_month) {
        this.paid_month = paid_month;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getEntered_date() {
        return entered_date;
    }

    public void setEntered_date(String entered_date) {
        this.entered_date = entered_date;
    }


    public RoiIncomeModel(String member_id, String payment_month, String payment_per_month, String paid_month, String paid_amount,String entered_date)
    {
        this.member_id=member_id;
        this.payment_month=payment_month;
        this.payment_per_month=payment_per_month;
        this.paid_month=paid_month;
        this.paid_amount=paid_amount;
        this.entered_date=entered_date;

    }


}
