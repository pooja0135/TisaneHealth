package com.tisanehealth.Model;

public class TeamModelClass {

    String memberid;
    String sponsorid;
    String name;
    String package_value;
    String registrationDate;
    String activeDate;
    String status;

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }


    public String getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        this.sponsorid = sponsorid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackage_value() {
        return package_value;
    }

    public void setPackage_value(String package_value) {
        this.package_value = package_value;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public TeamModelClass(String memberid,String name,String package_value,String registrationDate,String activeDate,String status,String sponsorid)
    {
        this.memberid=memberid;
        this.name=name;
        this.package_value=package_value;
        this.registrationDate=registrationDate;
        this.activeDate=activeDate;
        this.status=status;
        this.sponsorid=sponsorid;
    }

}
