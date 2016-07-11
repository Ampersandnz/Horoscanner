package com.android.michaello.horoscanner;

import android.graphics.Bitmap;

/**
 * Created by michaello on 11/7/2016.
 */
public class ScanResult {
    private String stringResult;
    private int intResult;
    private Bitmap bitmapResult;

    public ScanResult() {
        stringResult = null;
        intResult = -1;
        bitmapResult = null;
    }

    public String getString() {
        return stringResult;
    }

    public ScanResult setString(String s) {
        stringResult = s;
        return this;
    }

    public int getNumber() {
        return intResult;
    }

    public ScanResult setNumber(int i) {
        intResult = i;
        return this;
    }

    public Bitmap getBitmap() {
        return bitmapResult;
    }

    public ScanResult setBitmap(Bitmap b) {
        bitmapResult = b;
        return this;
    }
}
