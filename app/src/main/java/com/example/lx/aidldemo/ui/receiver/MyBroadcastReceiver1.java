package com.example.lx.aidldemo.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lixiang
 * on 2018/12/24
 */
public class MyBroadcastReceiver1 extends BroadcastReceiver {
    public static final String TAG = "MyBroadcast_LOG";
    @Override
    public void onReceive(Context context, Intent intent) {
        abortBroadcast();
        Log.d(TAG,"onReceive1");
    }
}
