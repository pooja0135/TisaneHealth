package com.tisanehealth.Model.payout;

public class DirectIncomeModel {
    String member_id;
    String name;

    public String getDirect_business() {
        return direct_business;
    }

    public void setDirect_business(String direct_business) {
        this.direct_business = direct_business;
    }

    public String getDirect_income() {
        return direct_income;
    }

    public void setDirect_income(String direct_income) {
        this.direct_income = direct_income;
    }

    String direct_business;
    String direct_income;
    String generate_date;

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



    public String getGenerate_date() {
        return generate_date;
    }

    public void setGenerate_date(String generate_date) {
        this.generate_date = generate_date;
    }

    public DirectIncomeModel(String member_id, String name, String direct_business, String direct_income, String generate_date)
    {
        this.member_id=member_id;
        this.name=name;
        this.direct_business=direct_business;
        this.direct_income=direct_income;
        this.generate_date=generate_date;

    }


}
