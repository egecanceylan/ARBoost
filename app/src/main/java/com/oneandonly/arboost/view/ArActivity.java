package com.oneandonly.arboost.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.oneandonly.arboost.R;
import com.oneandonly.arboost.models.CardModel;

public class ArActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

//        Intent intent = getIntent();
//        CardModel cardmodel = intent.getParcelableExtra("cardModel");
//        System.out.println(cardmodel.getCardNumber());
//        System.out.println(cardmodel.getAccountLimit());
//        System.out.println(cardmodel.getCutOffDate());
//        System.out.println(cardmodel.getExpireDate());
//        System.out.println(cardmodel.getDebt());
//        System.out.println(cardmodel.getPaymentDueDate());
//        System.out.println(cardmodel.geteAccountStatement());
//        System.out.println(cardmodel.getUser().getId());
//        System.out.println(cardmodel.getUser().getName());
//        System.out.println(cardmodel.getUser().getSurname());

    }
}