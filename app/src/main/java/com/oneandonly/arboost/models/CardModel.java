package com.oneandonly.arboost.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CardModel implements Parcelable {
    private String cardNumber, cutOffDate, paymentDueDate, expireDate, eAccountStatement, accountNumber, type;
    private UserModel user;
    private double accountLimit, currentDebt,totalDebt, balance, flexibleAccountLimit;
    private boolean isContactless, isEcom, mailOrder, isAutomated, isCurrency;

    public CardModel(String cardNumber, String cutOffDate, String paymentDueDate, String expireDate, String eAccountStatement, String accountNumber,
                     String type, UserModel user, double accountLimit, double currentDebt, double totalDebt,
                     double balance, double flexibleAccountLimit, boolean isContactless,
                     boolean isEcom, boolean mailOrder, boolean isAutomated, boolean isCurrency) {
        this.cardNumber = cardNumber;
        this.cutOffDate = cutOffDate;
        this.paymentDueDate = paymentDueDate;
        this.expireDate = expireDate;
        this.eAccountStatement = eAccountStatement;
        this.accountNumber = accountNumber;
        this.type = type;
        this.user = user;
        this.accountLimit = accountLimit;
        this.currentDebt = currentDebt;
        this.totalDebt = totalDebt;
        this.balance = balance;
        this.flexibleAccountLimit = flexibleAccountLimit;
        this.isContactless = isContactless;
        this.isEcom = isEcom;
        this.mailOrder = mailOrder;
        this.isAutomated = isAutomated;
        this.isCurrency = isCurrency;
    }

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(double debt) {
        this.currentDebt = currentDebt;
    }

    public double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(double debt) {
        this.totalDebt = totalDebt;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFlexibleAccountLimit() {
        return flexibleAccountLimit;
    }

    public void setFlexibleAccountLimit(double flexibleAccountLimit) {
        this.flexibleAccountLimit = flexibleAccountLimit;
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

    public boolean isAutomated() {
        return isAutomated;
    }

    public void setAutomated(boolean isAutomated) {
        this.isAutomated = isAutomated;
    }

    public boolean isCurrency() {
        return isCurrency;
    }

    public void setCurrency(boolean isCurrency) {
        this.isCurrency = isCurrency;
    }

    public static Creator<CardModel> getCREATOR() {
        return CREATOR;
    }

    protected CardModel(Parcel in) {
        cardNumber = in.readString();
        cutOffDate = in.readString();
        paymentDueDate = in.readString();
        expireDate = in.readString();
        eAccountStatement = in.readString();
        accountNumber = in.readString();
        type = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
        accountLimit = in.readDouble();
        currentDebt = in.readDouble();
        totalDebt = in.readDouble();
        balance = in.readDouble();
        flexibleAccountLimit = in.readDouble();
        isContactless = in.readByte() != 0;
        isEcom = in.readByte() != 0;
        mailOrder = in.readByte() != 0;
        isAutomated = in.readByte() != 0;
        isCurrency = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardNumber);
        dest.writeString(cutOffDate);
        dest.writeString(paymentDueDate);
        dest.writeString(expireDate);
        dest.writeString(eAccountStatement);
        dest.writeString(accountNumber);
        dest.writeString(type);
        dest.writeParcelable(user, flags);
        dest.writeDouble(accountLimit);
        dest.writeDouble(currentDebt);
        dest.writeDouble(totalDebt);
        dest.writeDouble(balance);
        dest.writeDouble(flexibleAccountLimit);
        dest.writeByte((byte) (isContactless ? 1 : 0));
        dest.writeByte((byte) (isEcom ? 1 : 0));
        dest.writeByte((byte) (mailOrder ? 1 : 0));
        dest.writeByte((byte) (isAutomated ? 1 : 0));
        dest.writeByte((byte) (isCurrency ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
}
