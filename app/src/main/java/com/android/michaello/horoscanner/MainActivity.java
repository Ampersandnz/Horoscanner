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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//Camera intent and permissions code taken from a variety of different tutorials
//None of them completely worked, but I've managed to mash them all together into something that maybe does
public class MainActivity extends AppCompatActivity {
    private TextView _txtView;
    private TextView _numView;
    private ImageView _imageView;
    private Scanner _scanner;
    private ScanResult _lastScan;
    private String _currentPhotoPath;
    private Bitmap _bitmap;

    private static final int REQUEST_TAKE_PHOTO = 1;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int PERMISSIONS_REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        _txtView = (TextView) findViewById(R.id.txt_scan_result_text);
        _numView = (TextView) findViewById(R.id.txt_scan_result_number);
        _imageView = (ImageView) findViewById(R.id.img_scan_result_image);
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan_test_barcode:
                Log.d("Horoscanner", "Barcode button pressed");
                _scanner = new TestImageScanner(getApplicationContext());
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
        Log.d("Horoscanner", "openCameraActivity started");
        if (checkPermissions()) {
            Log.d("Horoscanner", "Permissions are fine");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri fileUri = getImageFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            _currentPhotoPath = fileUri.getPath();

            if (intent.resolveActivity(getPackageManager()) != null) {
                Log.d("Horoscanner", "About to open camera");
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            } else {

                Log.d("Horoscanner", "intent.resolveActivity(getPackageManager()) is null");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        Log.d("Horoscanner", "Camera activity returned");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
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

    private void setImage(String filePath) {
        int targetW = 2000;
        int targetH = 2000;

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
                Log.d("Horoscanner", "failed to create directory");
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
}