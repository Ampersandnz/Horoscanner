package com.android.michaello.horoscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private TextView _txtView;
    private TextView _numView;
    private ImageView _imageView;
    //TODO: Add an ImageView for a solid colour block
    private Scanner _scanner;
    private ScanResult _lastScan;
    private String _currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

            case R.id.btn_scan_test_face:
                openCameraActivity();
                break;

            case R.id.fab:
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
            _scanner = new FaceScanner(getApplicationContext());
            _lastScan = _scanner.scan(Utility.getBitmapFromFile(_currentPhotoPath));

            if (_lastScan != null) {
                setImage(_currentPhotoPath);
                displayResults(_lastScan);
            } else {
                _lastScan = new ScanResult();
                _lastScan.setString(getString(R.string.error_no_face));
                displayResults(_lastScan);
                setImage(null);
            }
        }
    }

    private void setImage(String filePath) {
        if (filePath != null) {
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
        } else {
            _imageView.setImageBitmap(null);
            _imageView.destroyDrawingCache();
        }
    }

    private void displayResults(ScanResult result) {
        String numString, txtString;

        if (result.getNumber() == -1) {
            numString = "No number returned from scan.";
        } else {
            numString = "" + result.getNumber();
        }

        if (result.getString() == null) {
            txtString = "No text returned from scan.";
        } else {
            txtString = result.getString();
        }

        _numView.setText(numString);
        _txtView.setText(txtString);
        //TODO: _colorView.setColor(result.getColor());
    }
}


//TODO: Circle the face in the bitmap displayed
//TODO: Hash the facial features in some way (hopefully will be reasonably consistent across multiple photos)