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
 *
 * 前台进程
 * 用户正在交互的 Activity（ onResume() ）
 * 当某个 Service 绑定正在交互的 Activity
 * 被主动调用为前台 Service（ startForeground() ）
 * 组件正在执行生命周期的回调（ onCreate() 、onStart() 、onDestory() ）
 * BroadcastReceiver 正在执行 onReceive()
 *
 * 作者：_yuanhao
 * 链接：https://juejin.cn/post/6844903983656468488
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
