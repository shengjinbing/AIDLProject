package com.example.lx.aidldemo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.R;

public class CActivity extends AppCompatActivity {
    public static final String TAG = "AACTIVITY_LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        Log.d(TAG,"ConCreate");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"ConStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"ConRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"ConResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"ConPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"ConStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"ConDestroy");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"ConNewIntent");
    }

    public void startD(View view) {
        startActivity(new Intent(this,DActivity.class));
    }
}
