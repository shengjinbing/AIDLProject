package com.example.lx.aidldemo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.R;

public class BActivity extends AppCompatActivity {
    public static final String TAG = "AACTIVITY_LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        Log.d(TAG,"BonCreate");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"BonStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"BonRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"BonResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"BonPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"BonStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"BonDestroy");
      /*  for (int i = 0; i < 20; i++) {
            try {
                Log.d(TAG,i+"onDestroy");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    public void finish(View view) {
        finish();
        setResult(2);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"BonNewIntent");
    }

    public void startC(View view) {
        Intent intent = new Intent(this, CActivity.class);
        //具有这个标记的Activity不会出现在历史Activity的列表中，当某些情况下我们不希望用户通过历史列表回
        //到我们的Activity的时候这个标记比较有用
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }
}

