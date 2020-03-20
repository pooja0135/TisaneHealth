package com.tisanehealth.Model.payout;

public class RoiIncomeHistoryModel {
    String member_id,paid_amount,paid_month,payment_month,payment_per_month,generate_date;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getPaid_month() {
        return paid_month;
    }

    public void setPaid_month(String paid_month) {
        this.paid_month = paid_month;
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

    public String getGenerate_date() {
        return generate_date;
    }

    public void setGenerate_date(String generate_date) {
        this.generate_date = generate_date;
    }




    public RoiIncomeHistoryModel(String member_id, String paid_amount, String paid_month, String payment_month,String payment_per_month, String generate_date)
    {
        this.member_id=member_id;
        this.paid_amount=paid_amount;
        this.paid_month=paid_month;
        this.payment_month=payment_month;
        this.generate_date=generate_date;
        this.payment_per_month=payment_per_month;

    }


}
