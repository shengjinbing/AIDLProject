package com.example.lx.aidldemo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.R;

public class AActivity extends AppCompatActivity {

    public static final String TAG = "AACTIVITY_LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Log.d(TAG,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
       /* for (int i = 0; i < 8; i++) {
            try {
                Log.d(TAG,i+"onPause");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        /*for (int i = 0; i < 8; i++) {
            try {
                Log.d(TAG,i+"onStop");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult"+"requestCode=="+requestCode+"resultCode=="+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startBActivity(View view) {
        startActivityForResult(new Intent(this,BActivity.class),1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"onNewIntent");
    }
}
