package com.example.lx.aidldemo.ui.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * 1.IntentService是google在原生的Service基础上通过创建子线程的Service。
 * 2.也就是说IntentService是专门为android开发者提供的能在service内部实现耗时操作的service。我们可以通过重
 *   写onHandleIntent方法实现耗时操作的回调处理，而且IntentService在耗时操作完成后，会主动销毁自己，
 * 3.IntentService可以通过多次启动来完成多个任务，如果之前的IntentService没有被销毁不会创建新的。
 *   内部是实现了Handler异步处理耗时操作的过程，一般多用在Service中需要处理耗时操作的功能。
 * 4.onBind()默认返回的是null，而service默认的是一个抽象方法，也就意味着默认只支持start方式启动该IntentService
 * 5.stopSelf(int startId)的特点了：
 * startId < 0：stopSelf()方法会直接停止Service
 * startId > 0 && startId != ServiceRecord.lastStartId：不会停止Service，代码流程直接返回
 * startId > 0 && startId == ServiceRecord.lastStartId: 直接停止Service。
 * https://blog.csdn.net/xlh1191860939/article/details/107225817
 *
 *
 提问：为什么IntentService中能实现耗时操作?
      在onCreate中，通过HandlerThread来开启一条线程，而HandlerThread线程中会跟我们平常用的Handler不太一样，在run方法
      中创建了looper对象，所以HandlerThread能让IntentService在子线程中使用handler达到耗时操作


 查看IntentService源码，其中的onBind（）函数被实现，而且返回null。这从侧面就证明了以上结论。再者，IntentService本身
 就是异步的，本身就不能确定是否在activity销毁后还是否执行，如果用bind的话，activity销毁的时候，IntentService还在执行任务的话就很矛盾了。
 */
public class UIIntentService extends IntentService {
    public static final String TAG = "Test_LOG";

    public UIIntentService() {
        super("UIIntentService");
        Log.d(TAG,"UIIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");
        SystemClock.sleep(3000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return super.onBind(intent);
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
