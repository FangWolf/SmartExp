package com.fangwolf.smartexp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView resultQR5;
    TextView resultQR6;
    Button sms;
    Button call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionMenu fabMenu = findViewById(R.id.fabMenu);
        FloatingActionButton fabScanQR = findViewById(R.id.fabScanQR);
        FloatingActionButton fabGenerateQR = findViewById(R.id.fabGenerateQR);
        sms = findViewById(R.id.sms);
        call = findViewById(R.id.call);

        resultQR5 = findViewById(R.id.result5);
        resultQR6 = findViewById(R.id.result6);

        //判断短信权限并发送短信
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                } else if (resultQR5.getText().toString().equals("暂无扫描内容")){
                    Toast.makeText(MainActivity.this,"请扫描后在操作",Toast.LENGTH_SHORT).show();
                } else {
                    sendSMS();
                }
            }
        });
        //判断电话权限并拨打电话
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else if (resultQR6.getText().toString().equals("暂无扫描内容")){
                    Toast.makeText(MainActivity.this,"请扫描后在操作",Toast.LENGTH_SHORT).show();
                } else {
                    callPhone();
                }
            }
        });

        //扫描按钮
        fabScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(false)//扫码的方向
                        .setCaptureActivity(ScanQRActivity.class)
                        .setPrompt("对准二维码开始扫描")//下方提示文字
                        .setCameraId(0)//前置或后置摄像头
                        .setBeepEnabled(true)//扫码提示音
                        .initiateScan();
                fabMenu.close(false);//关闭浮动菜单

            }
        });
        //生成按钮
        fabGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateQRActivity.class);
                startActivity(intent);
                fabMenu.close(false);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + resultQR6.getText().toString());
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void sendSMS() {
        //打开短信页面编辑发送
        /*Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri data = Uri.parse("smsto:"+10086);
        intent.setData(data);
        intent.putExtra("sms_body", "cxll");
        startActivity(intent);*/
        //直接发送
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage("【菜鸟驿站】"+resultQR5.getText().toString()+"，你的包裹到郑州轻工业学院宿舍二号楼菜鸟驿站，请21:00前领取，联系方式：13512345678");
        for (String text : divideContents) {
            smsManager.sendTextMessage(resultQR6.getText().toString(), null, text,null,null );
            Toast.makeText(MainActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
        }
    }

    //处理扫码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //Base64 解码

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "你取消了扫描", Toast.LENGTH_LONG).show();
            } else {
                String miwen = result.getContents().toString();
                String jiemi =new String(Base64.decode(miwen.getBytes(), Base64.DEFAULT));
                if (fangwolf(jiemi)) {
                    decodecode(jiemi);    //解码
                } else {
                    Toast.makeText(MainActivity.this,"不是专属二维码",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    //解码-拆分扫描结果
    private void decodecode(String s) {
        String[] str = s.split("\\&");
        /*resultQR.setText("寄件人姓名：" + str[1]);
        resultQR2.setText("寄件人电话：" + str[2]);
        resultQR3.setText("寄件人地址：" + str[3]);
        resultQR4.setText("寄件人详细地址：" + str[4]);*/
        resultQR5.setText(str[5]);//"收件人姓名："
        resultQR6.setText(str[6]);//"收件人电话："
        /*resultQR7.setText("收件人地址：" + str[7]);
        resultQR8.setText("收件人详细地址：" + str[8]);*/
    }

    //判断是否为本工具生成的二维码
    private boolean fangwolf (String s) {
        String[] str = s.split("\\&");
        if (str[0].equals("fangwolf shi handsome")) {
            return  true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*//创建右上三点
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //右上三点
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id ==R.id.action_help) {
            Toast.makeText(this,"clisk help",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //侧滑菜单
        int id = item.getItemId();

        if (id == R.id.nav_scanData) {

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
