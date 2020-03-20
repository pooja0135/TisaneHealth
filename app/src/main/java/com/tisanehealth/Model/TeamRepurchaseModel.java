package com.tisanehealth.Model;

public class TeamRepurchaseModel {

    String Amount;
    String CGSTAmt;
    String CenterId;
    String Date;
    String GrossAmt;
    String InvoiceNo;
    String SGSTAmt;
    String TotalBV;
    String MemberId;
    String MemberName;


    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }



    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCGSTAmt() {
        return CGSTAmt;
    }

    public void setCGSTAmt(String CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
    }

    public String getCenterId() {
        return CenterId;
    }

    public void setCenterId(String centerId) {
        CenterId = centerId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGrossAmt() {
        return GrossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        GrossAmt = grossAmt;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getSGSTAmt() {
        return SGSTAmt;
    }

    public void setSGSTAmt(String SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
    }

    public String getTotalBV() {
        return TotalBV;
    }

    public void setTotalBV(String totalBV) {
        TotalBV = totalBV;
    }

    public TeamRepurchaseModel(String Amount, String CGSTAmt, String CenterId, String Date, String GrossAmt, String InvoiceNo, String SGSTAmt, String TotalBV, String MemberId, String MemberName)
    {
        this.Amount=Amount;
        this.CGSTAmt=CGSTAmt;
        this.CenterId=CenterId;
        this.Date=Date;
        this.GrossAmt=GrossAmt;
        this.InvoiceNo=InvoiceNo;
        this.SGSTAmt=SGSTAmt;
        this.TotalBV=TotalBV;
        this.MemberId=MemberId;
        this.MemberName=MemberName;

    }

}
