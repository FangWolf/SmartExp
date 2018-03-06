package com.fangwolf.smartexp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GenerateQRActivity extends AppCompatActivity {

    RecyclerView mRecyclerview;
    List<MoreTypeBean> mData;

    EditText message;
    Button confirm;
    Button cancel;
    ImageView QR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        message = (EditText) findViewById(R.id.message);
        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);
        QR = (ImageView) findViewById(R.id.QR);

        inintView();
        inintData();
        inintViewOper();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createCode(v);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void inintView() {
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerviewGenerateQR);
    }

    private void inintData() {
        mData = new ArrayList<>();
        for (int i=0;i<3;i++) {
            MoreTypeBean moreTypeBean = new MoreTypeBean();
            moreTypeBean.type = i;
            mData.add(moreTypeBean);

        }
    }

    private void inintViewOper() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        GeneratrQRAdapter adapter = new GeneratrQRAdapter(mData);
        mRecyclerview.setAdapter(adapter);
    }

    //输入的内容生成二维码
    public void createCode(View view) throws UnsupportedEncodingException {
        Bitmap bitmap;
        BitMatrix matrix;
        String words = message.getText().toString();
        try {
            matrix = new MultiFormatWriter().encode(new String(message.getText().toString().getBytes("UTF-8"),"ISO-8859-1"),
                    BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            QR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
