
package com.tisanehealth.Model.recharge.rechapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recharge {

    @SerializedName("RchDetail")
    @Expose
    private List<RchDetail> rchDetail = null;
    @SerializedName("Recharge_type")
    @Expose
    private String rechargeType;

    public List<RchDetail> getRchDetail() {
        return rchDetail;
    }

    public void setRchDetail(List<RchDetail> rchDetail) {
        this.rchDetail = rchDetail;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

}
