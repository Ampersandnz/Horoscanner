package com.android.michaello.horoscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//Camera intent and permissions code taken from a variety of different tutorials
//None of them completely worked, but I've managed to mash them all together into something that maybe does
public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int PERMISSIONS_REQUEST_CAMERA = 2;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private TextView _txtView;
    private TextView _numView;
    private ImageView _imageView;
    private Scanner _scanner;
    private ScanResult _lastScan;
    private String _currentPhotoPath;
    private Bitmap _bitmap;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getImageFileUri() {
        return Uri.fromFile(getImageFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getImageFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Horoscanner");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        mediaFile.deleteOnExit();

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onButtonClick(view);
////                openCameraActivity();
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        _txtView = (TextView) findViewById(R.id.txt_scan_result_text);
        _numView = (TextView) findViewById(R.id.txt_scan_result_number);
        _imageView = (ImageView) findViewById(R.id.img_scan_result_image);
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan_test_barcode:
                _scanner = new TestScanner(getApplicationContext());
                _currentPhotoPath = null;
                Bitmap b = BitmapFactory.decodeResource(
                        getResources(), R.drawable.puppy);
                _imageView.setImageBitmap(b);
                _lastScan = _scanner.scan(b);
                displayResults(_lastScan);
                break;

            case R.id.fab:
                _scanner = new BarcodeScanner(getApplicationContext());
                openCameraActivity();
                break;

            default:
                _scanner = null;
                _txtView.setText(R.string.error_no_scanner);
                _numView.setText("");
                _imageView.setImageResource(android.R.color.transparent);
        }
    }

    private void openCameraActivity() {
        if (checkPermissions()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri fileUri = getImageFileUri();
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
            _lastScan = _scanner.scan(getImage(_currentPhotoPath));
            displayResults(_lastScan);

            setImage(_currentPhotoPath);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermissions() {
        if (!(checkPermissions_Storage())) {
            return false;
        } else if (!(checkPermissions_Camera())) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermissions_Storage() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermissions_Camera() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private Bitmap getImage(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    private void setImage(String filePath) {
        int targetW = 500;
        int targetH = 500;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        _imageView.setImageBitmap(bitmap);
    }

    private void displayResults(ScanResult result) {
        _numView.setText("" + result.getNumber());
        _txtView.setText(result.getString());
        //TODO: Add an ImageView
        //TODO: _colorView.setColor(result.getColor());
    }
}