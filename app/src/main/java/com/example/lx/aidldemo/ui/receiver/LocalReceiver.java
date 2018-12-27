package com.example.lx.aidldemo.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by lixiang
 * on 2018/12/24
 */
public class LocalReceiver extends BroadcastReceiver{
    public static final String TAG = "MyBroadcast_LOG";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"LocalReceiver");
    }
}