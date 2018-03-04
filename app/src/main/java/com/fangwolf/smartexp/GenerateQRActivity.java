package com.fangwolf.smartexp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRActivity extends AppCompatActivity {

    EditText message;
    Button confirm;
    ImageView QR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        message = (EditText) findViewById(R.id.message);
        confirm = (Button) findViewById(R.id.confirm);
        QR = (ImageView) findViewById(R.id.QR);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCode(v);
            }
        });
    }

    public void createCode(View view) {
        Bitmap bitmap;
        BitMatrix matrix;
        MultiFormatWriter writer = new MultiFormatWriter();
        String words = message.getText().toString();//输入的内容
        try {
            matrix = writer.encode(words, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            QR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
