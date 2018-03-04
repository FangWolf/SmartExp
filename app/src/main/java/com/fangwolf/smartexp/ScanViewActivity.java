package com.fangwolf.smartexp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScanViewActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener{

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    Button swichLight;
    private boolean isLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_view);

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        barcodeScannerView.setTorchListener(this);
        //手电筒控制
        swichLight = findViewById(R.id.light);
        swichLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    barcodeScannerView.setTorchOff();
                } else {
                    barcodeScannerView.setTorchOn();
                }
            }
        });

        capture = new CaptureManager(this,barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    //手电开
    @Override
    public void onTorchOn() {
        isLightOn = true;
    }

    //手电关
    @Override
    public void onTorchOff() {
        isLightOn = false;
    }
}
