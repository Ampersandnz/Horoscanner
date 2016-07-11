package com.android.michaello.horoscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Michael on 10/07/2016.
 */
public class TestScanner implements Scanner {
    private Context context;

    public TestScanner(Context context) {
        this.context = context;
    }

    @Override
    public ScanResult scan(Bitmap b) {
        ScanResult result = new ScanResult();

        Bitmap myBitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.puppy);

        result.setBitmap(myBitmap);
        result.setString(context.getString(R.string.scan_result_test));
        result.setNumber(1000);

        return result;
    }
}