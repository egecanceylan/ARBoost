package com.oneandonly.arboost.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.oneandonly.arboost.R;
import com.oneandonly.arboost.adapters.RecyclerAdapter;
import com.oneandonly.arboost.models.CardImage;
import com.oneandonly.arboost.models.CardModel;
import com.oneandonly.arboost.models.TransactionModel;
import com.oneandonly.arboost.service.CardAPI;
import com.oneandonly.arboost.service.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private CardAPI transactionAPI;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private TextView worldPointTextCredit;
    private TextView worldPointTextDebit;
    private TextView worldPointTextPrepaid;
    private double totalWorldPoints = 0;
    private List<Anchor> anchorList;
    private boolean isAdded = false;
    private float cardX = 0f, cardY = 0f, cardZ = 0f, time;
    private boolean isTop = false, isBottom = false, isRight = false, isCenter = false;

    private ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        Intent intent = getIntent();
        CardModel cardmodel = intent.getParcelableExtra("cardModel");
        LayoutInflater layoutInflater = getLayoutInflater();

        anchorList = new ArrayList<>();

        // Credit Card Home Screen with db values
        View creditCardHomeScreen = layoutInflater.inflate(R.layout.credit_card_home_screen, null);

        TextView creditCardDebt = creditCardHomeScreen.findViewById(R.id.credit_card_home_screen_current_debt);
        TextView creditCardCurrentLimit = creditCardHomeScreen.findViewById(R.id.credit_card_home_screen_card_limit);
        TextView creditCardTotalLimit = creditCardHomeScreen.findViewById(R.id.credit_card_home_screen_total_card_limit);

        //only last two numbers after decimal has shown, like 2355.98
        double current = cardmodel.getAccountLimit() - cardmodel.getCurrentDebt();
        double roundedCurrent = Math.round(current * 100.0) / 100.0;

        creditCardDebt.setText(String.valueOf(cardmodel.getCurrentDebt()));
        creditCardCurrentLimit.setText(String.valueOf(roundedCurrent));
        creditCardTotalLimit.setText(String.valueOf(cardmodel.getAccountLimit()));

        // Credit Card Debt Payments with db values
        View creditCardDebtPaymentScreen = layoutInflater.inflate(R.layout.credit_card_debt_payments, null);

        TextView creditCardCurrentDebt = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_current_debt);
        TextView creditCardTotalDebt = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_total_debt);
        TextView creditCardLastPaymentDate = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_last_payment_date);
        TextView creditCardCutoffDate = creditCardDebtPaymentScreen.findViewById(R.id.credit_card_debt_payments_screen_cutoff_date);

        creditCardCurrentDebt.setText(String.valueOf(cardmodel.getCurrentDebt()));
        creditCardTotalDebt.setText(String.valueOf(cardmodel.getTotalDebt()));
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

        creditCardExpiryDate.setText(String.valueOf(cardmodel.getExpireDate()));

        if (cardmodel.isContactless() == true) {
            creditCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            creditCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isEcom() == true) {
            creditCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            creditCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isMailOrder() == true) {
            creditCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            creditCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }

        if (cardmodel.isAutomated() == true) {
            creditCardAutomatedCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            creditCardAutomatedCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isCurrency() == true) {
            creditCardCurrencyCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            creditCardCurrencyCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.geteAccountStatement() != null) {
            creditCardAccountSumCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
            creditCardEmail.setText(String.valueOf(cardmodel.geteAccountStatement()));
        } else {
            creditCardAccountSumCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }

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

        debitCardExpiryDate.setText(String.valueOf(cardmodel.getExpireDate()));

        if (cardmodel.isContactless() == true) {
            debitCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            debitCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isEcom() == true) {
            debitCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            debitCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isMailOrder() == true) {
            debitCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            debitCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.geteAccountStatement() != null) {
            debitCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
            debitCardEmail.setText(String.valueOf(cardmodel.geteAccountStatement()));
        } else {
            creditCardAccountSumCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }

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

        prepaidCardExpiryDateDetails.setText(String.valueOf(cardmodel.getExpireDate()));

        if (cardmodel.isContactless() == true) {
            prepaidCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            prepaidCardContactlessCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isEcom() == true) {
            prepaidCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            prepaidCardEcomCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.isMailOrder() == true) {
            prepaidCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
        } else {
            prepaidCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }
        if (cardmodel.geteAccountStatement() != null) {
            prepaidCardMailOrderCircle.setBackground(getResources().getDrawable(R.drawable.green_circle));
            prepaidCardEmail.setText(String.valueOf(cardmodel.geteAccountStatement()));
        } else {
            creditCardAccountSumCircle.setBackground(getResources().getDrawable(R.drawable.white_circle));
        }

        //API call for transaction tables
        transactionAPI = RetrofitClient.getInstances().getCardAPI();
        makeTransactionCall("4943141334422544");

        //Credit Card Transaction Screen with db values
        View creditCardTransactionsScreen = layoutInflater.inflate(R.layout.credit_card_transactions_demo, null);
        worldPointTextCredit = creditCardTransactionsScreen.findViewById(R.id.credit_card_transactions_world_points);

        recyclerView = creditCardTransactionsScreen.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(transactionModelArrayList, this);
        recyclerView.setAdapter(recyclerAdapter);

        //Debit Card Transaction Screen with db values
        View debitCardTransactionsScreen = layoutInflater.inflate(R.layout.debit_card_transactions_demo, null);
        worldPointTextDebit = debitCardTransactionsScreen.findViewById(R.id.debit_card_transactions_world_points);

        recyclerView = debitCardTransactionsScreen.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(transactionModelArrayList, this);
        recyclerView.setAdapter(recyclerAdapter);

        //Prepaid Card Transaction Screen with db values
        View prepaidCardTransactionsScreen = layoutInflater.inflate(R.layout.prepaid_card_transactions_demo, null);
        worldPointTextPrepaid = prepaidCardTransactionsScreen.findViewById(R.id.prepaid_card_transactions_world_points);

        recyclerView = prepaidCardTransactionsScreen.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(transactionModelArrayList, this);
        recyclerView.setAdapter(recyclerAdapter);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
        arFragment.getArSceneView().getPlaneRenderer().setVisible(false);
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        arFragment.getArSceneView().getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                Frame frame = arFragment.getArSceneView().getArFrame();
                if (frame != null && frameTime.getStartTime(TimeUnit.MILLISECONDS) > time + 2000) {
                    Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
                    for (AugmentedImage augmentedImage : augmentedImages) {
                        if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                            Pose centerPose = augmentedImage.getCenterPose();
                            time = frameTime.getStartTime(TimeUnit.MILLISECONDS);
                            if (augmentedImage.getName().equals("card") && !isAdded) {
                                cardX = centerPose.tx();
                                cardY = centerPose.ty();
                                cardZ = centerPose.tz();
                                placeTextView(creditCardHomeScreen, augmentedImage.createAnchor(Pose.makeTranslation(centerPose.tx() + 0.1f, centerPose.ty(), centerPose.tz())));
                                isAdded = true;
                                System.out.println("catched");
                            } else if (augmentedImage.getName().equals("card") && centerPose.tz() < cardZ - 0.05f && !isTop) {
                                System.out.println("top");
                                placeTextView(creditCardCardDetailsScreen, augmentedImage.createAnchor(Pose.makeTranslation(centerPose.tx() + 0.1f, centerPose.ty() - 0.05f, centerPose.tz())));
                                isTop = true;
                                isBottom = false;
                                isRight = false;
                                isCenter = false;
                            } else if (augmentedImage.getName().equals("card") && centerPose.tz() > cardZ + 0.05f && !isBottom) {
                                System.out.println("bottom");
                                placeTextView(creditCardTransactionsScreen, augmentedImage.createAnchor(Pose.makeTranslation(centerPose.tx() + 0.1f, centerPose.ty() - 0.15f, centerPose.tz())));
                                isBottom = true;
                                isTop = false;
                                isRight = false;
                                isCenter = false;
                            } else if (augmentedImage.getName().equals("card") && centerPose.tx() > cardX + 0.05f && !isRight) {
                                System.out.println("right");
                                placeTextView(creditCardDebtPaymentScreen, augmentedImage.createAnchor(Pose.makeTranslation(centerPose.tx() + 0.1f, centerPose.ty(), centerPose.tz())));
                                isRight = true;
                                isBottom = false;
                                isTop = false;
                                isCenter = false;
                            } else if (augmentedImage.getName().equals("card") && ((centerPose.tz() > cardZ - 0.025f || centerPose.tz() < cardZ + 0.025f)
                                    && (centerPose.tx() > cardX - 0.025f || centerPose.tx() < cardX + 0.025f))
                                    && !isCenter && (isTop || isBottom || isRight)) {
                                System.out.println("center");
                                placeTextView(creditCardHomeScreen, augmentedImage.createAnchor(Pose.makeTranslation(centerPose.tx() + 0.1f, centerPose.ty(), centerPose.tz())));
                                isCenter = true;
                                isTop = false;
                                isBottom = false;
                                isRight = false;
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean setupAugmentedImagesDB(Config config, Session session) {
        AugmentedImageDatabase augmentedImageDatabase;
        CardImage cardImageModel = CardImage.getInstance();
        Bitmap bitmap = cardImageModel.getBitmap();
        if (bitmap == null) {
            return false;
        }

        augmentedImageDatabase = new AugmentedImageDatabase(session);
        augmentedImageDatabase.addImage("card", bitmap);
        config.setAugmentedImageDatabase(augmentedImageDatabase);
        return true;
    }

    private void makeTransactionCall(String cardNumber) {
        Call<JsonArray> call = transactionAPI.getTransaction(cardNumber);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        JsonArray body = response.body();
                        //Transaction information fetched
                        for (int i = 0; i < body.size(); i++) {
                            JsonObject transactionObject = (JsonObject) body.get(i);
                            double totalAmount = transactionObject.get("total_amount").getAsDouble();
                            double worldPoint = transactionObject.get("world_point").getAsDouble();

                            String store = transactionObject.get("store").getAsString().split("T")[0];
                            String sector = transactionObject.get("sector").getAsString().split("T")[0];
                            String date = transactionObject.get("date").getAsString().split("T")[0];

                            //total World points calculation for each transaction
                            totalWorldPoints += worldPoint;
                            TransactionModel transactionModel = new TransactionModel(store, sector, date, totalAmount, worldPoint);

                            transactionModelArrayList.add(transactionModel);
                            recyclerAdapter.notifyDataSetChanged();

                            //worlpointtext's text has changed via this method
                            if (transactionModelArrayList.size() == body.size()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        worldPointTextCredit.setText(String.valueOf(totalWorldPoints));
                                        worldPointTextDebit.setText(String.valueOf(totalWorldPoints));
                                        worldPointTextPrepaid.setText(String.valueOf(totalWorldPoints));
                                    }
                                });
                            }
                        }

                    } else {
                        System.out.println("Body NULL!!");
                    }
                } else {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void placeTextView(View view, Anchor anchor) {
        ViewRenderable.builder()
                .setView(this, view)
                .build()
                .thenAccept(viewRenderable -> {
                    placeInstant(viewRenderable, anchor);
                });
    }

    private void placeInstant(ViewRenderable viewRenderable, Anchor anchor) {
        ArSceneView sceneView = arFragment.getArSceneView();

        if (!anchorList.isEmpty()) {
            anchorList.get(0).detach();
            anchorList.set(0, anchor);
        } else anchorList.add(anchor);

        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(sceneView.getScene());

        viewRenderable.setShadowCaster(false);
        viewRenderable.setShadowReceiver(false);

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(viewRenderable);
        node.getScaleController().setMaxScale(0.1f);
        node.getScaleController().setMinScale(0.04f);
        node.getRotationController().setRotationRateDegrees(180);
        node.setLocalRotation(Quaternion.axisAngle(new Vector3(1f, 0, 0), 270));
        node.select();
    }
}