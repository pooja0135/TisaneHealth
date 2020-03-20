package com.tisanehealth.Model.recharge;

public class LandlineModel {

    int image;
    String name;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LandlineModel(int image, String name, String id)
    {
        this.image=image;
        this.name=name;
        this.id=id;
    }

}
