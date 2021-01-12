package com.example.lx.aidldemo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.lx.aidldemo.ui.services.UIService;

/**
 * Created by lixiang on 2021/1/6
 * Describe:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        bindService(new Intent(this, UIService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                UIService.MyBinder binder = (UIService.MyBinder) service;
                UIService UIService = binder.getService();
                //UIService.setHandler(mHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
    }
}
