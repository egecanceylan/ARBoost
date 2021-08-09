package com.oneandonly.arboost.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CardModel implements Parcelable {
    private String cardNumber, cutOffDate, paymentDueDate, expireDate, eAccountStatement;
    private UserModel user;
    private double accountLimit, debt;
    private boolean isContactless, isEcom, mailOrder;

    public CardModel(String cardNumber, String cutOffDate,
                     String paymentDueDate, String expireDate, String eAccountStatement,
                     UserModel user, double accountLimit, double debt, boolean isContactless, boolean isEcom, boolean mailOrder) {
        this.cardNumber = cardNumber;
        this.cutOffDate = cutOffDate;
        this.paymentDueDate = paymentDueDate;
        this.expireDate = expireDate;
        this.eAccountStatement = eAccountStatement;
        this.user = user;
        this.accountLimit = accountLimit;
        this.debt = debt;
        this.isContactless = isContactless;
        this.isEcom = isEcom;
        this.mailOrder = mailOrder;
    }

    protected CardModel(Parcel in) {
        cardNumber = in.readString();
        cutOffDate = in.readString();
        paymentDueDate = in.readString();
        expireDate = in.readString();
        eAccountStatement = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
        accountLimit = in.readDouble();
        debt = in.readDouble();
        isContactless = in.readByte() != 0;
        isEcom = in.readByte() != 0;
        mailOrder = in.readByte() != 0;
    }

    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel in) {
            return new CardModel(in);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String geteAccountStatement() {
        return eAccountStatement;
    }

    public void seteAccountStatement(String eAccountStatement) {
        this.eAccountStatement = eAccountStatement;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public double getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(double accountLimit) {
        this.accountLimit = accountLimit;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public boolean isContactless() {
        return isContactless;
    }

    public void setContactless(boolean contactless) {
        isContactless = contactless;
    }

    public boolean isEcom() {
        return isEcom;
    }

    public void setEcom(boolean ecom) {
        isEcom = ecom;
    }

    public boolean isMailOrder() {
        return mailOrder;
    }

    public void setMailOrder(boolean mailOrder) {
        this.mailOrder = mailOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cardNumber);
        parcel.writeString(cutOffDate);
        parcel.writeString(paymentDueDate);
        parcel.writeString(expireDate);
        parcel.writeString(eAccountStatement);
        parcel.writeParcelable(user, i);
        parcel.writeDouble(accountLimit);
        parcel.writeDouble(debt);
        parcel.writeByte((byte) (isContactless ? 1 : 0));
        parcel.writeByte((byte) (isEcom ? 1 : 0));
        parcel.writeByte((byte) (mailOrder ? 1 : 0));
    }
}
