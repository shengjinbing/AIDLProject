package com.example.lx.aidldemo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
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

    /**
     * 生命周期
     *onActivityResult->onNewIntent->ConRestart->ConStart->ConResume
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"ConNewIntent");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 生命周期执行顺序
     * onActivityResult（最先执行）->ConRestart->ConStart->ConResume
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"ConActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *生命周期变化:
     * 1.C->D
     * ConCreate->ConStart->ConResume->ConPause->DonCreate->DonStart->DonResume->ConStop
     * 2.再按下返回键盘
     * DonPause->ConRestart->ConStart->ConResume->DonStop->DonDestroy
     *
     * 3.切换横竖屏时，onCreate会调用吗？几次？
     * 竖屏->横屏 ConPause->onSaveInstanceState->ConStop->ConDestroy->ConCreate->ConStart->onRestoreInstanceState->ConResume
     * 横屏-竖屏 ConPause->onSaveInstanceState->ConStop->ConDestroy->ConCreate->ConStart->onRestoreInstanceState->ConResume
     */
    public void startD(View view) {
        startActivity(new Intent(this,DActivity.class));
    }
}
