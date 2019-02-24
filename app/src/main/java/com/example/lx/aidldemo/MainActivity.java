package com.example.lx.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.ui.activitys.ProcessActivity;
import com.example.lx.aidldemo.ui.activitys.ProviderActivity;
import com.example.lx.aidldemo.ui.activitys.ReceiverActivity;
import com.example.lx.aidldemo.ui.services.UIService;
import com.example.lx.aidldemo.utils.IntentConstant;

/**
 * 进程间通信的方式，Bundle,文件共享，AIDL，Messenger,ContentProvider,Socket,广播
 *
 *
 * 测量应用启动的时长
 * 1.adb shell am start -w packagename/activity,是可以完全应用的启动时间的.不过也要分场景
 *   startTime记录的刚准备调用startActivityAndWait()的时间点
 *   endTime记录的是startActivityAndWait()函数调用返回的时间点
 *   WaitTime = startActivityAndWait()调用耗时。
 *
 * 2.Android中ClassLoader的种类&特点
 * BootClassLoader（Java的BootStrap ClassLoader）
 * 用于加载Android Framework层class文件。
 * PathClassLoader（Java的App ClassLoader）
 * 用于加载已经安装到系统中的apk中的class文件。
 * DexClassLoader（Java的Custom ClassLoader）
 * 用于加载指定目录中的class文件。
 * BaseDexClassLoader
 * 是PathClassLoader和DexClassLoader的父类。
 *
 * 因为遵循双亲委派模型，Android中的ClassLoader具有两个特点：
 * 类加载共享
 * 当一个class文件被任何一个ClassLoader加载过，就不会再被其他ClassLoader加载。
 * 类加载隔离
 * 不同ClassLoader加载的class文件肯定不是一个。举个栗子，一些系统层级的class文件在系统初始化的时候被加载，比如java.net.String，这个是在应用启动前就被系统加载好的。如果在一个应用里能简单地用一个自定义的String类把这个String类替换掉的话，将有严重的安全问题。
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

    public void provider(View view) {
        startActivity(new Intent(this, ProviderActivity.class));
    }



    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,msg.what+"");
            super.handleMessage(msg);
        }
    };

    public void service(View view) {
        bindService(new Intent(this, UIService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                UIService.MyBinder binder = (UIService.MyBinder) service;
                UIService UIService = binder.getService();
                UIService.setHandler(mHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
    }
}
