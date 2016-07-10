package com.android.michaello.horoscanner;

import android.content.Context;

/**
 * Created by Michael on 10/07/2016.
 */
public class TestScanner implements Scanner {
    private Context context;

    public TestScanner(Context context) {
        this.context = context;
    }

    @Override
    public String scan() {
        return context.getString(R.string.scan_result_test);
    }
}