package com.example.lx.aidldemo.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 1.通过Handler和Service通信。
 * 2.通过广播通信（结合更好IntentService）
 * 3.通过共享文件
 * 4.Messenger
 * 5.AIDL
 */
public class UIService extends Service {

    public UIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        Log.d("BBBBB","onCreate");

    }

    public class MyBinder extends Binder{
        public UIService getService() {
            return UIService.this;
        }
    }

    public void setHandler(Handler os){
        os.sendEmptyMessage(1);
    }
}
