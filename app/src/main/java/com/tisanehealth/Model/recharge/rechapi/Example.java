
package com.tisanehealth.Model.recharge.rechapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("Recharge")
    @Expose
    private List<Recharge> recharge = null;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public List<Recharge> getRecharge() {
        return recharge;
    }

    public void setRecharge(List<Recharge> recharge) {
        this.recharge = recharge;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
