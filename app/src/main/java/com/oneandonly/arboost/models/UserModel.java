package com.oneandonly.arboost.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private int id;
    private String name, surname;

    public UserModel(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(surname);
    }
}
