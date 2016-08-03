package com.android.michaello.horoscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.face.Face;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private FaceScanner _scanner;
    private Face _lastFace;
    private String _currentPhotoPath;
    private float _scaleFactorX;
    private float _scaleFactorY;
    private TextView _DEBUGnumView;
    private TextView _DEBUGnumView2;
    private TextView _DEBUGtxtView;
    private TextView _DEBUGtxtView2;
    private TextView _txtViewLuckyNumbers;
    private TextView _txtViewAdvice;
    private TextView _txtViewColour;
    private ImageView _imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _DEBUGnumView = (TextView) findViewById(R.id.txt_scan_result_number);
        _DEBUGnumView2 = (TextView) findViewById(R.id.txt_scan_result_number_2);
        _DEBUGtxtView = (TextView) findViewById(R.id.txt_scan_result_text);
        _DEBUGtxtView2 = (TextView) findViewById(R.id.txt_scan_result_text_2);
        _txtViewLuckyNumbers = (TextView) findViewById(R.id.txt_luckyNums_numbers);
        _txtViewAdvice = (TextView) findViewById(R.id.txt_advice_text);
        _txtViewColour = (TextView) findViewById(R.id.txt_colour_text);
        _imageView = (ImageView) findViewById(R.id.img_scan_result_image);
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                openCameraActivity();
                break;

            default:
                _scanner = null;
                _lastFace = null;
        }
    }

    private void openCameraActivity() {
        if (Utility.checkPermissions(this)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri fileUri = Utility.getImageFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            _currentPhotoPath = fileUri.getPath();

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //TODO: Do this in a background task somehow
            _scanner = new FaceScanner(getApplicationContext());
            _lastFace = _scanner.scan(Utility.getBitmapFromFile(_currentPhotoPath));

            if (_lastFace != null) {
                displayResults();
            } else {
                displayResults();
            }
        }
    }

    private Bitmap drawFaceRectangle(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);

        int x1 = Math.round(_lastFace.getPosition().x * _scaleFactorX);
        int y1 = Math.round(_lastFace.getPosition().y * _scaleFactorY);
        int x2 = Math.round((_lastFace.getPosition().x + _lastFace.getWidth()) * _scaleFactorX);
        int y2 = Math.round((_lastFace.getPosition().y + _lastFace.getHeight()) * _scaleFactorY);

        RectF rect = new RectF(x1, y1, x2, y2);

        canvas.drawBitmap(bitmap, 0, 0, null);

        canvas.drawOval(rect, paint);

        return tempBitmap;
    }

    private void setImage(String filePath) {
        if (filePath != null) {
            //TODO: Rewrite this so the imageview fits nicely into the screen
            int targetW = 500;
            int targetH = 500;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            //TODO: Figure out how this works because it makes no sense to me. Changing the target W and H seem to arbitrarily scale the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inMutable = true;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
            _scaleFactorX = (float) bitmap.getWidth() / (float) photoW;
            _scaleFactorY = (float) bitmap.getHeight() / (float) photoH;

            _imageView.setImageBitmap(drawFaceRectangle(bitmap));
        } else {
            _imageView.setImageResource(android.R.color.transparent);
        }
    }

    private void displayResults() {
        String faceDimensionsString, facePositionString, faceAsString, hashCodeString, luckyNumbersString, adviceString, colourString;

        if (_lastFace == null) {
            faceAsString = "";
            hashCodeString = "";
            faceDimensionsString = "";
            facePositionString = "";
            luckyNumbersString = "";
            adviceString = "";
            colourString = "";
        } else {
            faceAsString = _lastFace.toString();
            hashCodeString = "" + _lastFace.hashCode();
            faceDimensionsString = _lastFace.getWidth() + " x " + _lastFace.getHeight();
            facePositionString = "(" + _lastFace.getPosition().x + ", " + _lastFace.getPosition().y + ")";
            luckyNumbersString = (_lastFace.hashCode() % 61) + ", " + (_lastFace.hashCode() % 75) + ", " + (_lastFace.hashCode() % 99) + ", " + (_lastFace.hashCode() % 100) + ", " + (_lastFace.hashCode() % 85);
            adviceString = getString(R.string.string_advice_default);
            colourString = getString(R.string.string_colour_default);
            ;
        }

        setImage(_currentPhotoPath);

        _DEBUGnumView.setText(faceDimensionsString);
        _DEBUGnumView2.setText(facePositionString);
        _DEBUGtxtView.setText(faceAsString);
        _DEBUGtxtView2.setText(hashCodeString);
        _txtViewLuckyNumbers.setText(luckyNumbersString);
        _txtViewAdvice.setText(adviceString);
        _txtViewColour.setText(colourString);
    }
}