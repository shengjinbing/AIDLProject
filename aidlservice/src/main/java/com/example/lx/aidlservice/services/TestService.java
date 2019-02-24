package com.example.lx.aidlservice.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.lx.aidlservice.R;

public class TestService extends Service {
    public static final String TAG = "Test_LOG";

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG,"onBind");
        return  null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("你好啊");
        builder.setContentTitle("woshibiaoti");
        builder.setWhen(System.currentTimeMillis());
        Notification build = builder.build();
        //前台Service
        startForeground(1,build);
        super.onCreate();
    }

    /**
     * 方法过时
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        //Log.d(TAG,"onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }
}
