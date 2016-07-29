package com.android.michaello.horoscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class FaceScanner {
    private Context context;

    public FaceScanner(Context context) {
        this.context = context;
    }

    public Face scan(Bitmap b) {
        FaceDetector detector =
                new FaceDetector.Builder(context).build();

        if (!detector.isOperational()) {
            return null;
        }

        Frame frame = new Frame.Builder().setBitmap(b).build();
        SparseArray<Face> faces = detector.detect(frame);

        detector.release();

        if (faces.size() > 0) {
            return faces.valueAt(0);
        }

        return null;
    }
}