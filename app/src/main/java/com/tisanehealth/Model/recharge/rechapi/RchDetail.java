
package com.tisanehealth.Model.recharge.rechapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RchDetail {

    @SerializedName("Instruction")
    @Expose
    private String instruction;
    @SerializedName("Operator_Code")
    @Expose
    private String operatorCode;
    @SerializedName("Service")
    @Expose
    private String service;
    @SerializedName("State")
    @Expose
    private String state;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
