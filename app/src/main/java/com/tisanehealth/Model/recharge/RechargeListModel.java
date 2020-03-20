package com.tisanehealth.Model.recharge;

import android.os.Parcel;
import android.os.Parcelable;

public class RechargeListModel implements Parcelable {
    String instruction;
    String operator_Code;
    String service;
    String state;


    protected RechargeListModel(Parcel in) {
        instruction = in.readString();
        operator_Code = in.readString();
        service = in.readString();
        state = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(instruction);
        dest.writeString(operator_Code);
        dest.writeString(service);
        dest.writeString(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RechargeListModel> CREATOR = new Creator<RechargeListModel>() {
        @Override
        public RechargeListModel createFromParcel(Parcel in) {
            return new RechargeListModel(in);
        }

        @Override
        public RechargeListModel[] newArray(int size) {
            return new RechargeListModel[size];
        }
    };

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        instruction = instruction;
    }

    public String getOperator_Code() {
        return operator_Code;
    }

    public void setOperator_Code(String operator_Code) {
        operator_Code = operator_Code;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        service = service;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        state = state;
    }


    public RechargeListModel(String instruction,String operator_Code,String service,String state)
    {
        this.instruction=instruction;
        this.operator_Code=operator_Code;
        this.service=service;
        this.state=state;

    }

}
