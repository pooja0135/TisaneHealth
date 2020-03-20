package com.tisanehealth.Model.recharge;

public class CircleModel {

    String circle_code,state_name;
    public String getCircle_code() {
        return circle_code;
    }

    public void setCircle_code(String circle_code) {
        this.circle_code = circle_code;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }


    public CircleModel(String circle_code,String state_name)
    {
        this.circle_code=circle_code;
        this.state_name=state_name;
    }

}
