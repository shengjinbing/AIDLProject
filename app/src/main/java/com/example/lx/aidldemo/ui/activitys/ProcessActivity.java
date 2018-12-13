package com.example.lx.aidldemo.ui.activitys;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lx.aidldemo.MainActivity;
import com.example.lx.aidldemo.R;
import com.example.lx.aidldemo.utils.IntentConstant;
import com.example.lx.aidldemo.utils.SystemUtil;

import java.util.List;

/**
 * 进程间不能共享数据
 */
public class ProcessActivity extends AppCompatActivity {
    private final static String TAG = "BBBBB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        //SystemUtil.printProcessName(this,TAG);
        initIntent();
        startThread();
    }

    private void initIntent() {
        Intent intent = getIntent();
        int age = intent.getIntExtra(IntentConstant.Name, 0);
        Log.d(TAG,"age="+age);
    }
    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        Log.d(TAG, "age=" + MainActivity.age);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
