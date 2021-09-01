package com.oneandonly.arboost.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.oneandonly.arboost.R;
import com.oneandonly.arboost.models.CardImage;
import com.oneandonly.arboost.models.CardModel;
import com.oneandonly.arboost.models.TransactionModel;
import com.oneandonly.arboost.models.UserModel;
import com.oneandonly.arboost.service.CardAPI;
import com.oneandonly.arboost.service.RetrofitClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button scan;
    private ImageView info;
    int MY_SCAN_REQUEST_CODE = 111;
    private CardAPI cardAPI;
    private ProgressBar progressBar;
    private RelativeLayout homePage, progressPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = findViewById(R.id.main_scan_button);
        info = findViewById(R.id.main_info_button);
        cardAPI = RetrofitClient.getInstances().getCardAPI();
        progressBar = findViewById(R.id.main_progress_bar);
        homePage = findViewById(R.id.main_home_page);
        progressPage = findViewById(R.id.main_progress_page);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                makeApiCall("4943141334422544", 3);

                Intent scanIntent = new Intent(MainActivity.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true); // default: false  || True seçersek ek doğrulama ekranına gitmiyor.
                scanIntent.putExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false  || Expiry date'i istiyorsak true yapmamız lazım
                scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, false); // default: true
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Bilgilen!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeApiCall(String cardNumber, int userId) {
        homePage.setVisibility(View.GONE);
        progressPage.setVisibility(View.VISIBLE);
        Call<JsonObject> call = cardAPI.getCard(cardNumber, userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        JsonObject body = response.body();
                        System.out.println(body);

                        // User information
                        JsonObject user = (JsonObject) body.get("user_id");
                        System.out.println(user);
                        int userId = user.get("id").getAsInt();
                        String name = user.get("name").getAsString();
                        String surname = user.get("surname").getAsString();
                        System.out.println(userId + " , " + name + " , " + surname);
                        UserModel userModel = new UserModel(userId, name, surname);

                        // Card information
                        String cardNumber = body.get("card_number").getAsString();
                        // In order to get xxxx xxxx xxxx xxxx format we use stringbuilder and
                        // add blanks after every 4 numbers.
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < cardNumber.length(); i++) {
                            sb.append(cardNumber.charAt(i));
                            if ((i + 1) % 4 == 0 && i != cardNumber.length() - 1)
                                sb.append(" ");
                        }
                        cardNumber = sb.toString();
                        double accountLimit = body.get("account_limit").getAsDouble();
                        double currentDebt = body.get("current_debt").getAsDouble();
                        double totalDebt = body.get("total_debt").getAsDouble();
                        // cutoffDate, paymentDueDate and expireDate are formatted as 2021-08-06T17:45:52.217+00:00
                        // we need only the date not the time so we split the text by 'T' and get
                        // the first index.
                        String cutoffDate = body.get("cutoff_date").getAsString().split("T")[0];
                        String paymentDueDate = body.get("payment_due_date").getAsString().split("T")[0];
                        String expireDate = body.get("expire_date").getAsString().split("T")[0];
                        String eAccountStatement = body.get("e_account_statement").getAsString();
                        String type = body.get("type").isJsonNull() ? "null" : body.get("type").getAsString();
                        String accountNumber = body.get("account_number").isJsonNull() ? "null" : body.get("account_number").getAsString();
                        double balance = body.get("balance").isJsonNull() ? 0 : body.get("balance").getAsDouble();
                        double flexibleAccountLimit = body.get("flexible_account_limit").isJsonNull() ? 0 : body.get("flexible_account_limit").getAsDouble();
                        boolean isContactless = body.get("is_contactless").getAsBoolean();
                        boolean isEcom = body.get("is_ecom").getAsBoolean();
                        boolean mailOrder = body.get("mail_order").getAsBoolean();
                        boolean isAutomated = body.get("is_automatic_payment_order").getAsBoolean();
                        boolean isCurrency = body.get("is_currency_account_statement").getAsBoolean();

                        CardModel cardModel = new CardModel(cardNumber, cutoffDate, paymentDueDate, expireDate,
                                eAccountStatement, accountNumber, type, userModel, accountLimit,
                                currentDebt, totalDebt, balance, flexibleAccountLimit, isContactless, isEcom, mailOrder, isAutomated, isCurrency);

                        // Make user wait for 2.5 seconds in order to read instructions
                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, ArActivity.class);
                                intent.putExtra("cardModel", cardModel);
                                startActivityForResult(intent, 0);
                            }
                        };
                        handler.postDelayed(runnable, 2500);
                    }
                    else {
                        progressPage.setVisibility(View.GONE);
                        homePage.setVisibility(View.VISIBLE);
                        System.out.println("Body NULL!!");
                    }
                }
                else{
                    progressPage.setVisibility(View.GONE);
                    homePage.setVisibility(View.VISIBLE);
                    System.out.println(response.code());
                    if (response.code() == 400) {
                        try {
                            String errorText = response.errorBody().string();
                            new AlertDialog.Builder(MainActivity.this)
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressPage.setVisibility(View.GONE);
                homePage.setVisibility(View.VISIBLE);
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap cardImage = null;
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                cardImage = CardIOActivity.getCapturedCardImage(data);
                CardImage cardImageModel = CardImage.getInstance();
                cardImageModel.setBitmap(cardImage);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                //resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                resultDisplayStr = "Card Number: " + scanResult.getFormattedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }

            // resultDisplayStr comes as Cardnumber:xxxx xxxx xxxx xxxx we change it to
            // xxxxxxxxxxxxxxxx format in order to make API call
            resultDisplayStr = resultDisplayStr.split(":")[1].replace(" ", "").substring(0, 16);
            makeApiCall(resultDisplayStr, 1);
        } else if (requestCode == 0) {
            // If user come back from ar activity render main page
            progressPage.setVisibility(View.GONE);
            homePage.setVisibility(View.VISIBLE);
        }
    }
}