package com.android.michaello.horoscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class TestScanner implements Scanner {
    private Context context;

    public TestScanner(Context context) {
        this.context = context;
    }

    @Override
    public ScanResult scan(Bitmap b) {
        b = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.puppy);

        ScanResult result = new ScanResult();

        BarcodeDetector detector =
                new BarcodeDetector.Builder(context)
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();

        if (!detector.isOperational()) {
            result.setString(context.getString(R.string.error_no_detector));
        }

        Frame frame = new Frame.Builder().setBitmap(b).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        if (barcodes.size() != 0) {
            Barcode thisCode = barcodes.valueAt(0);
            result.setString(thisCode.rawValue);
        } else {
            result.setString(context.getString(R.string.error_no_barcode));
        }

        return result;
    }
}