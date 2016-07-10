package com.android.michaello.horoscanner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Scanner scanner;
    //private ScanResult lastScan;

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
    }

    public void scan(View v) {
        TextView txtView = (TextView) findViewById(R.id.txt_scan_result);

        switch (v.getId()) {
            case R.id.btn_scan_test_text:
                this.scanner = new TestScanner(getApplicationContext());
                break;

            case R.id.btn_scan_test_image:
                this.scanner = new TestImageScanner(getApplicationContext());
                break;

            default:
                this.scanner = null;
                txtView.setText(R.string.error_no_scanner);
                return;
        }

        String result = scanner.scan();

        //TODO: Convert to calling getString(), getImage(), getNumber() on the ScanResult object returned and, if they're not null, set the corresponding views.
        txtView.setText(result);
    }
}
