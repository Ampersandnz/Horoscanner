package com.android.michaello.horoscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView _txtView;
    private TextView _numView;
    private ImageView _imageView;
    private Scanner _scanner;
    private ScanResult _lastScan;
    private String _currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //Bitmap imageBitmap = BitmapFactory.decodeFile(_currentPhotoPath);

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
}