package com.android.michaello.horoscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class FaceScanner implements Scanner {
    private Context context;

    public FaceScanner(Context context) {
        this.context = context;
    }

    @Override
    public ScanResult scan(Bitmap b) {
        ScanResult result = new ScanResult();

        FaceDetector detector =
                new FaceDetector.Builder(context).build();

        if (!detector.isOperational()) {
            result.setString(context.getString(R.string.error_no_detector));
        }

        Frame frame = new Frame.Builder().setBitmap(b).build();
        SparseArray<Face> faces = detector.detect(frame);

        if (faces.size() > 0) {
            Face thisFace = faces.valueAt(0);
            result.setString(thisFace.toString());
            result.setNumber(thisFace.getId());
        } else {
            return null;
        }

        return result;
    }
}