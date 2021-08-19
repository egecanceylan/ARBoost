package com.oneandonly.arboost.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionModel implements Parcelable {
    private String store,sector,date;
    private double totalAmount, worldPoint;


    public TransactionModel(String store, String sector, String date, double totalAmount, double worldPoint) {
        this.store = store;
        this.sector = sector;
        this.date = date;
        this.totalAmount = totalAmount;
        this.worldPoint = worldPoint;
    }

    protected TransactionModel(Parcel in) {
        store = in.readString();
        sector = in.readString();
        date = in.readString();
        totalAmount = in.readDouble();
        worldPoint = in.readDouble();
    }

    //added later. idk if it is important
    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getWorldPoint() {
        return worldPoint;
    }

    public void setWorldPoint(double worldPoint) {
        this.worldPoint = worldPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(store);
        dest.writeString(sector);
        dest.writeString(date);
        dest.writeDouble(totalAmount);
        dest.writeDouble(worldPoint);
    }
}
