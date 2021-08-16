package com.oneandonly.arboost.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.oneandonly.arboost.R;

public class ArActivity extends AppCompatActivity {

    private ArFragment arFragment;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

//        Intent intent = getIntent();
//        CardModel cardmodel = intent.getParcelableExtra("cardModel");

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            placeTextView(hitResult.createAnchor());
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
    private void placeTextView(Anchor anchor) {
        ViewRenderable.builder()
                .setView(this, R.layout.debit_card_details)
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