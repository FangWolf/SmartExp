package com.fangwolf.smartexp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GenerateQRActivity extends AppCompatActivity {

    EditText Sname;
    EditText Sphone;
    EditText SDadress;
    EditText Rname;
    EditText Rphone;
    EditText RDadress;

    Spinner Sadress_province;
    Spinner Sadress_city;
    Spinner Sadress_district;
    int a = 0;
    Spinner Radress_province;
    Spinner Radress_city;
    ;
    Spinner Radress_district;

    Button confirm;
    Button cancel;
    ImageView QR;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        Sname = (EditText) findViewById(R.id.Sname);
        Sphone = (EditText) findViewById(R.id.Sphone);
        SDadress = (EditText) findViewById(R.id.SDadress);
        Rname = (EditText) findViewById(R.id.Rname);
        Rphone = (EditText) findViewById(R.id.Rphone);
        RDadress = (EditText) findViewById(R.id.RDadress);

        Sadress_province = (Spinner) findViewById(R.id.Sadress_province);
        Sadress_city = (Spinner) findViewById(R.id.Sadress_city);
        Sadress_district = (Spinner) findViewById(R.id.Sadress_district);
        Radress_province = (Spinner) findViewById(R.id.Radress_province);
        Radress_city = (Spinner) findViewById(R.id.Radress_city);
        Radress_district = (Spinner) findViewById(R.id.Radress_district);

        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);
        QR = (ImageView) findViewById(R.id.QR);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //输入框不能为空
                    if (TextUtils.isEmpty(Sname.getText()) || TextUtils.isEmpty(Sphone.getText()) || TextUtils.isEmpty(SDadress.getText()) ||
                            TextUtils.isEmpty(Rname.getText()) || TextUtils.isEmpty(Rphone.getText()) || TextUtils.isEmpty(RDadress.getText())) {
                        Toast.makeText(GenerateQRActivity.this, "null", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("GenerateQRActivity", "name=" + Sname.getText().toString() + " phone=" + Sphone.getText().toString() + " adress=" + SDadress.getText().toString());
                        createCode(v);
                    }
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

        //发件详细地址
        Sadress_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message shengmessage = new Message();
                shengmessage.what = (int) id;
                a = shengmessage.what;
                shenghandler.sendMessage(shengmessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Sadress_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message shimessage = new Message();
                shimessage.what = a * 10 + (int) id;
                shihandler.sendMessage(shimessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //收件详细地址
        Radress_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message shengmessage = new Message();
                shengmessage.what = (int) id;
                a = shengmessage.what;
                shenghandler2.sendMessage(shengmessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Radress_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Message shimessage = new Message();
                shimessage.what = a * 10 + (int) id;
                shihandler2.sendMessage(shimessage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    //Spinner 寄件人省选择
    @SuppressLint("HandlerLeak")
    private Handler shenghandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SpinnerAdapter shi0Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.henanshi, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_city.setAdapter(shi0Adapter);
                    SpinnerAdapter qu0Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.zhengzhouqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu0Adapter);
                    break;
                case 1:
                    SpinnerAdapter shiAdapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.anhuishi, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_city.setAdapter(shiAdapter);
                    SpinnerAdapter quAdapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.hefeiqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(quAdapter);
                    break;
                case 2:
                    SpinnerAdapter shi2Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.shanxishi, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_city.setAdapter(shi2Adapter);
                    SpinnerAdapter qu2Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.xianqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu2Adapter);
                    break;
                default:
                    break;
            }
        }
    };
    //Spinner 寄件人市选择
    @SuppressLint("HandlerLeak")
    private Handler shihandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SpinnerAdapter qu0Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.zhengzhouqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu0Adapter);
                    break;
                case 1:
                    SpinnerAdapter quAdapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.kaifengqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(quAdapter);
                    break;
                case 10:
                    SpinnerAdapter qu1Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.hefeiqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu1Adapter);
                    break;
                case 11:
                    SpinnerAdapter qu2Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.luaniqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu2Adapter);
                    break;
                case 20:
                    SpinnerAdapter qu3Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.xianqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu3Adapter);
                    break;
                case 21:
                    SpinnerAdapter qu4Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.xianyangqu, R.layout.support_simple_spinner_dropdown_item);
                    Sadress_district.setAdapter(qu4Adapter);
                    break;
                default:
                    break;

            }
        }
    };

    //Spinner 收件人省选择
    @SuppressLint("HandlerLeak")
    private Handler shenghandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SpinnerAdapter shi0Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.henanshi, R.layout.support_simple_spinner_dropdown_item);
                    Radress_city.setAdapter(shi0Adapter);
                    SpinnerAdapter qu0Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.zhengzhouqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu0Adapter);
                    break;
                case 1:
                    SpinnerAdapter shiAdapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.anhuishi, R.layout.support_simple_spinner_dropdown_item);
                    Radress_city.setAdapter(shiAdapter);
                    SpinnerAdapter quAdapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.hefeiqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(quAdapter);
                    break;
                case 2:
                    SpinnerAdapter shi2Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.shanxishi, R.layout.support_simple_spinner_dropdown_item);
                    Radress_city.setAdapter(shi2Adapter);
                    SpinnerAdapter qu2Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.xianqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu2Adapter);
                    break;
                default:
                    break;
            }
        }
    };
    //Spinner 收件人市选择
    @SuppressLint("HandlerLeak")
    private Handler shihandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SpinnerAdapter qu0Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.zhengzhouqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu0Adapter);
                    break;
                case 1:
                    SpinnerAdapter quAdapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.kaifengqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(quAdapter);
                    break;
                case 10:
                    SpinnerAdapter qu1Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.hefeiqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu1Adapter);
                    break;
                case 11:
                    SpinnerAdapter qu2Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.luaniqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu2Adapter);
                    break;
                case 20:
                    SpinnerAdapter qu3Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.xianqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu3Adapter);
                    break;
                case 21:
                    SpinnerAdapter qu4Adapter = ArrayAdapter.createFromResource(GenerateQRActivity.this, R.array.xianyangqu, R.layout.support_simple_spinner_dropdown_item);
                    Radress_district.setAdapter(qu4Adapter);
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            //优化内存
            bitmap.recycle();
            bitmap = null;
        }
    }

    //输入的内容生成二维码
    public void createCode(View view) throws UnsupportedEncodingException {

        BitMatrix matrix;
        //拼接生成的信息
        StringBuilder stringBuilder = new StringBuilder("fangwolf shi handsome&");
        stringBuilder.append((Sname.getText()) + "&")
                .append((Sphone.getText()) + "&")
                .append(Sadress_province.getSelectedItem())
                .append(Sadress_city.getSelectedItem())
                .append(Sadress_district.getSelectedItem() + "&")
                .append((SDadress.getText() + "&"))
                .append((Rname.getText()) + "&")
                .append((Rphone.getText() + "&"))
                .append(Radress_province.getSelectedItem())
                .append(Radress_city.getSelectedItem())
                .append(Radress_district.getSelectedItem() + "&")
                .append(RDadress.getText());
        try {
            matrix = new MultiFormatWriter().encode(new String(stringBuilder.toString().getBytes("UTF-8"),
                    "ISO-8859-1"), BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
            QR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
