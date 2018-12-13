package com.example.lx.aidldemo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by lixiang
 * on 2018/12/12
 */
public class SystemUtil {
    public static void printProcessName(Activity activity,String Tag){
        ActivityManager am = (ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (int i = 0; i < list.size(); i++) {
            Log.i(Tag, String.valueOf(list.get(i).processName));
        }
    }
}
