package com.oneandonly.arboost.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oneandonly.arboost.R;
import com.oneandonly.arboost.adapters.RecyclerAdapter;
import com.oneandonly.arboost.models.CardModel;
import com.oneandonly.arboost.models.TransactionModel;
import com.oneandonly.arboost.models.UserModel;
import com.oneandonly.arboost.service.CardAPI;
import com.oneandonly.arboost.service.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private CardAPI transactionAPI;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private TextView worldPointText;
    private double totalWorldPoints = 0;

    private ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        Intent intent = getIntent();
        CardModel cardmodel = intent.getParcelableExtra("cardModel");
        LayoutInflater layoutInflater = getLayoutInflater();

        // Credit Card Home Screen with db values
        View creditCardHomeScreen = layoutInflater.inflate(R.layout.credit_card_home_screen, null);

        TextView creditCardDebt = creditCardHomeScreen.findViewById(R.id.credit_card_home_screen_current_debt);
        TextView creditCardCurrentLimit = creditCardHomeScreen.findViewById(R.id.credit_card_home_screen_card_limit);
        TextView creditCardTotalLimit = creditCardHomeScreen.findViewById(R.id.credit_card_home_screen_total_card_limit);

        //double creditCardDebt = cardmodel.getDebt();
        creditCardDebt.setText(String.valueOf(cardmodel.getDebt()));
        creditCardCurrentLimit.setText(String.valueOf(cardmodel.getAccountLimit()-cardmodel.getDebt()));
        creditCardTotalLimit.setText(String.valueOf(cardmodel.getAccountLimit()));

        // Credit Card Debt Payments with db values
        View creditCardDebtPaymentScreen = layoutInflater.inflate(R.layout.credit_card_debt_payments, null);

        //TextView currentDebt = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_home_screen_current_debt);
        TextView creditCardTotalDebt = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_total_debt);
        TextView creditCardLastPaymentDate = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_last_payment_date);
        TextView creditCardCutoffDate = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_cutoff_date);

        //currentDebt.setText(String.valueOf(cardmodel.getDebt()));
        creditCardTotalDebt.setText(String.valueOf(cardmodel.getDebt()));
        creditCardLastPaymentDate.setText(String.valueOf(cardmodel.getPaymentDueDate()));
        creditCardCutoffDate.setText(String.valueOf(cardmodel.getCutOffDate()));

        //Credit Card Card Details with db values
        View creditCardCardDetailsScreen = layoutInflater.inflate(R.layout.credit_card_details, null);

        View creditCardContactlessCircle = creditCardCardDetailsScreen.findViewById(R.id.credit_card_contactless_circle);
        View creditCardEcomCircle = creditCardCardDetailsScreen.findViewById(R.id.credit_card_ecom_circle);
        View creditCardMailOrderCircle = creditCardCardDetailsScreen.findViewById(R.id.credit_card_mail_order_circle);
        View creditCardAutomatedCircle = creditCardCardDetailsScreen.findViewById(R.id.credit_card_automated_circle);
        View creditCardCurrencyCircle = creditCardCardDetailsScreen.findViewById(R.id.credit_card_currency_circle);
        View creditCardAccountSumCircle = creditCardCardDetailsScreen.findViewById(R.id.credit_card_account_sum_circle);
        TextView creditCardEmail = creditCardCardDetailsScreen.findViewById(R.id.credit_card_card_details_screen_email);
        TextView creditCardExpiryDate = creditCardCardDetailsScreen.findViewById(R.id.credit_card_card_details_screen_expiry_date);


        if(cardmodel.isContactless() == true) {
            creditCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            creditCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if(cardmodel.isEcom() == true) {
            creditCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            creditCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if(cardmodel.isMailOrder() == true) {
            creditCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            creditCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        /*
        if(cardmodel.isAutomated() == true) {
            creditCardAutomatedCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            creditCardAutomatedCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }if(cardmodel.isCurrency() == true) {
            creditCardCurrencyCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            creditCardCurrencyCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }if(cardmodel.isAccounSum() == true) {
            creditCardAccountSumCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            creditCardAccountSumCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        */
        creditCardEmail.setText(String.valueOf(cardmodel.geteAccountStatement()));
        creditCardExpiryDate.setText(String.valueOf(cardmodel.getExpireDate()));

        //Debit Card Home Screen with db values
        View debitCardHomeScreen = layoutInflater.inflate(R.layout.debit_card_home_screen, null);

        TextView debitCardBalance = debitCardHomeScreen.findViewById(R.id.debit_card_home_screen_balance);
        TextView debitCardAccountNumber = debitCardHomeScreen.findViewById(R.id.debit_card_home_screen_account_number);
        TextView debitCardFlexibleAccountLimit = debitCardHomeScreen.findViewById(R.id.debit_card_home_screen_flexible_account_limit);

        debitCardBalance.setText(String.valueOf(cardmodel.getBalance()));
        debitCardAccountNumber.setText(String.valueOf(cardmodel.getAccountNumber()));
        debitCardFlexibleAccountLimit.setText(String.valueOf(cardmodel.getFlexibleAccountLimit()));

        //Debit Card Card Details with db values
        View debitCardCardDetailsScreen = layoutInflater.inflate(R.layout.debit_card_details, null);

        View debitCardContactlessCircle = debitCardCardDetailsScreen.findViewById(R.id.debit_card_card_details_contactless_circle);
        View debitCardEcomCircle = debitCardCardDetailsScreen.findViewById(R.id.debit_card_card_details_ecom_circle);
        View debitCardMailOrderCircle = debitCardCardDetailsScreen.findViewById(R.id.debit_card_card_details_account_sum_circle);
        TextView debitCardEmail = debitCardCardDetailsScreen.findViewById(R.id.debit_card_card_details_screen_email);
        TextView debitCardExpiryDate = debitCardCardDetailsScreen.findViewById(R.id.debit_card_card_details_expiry_date);

        if(cardmodel.isContactless() == true) {
            debitCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            debitCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if(cardmodel.isEcom() == true) {
            debitCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            debitCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if(cardmodel.isMailOrder() == true) {
            debitCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            debitCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        debitCardEmail.setText(String.valueOf(cardmodel.geteAccountStatement()));
        debitCardExpiryDate.setText(String.valueOf(cardmodel.getExpireDate()));


        //Prepaid Card Home Screen with db values
        View prepaidCardHomeScreen = layoutInflater.inflate(R.layout.prepaid_card_home_screen, null);

        TextView prepaidCardBalance = prepaidCardHomeScreen.findViewById(R.id.prepaid_card_home_screen_balance);
        TextView prepaidCardExpiryDateHome = prepaidCardHomeScreen.findViewById(R.id.prepaid_card_home_screen_expiry_date);

        prepaidCardBalance.setText(String.valueOf(cardmodel.getBalance()));
        prepaidCardExpiryDateHome.setText(String.valueOf(cardmodel.getExpireDate()));

        //Prepaid Card Details Screen with db values
        View prepaidCardDetailsScreen = layoutInflater.inflate(R.layout.prepaid_card_details, null);

        View prepaidCardContactlessCircle = prepaidCardDetailsScreen.findViewById(R.id.prepaid_card_card_details_contactless_circle);
        View prepaidCardEcomCircle = prepaidCardDetailsScreen.findViewById(R.id.prepaid_card_card_details_ecom_circle);
        View prepaidCardMailOrderCircle = prepaidCardDetailsScreen.findViewById(R.id.prepaid_card_card_details_account_sum_circle);
        TextView prepaidCardEmail = prepaidCardDetailsScreen.findViewById(R.id.prepaid_card_card_details_screen_email);
        TextView prepaidCardExpiryDateDetails = prepaidCardDetailsScreen.findViewById(R.id.prepaid_card_card_details_expiry_date);

        if(cardmodel.isContactless() == true) {
            prepaidCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            prepaidCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if(cardmodel.isEcom() == true) {
            prepaidCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            prepaidCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if(cardmodel.isMailOrder() == true) {
            prepaidCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        }else{
            prepaidCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        prepaidCardEmail.setText(String.valueOf(cardmodel.geteAccountStatement()));
        prepaidCardExpiryDateDetails.setText(String.valueOf(cardmodel.getExpireDate()));


        transactionAPI = RetrofitClient.getInstances().getCardAPI();
        //ArrayList<TransactionModel> transactionModelArrayList =
        makeTransactionCall("4943141334422544");


        //Credit Card Transaction Screen with db values
        View creditCardTransactionsScreen = layoutInflater.inflate(R.layout.credit_card_transactions_demo, null);
        worldPointText = creditCardTransactionsScreen.findViewById(R.id.credit_card_transactions_world_points);

        recyclerView = creditCardTransactionsScreen.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(transactionModelArrayList, this);
        recyclerView.setAdapter(recyclerAdapter);

        //Debit Card Transaction Screen with db values
        View debitCardTransactionsScreen = layoutInflater.inflate(R.layout.debit_card_transactions_demo, null);
        worldPointText = debitCardTransactionsScreen.findViewById(R.id.debit_card_transactions_world_points);

        recyclerView = debitCardTransactionsScreen.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(transactionModelArrayList, this);
        recyclerView.setAdapter(recyclerAdapter);

        //Prepaid Card Transaction Screen with db values
        View prepaidCardTransactionsScreen = layoutInflater.inflate(R.layout.prepaid_card_transactions_demo, null);
        worldPointText = prepaidCardTransactionsScreen.findViewById(R.id.prepaid_card_transactions_world_points);

        recyclerView = prepaidCardTransactionsScreen.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(transactionModelArrayList, this);
        recyclerView.setAdapter(recyclerAdapter);





        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            placeTextView(hitResult.createAnchor(), prepaidCardTransactionsScreen);
        });

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

    private void makeTransactionCall(String cardNumber) {
        Call<JsonArray> call = transactionAPI.getTransaction(cardNumber);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        JsonArray body = response.body();
                        //System.out.println(body);

                        for(int i = 0; i< body.size();i++){
                            JsonObject transactionObject = (JsonObject) body.get(i);
                            double totalAmount = transactionObject.get("total_amount").getAsDouble();
                            double worldPoint = transactionObject.get("world_point").getAsDouble();

                            String store = transactionObject.get("store").getAsString().split("T")[0];
                            String sector = transactionObject.get("sector").getAsString().split("T")[0];
                            String date = transactionObject.get("date").getAsString().split("T")[0];

                            totalWorldPoints += worldPoint;
                            TransactionModel transactionModel = new TransactionModel(store,sector,date,totalAmount,worldPoint);

                            transactionModelArrayList.add(transactionModel);
                            recyclerAdapter.notifyDataSetChanged();
                            if(transactionModelArrayList.size() == body.size()){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        worldPointText.setText(String.valueOf(totalWorldPoints));
                                    }
                                });
                            }
                        }
                        System.out.println(totalWorldPoints);

                        //worldPointText.setText(String.valueOf(totalWorldPoints));


//                        Intent intent = new Intent(ArActivity.this, ArActivity.class);
//                        intent.putExtra("transactionModelArrayList", transactionModelArrayList);
//                        startActivity(intent);
                    }
                    else{
                        System.out.println("Body NULL!!");
                    }
                }
                else{
                    System.out.println(response.code());
                    if (response.code() == 400) {
                        try {
                            String errorText = response.errorBody().string();
                            new AlertDialog.Builder(ArActivity.this)
                                    .setTitle("Hata")
                                    .setMessage(errorText)
                                    .setPositiveButton("Tamam", null)
                                    .show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                System.out.println("Error!!");
                System.out.println(t.getMessage());
            }

        });
    }


//    private void printCardTransaction(ArrayList<TransactionModel> transactionModelArrayList) {
//        System.out.println(transactionModelArrayList.size());
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void placeTextView(Anchor anchor, View view) {
        ViewRenderable.builder()
                .setView(this, view)
                .build()
                .thenAccept(viewRenderable -> {
                    placeModel(viewRenderable, anchor);
                });
    }

    private void placeModel(ViewRenderable viewRenderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
//        node.getScaleController().setMaxScale(0.2f);
//        node.getScaleController().setMinScale(0.01f);

        node.setParent(anchorNode);
        node.setRenderable(viewRenderable);
        node.select();

    }
}