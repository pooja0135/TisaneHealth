package com.tisanehealth.Model.recharge;


import android.os.Parcel;
import android.os.Parcelable;

public  class DTHModel implements Parcelable {

    int image;
    String name;
    String id;


    protected DTHModel(Parcel in) {
        image = in.readInt();
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<DTHModel> CREATOR = new Creator<DTHModel>() {
        @Override
        public DTHModel createFromParcel(Parcel in) {
            return new DTHModel(in);
        }

        @Override
        public DTHModel[] newArray(int size) {
            return new DTHModel[size];
        }
    };

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

    public DTHModel(int image, String name,String id)
    {
        this.image=image;
        this.name=name;
        this.id=id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(image);
        parcel.writeString(name);
        parcel.writeString(id);
    }




}
