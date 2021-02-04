package com.example.lx.aidldemo.ui.services;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.R;

public class ServiceActivity extends AppCompatActivity {


    private Intent mIntent;
    private Intent mIntentNormal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        mIntent = new Intent(this, UIIntentService.class);
        //普通服务
        mIntentNormal = new Intent(this, NormalService.class);


    }

    public void startService(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i++ < 3) {
                    SystemClock.sleep(4000);
                    startService(mIntent);
                }
            }
        }).start();

    }

    public void starNotifi(View view) {
        startService(new Intent(this,NotificationService.class));
    }

    public void bindService(View view) {
        //1.onCreate —>onBind  —>（onServiceConnected）
        //2、多次调用bindService，服务本身未执行任何操作。
        //BIND_AUTO_CREATE 只要绑定存在，就自动建立
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        unbindService(mServiceConnection);
    }

    public void stopService(View view) {
        stopService(mIntent);
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(UIIntentService.TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(UIIntentService.TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(UIIntentService.TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(UIIntentService.TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(UIIntentService.TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(UIIntentService.TAG, "onDestroy");
    }

/******************************开启普通服务**************************************/


    /**
     * 1.开启服务后，Activity销毁不影响服务。
     * 2.绑定服务后，Activity销毁服务解绑，如果没有开启服务还会走服务的onDestroy方法。
     * 3.多个activity绑定服务后，有一个activity解绑了服务不影响服务。
     *
     */
    public void startService_normal(View view) {
        startService(mIntentNormal);
    }

    public void bindService_normal(View view) {
        //1.onCreate —>onBind  —>（onServiceConnected）
        //2、多次调用bindService，服务本身未执行任何操作。
        //BIND_AUTO_CREATE 只要绑定存在，就自动建立
        bindService(mIntentNormal, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void unbindService_normal(View view) {
        unbindService(mServiceConnection);
    }

    public void stopService_normal(View view) {
        stopService(mIntentNormal);
    }

}