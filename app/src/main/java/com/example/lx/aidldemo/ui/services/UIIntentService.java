package com.example.lx.aidldemo.ui.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * IntentService是google在原生的Service基础上通过创建子线程的Service。也就是说IntentService是专门为android开
 * 发者提供的能在service内部实现耗时操作的service。我们可以通过重写onHandleIntent方法实现耗时操作的回调处理，
 * 而且IntentService在耗时操作完成后，会主动销毁自己，IntentService可以通过多次启动来完成多个任务，而IntentService
 * 只会被创建一次，每次启动的时候只会触发onStart方法。内部是实现了Handler异步处理耗时操作的过程，一般多用在Service中需要处理耗时操作的功能。
 提问：为什么IntentService中能实现耗时操作?在onCreate中，通过HandlerThread来开启一条线程，而HandlerThread线程中
 会跟我们平常用的Handler不太一样，在run方法中创建了looper对象，所以HandlerThread能让IntentService在子线程中使用handler达到耗时操作





 查看IntentService源码，其中的onBind（）函数被实现，而且返回null。这从侧面就证明了以上结论。再者，IntentService本身
 就是异步的，本身就不能确定是否在activity销毁后还是否执行，如果用bind的话，activity销毁的时候，IntentService还在执行任务的话就很矛盾了。
 */
public class UIIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.lx.aidldemo.ui.services.action.FOO";
    private static final String ACTION_BAZ = "com.example.lx.aidldemo.ui.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.lx.aidldemo.ui.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.lx.aidldemo.ui.services.extra.PARAM2";

    private static final String TAG = "BBBBB";


    public UIIntentService() {
        super("UIIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UIIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UIIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(TAG,"onStart");
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
