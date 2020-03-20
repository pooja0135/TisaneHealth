package com.tisanehealth.Model;

public class PinBankModel {

    public String getAssignTo() {
        return AssignTo;
    }

    public void setAssignTo(String assignTo) {
        AssignTo = assignTo;
    }

    public String getGenerateDate() {
        return GenerateDate;
    }

    public void setGenerateDate(String generateDate) {
        GenerateDate = generateDate;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getPinType() {
        return PinType;
    }

    public void setPinType(String pinType) {
        PinType = pinType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUsedDate() {
        return UsedDate;
    }

    public void setUsedDate(String usedDate) {
        UsedDate = usedDate;
    }

    public String getUsedName() {
        return UsedName;
    }

    public void setUsedName(String usedName) {
        UsedName = usedName;
    }

    String AssignTo,GenerateDate,Pin,PinType,Status,UsedDate,UsedName;






    public PinBankModel(String AssignTo, String GenerateDate, String Pin, String PinType, String Status, String UsedDate, String UsedName)
    {
        this.AssignTo=AssignTo;
        this.GenerateDate=GenerateDate;
        this.Pin=Pin;
        this.PinType=PinType;
        this.Status=Status;
        this.UsedDate=UsedDate;
        this.UsedName=UsedName;

    }


}
