package com.tisanehealth.Model.recharge;

public class OperatorModel {
    int image;
    String operator_name;
    String operator_id;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }



    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }



    public OperatorModel(int image,String operator_name,String operator_id)
    {
        this.image=image;
        this.operator_name=operator_name;
        this.operator_id=operator_id;
    }
}
