package com.example.lx.aidldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lx.aidldemo.ui.activitys.ProcessActivity;
import com.example.lx.aidldemo.ui.activitys.ReceiverActivity;
import com.example.lx.aidldemo.utils.IntentConstant;
import com.example.lx.aidldemo.utils.SystemUtil;

/**
 * 进程间通信的方式，Bundle,文件共享，AIDL，Messenger,ContentProvider,Socket,广播
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = "BBBBB";
    public static int age = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SystemUtil.printProcessName(this,TAG);
        startThread();
    }

    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                age--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        }).start();

    }

    /**
     * 开启一个进程activity，
     * 通过JNI在native层去fork一个新的进程
     *
     * @param view
     */
    public void activity(View view) {
        Intent intent = new Intent(this, ProcessActivity.class);
        intent.putExtra(IntentConstant.Name,age);
        startActivity(intent);
    }

    public void receiver(View view) {
        startActivity(new Intent(this, ReceiverActivity.class));
    }
}
