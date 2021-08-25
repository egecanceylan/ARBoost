package com.oneandonly.arboost.models;

import android.graphics.Bitmap;

public class CardImage {
    private Bitmap bitmap;
    private static CardImage instance = null;

    public static CardImage getInstance() {
        if (instance == null)
            instance = new CardImage();

        return instance;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
