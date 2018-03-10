package com.fangwolf.smartexp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView resultQR;
    TextView resultQR2;
    TextView resultQR3;
    TextView resultQR4;
    TextView resultQR5;
    TextView resultQR6;
    TextView resultQR7;
    TextView resultQR8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        FloatingActionButton fabScanQR = (FloatingActionButton) findViewById(R.id.fabScanQR);
        FloatingActionButton fabGenerateQR = (FloatingActionButton) findViewById(R.id.fabGenerateQR);

        resultQR = (TextView) findViewById(R.id.result);
        resultQR2 = (TextView) findViewById(R.id.result2);
        resultQR3 = (TextView) findViewById(R.id.result3);
        resultQR4 = (TextView) findViewById(R.id.result4);
        resultQR5= (TextView) findViewById(R.id.result5);
        resultQR6 = (TextView) findViewById(R.id.result6);
        resultQR7 = (TextView) findViewById(R.id.result7);
        resultQR8 = (TextView) findViewById(R.id.result8);

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //处理扫码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "退出扫描", Toast.LENGTH_LONG).show();
            } else {
                decodecode(result.getContents().toString());
                //resultQR.setText(result.getContents().toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //拆分扫描结果
    private void decodecode(String s) {
        String[] str = s.split("\\&");
        resultQR.setText("寄件人姓名："+str[1]);
        resultQR2.setText("寄件人电话："+str[2]);
        resultQR3.setText("寄件人地址："+str[3]);
        resultQR4.setText("寄件人详细地址："+str[4]);
        resultQR5.setText("收件人姓名："+str[5]);
        resultQR6.setText("收件人电话："+str[6]);
        resultQR7.setText("收件人地址："+str[7]);
        resultQR8.setText("收件人详细地址："+str[8]);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //侧滑菜单
        int id = item.getItemId();

        if (id == R.id.nav_scanData) {

        } else if (id == R.id.nav_generateData) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
