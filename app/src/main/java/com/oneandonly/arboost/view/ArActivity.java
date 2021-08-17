package com.oneandonly.arboost.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.oneandonly.arboost.R;
import com.oneandonly.arboost.models.CardModel;

public class ArActivity extends AppCompatActivity {

    private ArFragment arFragment;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        Intent intent = getIntent();
        CardModel cardmodel = intent.getParcelableExtra("cardModel");

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.credit_card_home_screen, null);

        TextView debt = view.findViewById(R.id.credit_card_home_screen_current_debt);
        TextView currentLimit = view.findViewById(R.id.credit_card_home_screen_card_limit);
        TextView totalLimit = view.findViewById(R.id.credit_card_home_screen_total_card_limit);

        debt.setText(String.valueOf(cardmodel.getDebt()));
        currentLimit.setText(String.valueOf(cardmodel.getAccountLimit()));
        totalLimit.setText(String.valueOf(cardmodel.getFlexibleAccountLimit()));

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            placeTextView(hitResult.createAnchor(), view);
        });


//
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