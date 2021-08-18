package com.oneandonly.arboost.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionModel implements Parcelable {
    private String store,sector,date,cardNumber;
    private double totalAmount, worldPoint;


    public TransactionModel(String store, String sector, String date, String cardNumber, double totalAmount, double worldPoint) {
        this.store = store;
        this.sector = sector;
        this.date = date;
        this.cardNumber = cardNumber;
        this.totalAmount = totalAmount;
        this.worldPoint = worldPoint;
    }

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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    }
}
