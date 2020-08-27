package com.example.lx.aidldemo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.R;

public class DActivity extends AppCompatActivity {
    public static final String TAG = "AACTIVITY_LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        Log.d(TAG,"DonCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"DonStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"DonRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"DonResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"DonPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"DonStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"DonDestroy");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"DonNewIntent");
    }

    public void startA(View view) {
        Intent intent = new Intent(this, AActivity.class);
        //相当于指定singTask
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //相当于指定singtop
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void startB(View view) {
        Intent intent = new Intent(this, BActivity.class);
        //1.这个标记一般与singTask模式一起出现，如果系统已经存在被启动的Activity直接走onNewIntent方法
        //2.如果启动的是standard模式，那么它连同它之上的Activity都要出栈，系统会创建新的Activity放入栈顶。
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startC(View view) {
        Intent intent = new Intent(this, CActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        /*setResult(100);
        finish();*/
    }
}
