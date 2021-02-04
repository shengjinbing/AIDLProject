package com.example.lx.aidldemo.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class NormalService extends Service {
    public static final String TAG = "Test_LOG";

    public NormalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onbind");
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(TAG,"SonStart");
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"SonDestroy");
        //默认mServiceLooper.quit();
        super.onDestroy();
    }
}
