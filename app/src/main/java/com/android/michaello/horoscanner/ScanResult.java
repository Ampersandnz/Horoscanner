package com.android.michaello.horoscanner;

import android.graphics.Color;

public class ScanResult {
    private String _stringResult;
    private int _intResult;
    private Color _colorResult;

    public ScanResult() {
        _stringResult = null;
        _intResult = -1;
        _colorResult = null;
    }

    public String getString() {
        return _stringResult;
    }

    public ScanResult setString(String s) {
        _stringResult = s;
        return this;
    }

    public int getNumber() {
        return _intResult;
    }

    public ScanResult setNumber(int i) {
        _intResult = i;
        return this;
    }

    public Color getColor() {
        return _colorResult;
    }

    public ScanResult setColor(Color b) {
        _colorResult = b;
        return this;
    }
}
