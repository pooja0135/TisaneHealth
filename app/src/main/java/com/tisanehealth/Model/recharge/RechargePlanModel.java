package com.tisanehealth.Model.recharge;

public class RechargePlanModel {
    String amount;
    String detail;
    String validity;
    String talktime;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }

    public RechargePlanModel(String amount, String detail, String validity, String talktime)
    {
        this.amount=amount;
        this.detail=detail;
        this.validity=validity;
        this.talktime=talktime;

    }

}
