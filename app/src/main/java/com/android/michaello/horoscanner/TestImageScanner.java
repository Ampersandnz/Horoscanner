package com.android.michaello.horoscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Created by Michael on 10/07/2016.
 */
public class TestImageScanner implements Scanner {
    private Context context;

    public TestImageScanner(Context context) {
        this.context = context;
    }

    @Override
    public String scan() {
        //ImageView myImageView = (ImageView) findViewById(R.id.img_scan_result__test_image);
        Bitmap myBitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.puppy);
        //myImageView.setImageBitmap(myBitmap);

        BarcodeDetector detector =
                new BarcodeDetector.Builder(context)
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();

        if (!detector.isOperational()) {
            return context.getString(R.string.error_no_detector);
        }

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        Barcode thisCode = barcodes.valueAt(0);

        return thisCode.rawValue;
    }
}