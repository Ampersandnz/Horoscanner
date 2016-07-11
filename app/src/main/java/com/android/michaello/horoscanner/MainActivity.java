package com.android.michaello.horoscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView _txtView;
    private TextView _numView;
    private ImageView _imageView;
    private Scanner _scanner;
    private ScanResult _lastScan;

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.android.michaello.horoscanner.fileprovider";

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
                _scanner = new TestImageScanner(getApplicationContext());
                openCamera();
                break;

            default:
                _scanner = null;
                _txtView.setText(R.string.error_no_scanner);
                _numView.setText("");
                _imageView.setImageResource(android.R.color.transparent);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createNewImageInCache();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, CAPTURE_IMAGE_FILE_PROVIDER, photoFile);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Bitmap imageBitmap = BitmapFactory.decodeFile(_currentPhotoPath);
            _imageView.setImageBitmap(imageBitmap);

            //_lastScan = _scanner.scan(imageBitmap);

//            String stringResult = _lastScan.getString();
//            int intResult = _lastScan.getNumber();
//            Bitmap bitmapResult = _lastScan.getBitmap();
//
//            _txtView.setText(stringResult);
//
//            _numView.setText((intResult == -1)? "" : "" + intResult);
//
//            _imageView.setImageBitmap(bitmapResult);
        }
    }

    private File createNewImageInCache() throws IOException {
        String imageFileName = "currentPhoto.jpg";

        File path = new File(getCacheDir(), "images/");

        if (!path.exists()) {
            path.mkdirs();
        }

        File image = new File(path, imageFileName);

        image.deleteOnExit();

        return image;
    }
}