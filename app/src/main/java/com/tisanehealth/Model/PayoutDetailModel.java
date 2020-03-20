package com.tisanehealth.Model;

public class PayoutDetailModel {
    String id;
    String business_value;
    String date;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusiness_value() {
        return business_value;
    }

    public void setBusiness_value(String business_value) {
        this.business_value = business_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PayoutDetailModel(String id,String business_value,String date)
    {
        this.business_value=business_value;
        this.date=date;
        this.id=id;

    }

}
