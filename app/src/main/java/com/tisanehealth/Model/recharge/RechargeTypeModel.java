package com.tisanehealth.Model.recharge;

import java.util.ArrayList;

public class RechargeTypeModel {

    String Recharge_type;
    ArrayList<RechargeListModel> rechargelist;

    public String getRecharge_type() {
        return Recharge_type;
    }

    public void setRecharge_type(String recharge_type) {
        Recharge_type = recharge_type;
    }

    public ArrayList<RechargeListModel> getRechargelist() {
        return rechargelist;
    }

    public void setRechargelist(ArrayList<RechargeListModel> rechargelist) {
        this.rechargelist = rechargelist;
    }

    public RechargeTypeModel(String Recharge_type, ArrayList<RechargeListModel> rechargelist)
    {
        this.Recharge_type=Recharge_type;
        this.rechargelist=rechargelist;

    }

}
