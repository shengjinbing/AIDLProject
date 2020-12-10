package com.example.lx.aidlservice.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lx.aidlservice.R;

public class TestService extends Service {
    public static final String TAG = "Test_LOG";

    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG,"onBind");
        return  null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    public static final String CHANNEL_ID = "com.medi.nimsdk.NotificationService";
    public static final String CHANNEL_NAME = "com.medi.nimsdk";
    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText("你好啊");
        builder.setContentTitle("woshibiaoti");
        // 设置优先级，优先级决定了 Android7.1 及更低版本通知的侵入程度
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setWhen(System.currentTimeMillis());
        Notification build = builder.build();
        //前台Service,提高优先级不被杀死
        startForeground(1,build);

        super.onCreate();
    }

    /**
     * 安卓8.0之后必须先创建通知通道
     */
    private void registerNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                //是否在桌面icon右上角展示小红点
                channel.enableLights(false);
                //小红点颜色
                channel.setLightColor(Color.RED);
                //通知显示
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                //是否在久按桌面图标时显示此渠道的通知
                //channel.setShowBadge(true);
                mNotificationManager.createNotificationChannel(channel);
            }
        }
    }



    /**
     * 方法过时
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        //Log.d(TAG,"onStart");
        super.onStart(intent, startId);
    }

    /**
     * @return 有四种返回值结果
     * 1.START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试
     *    重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在
     *    此期间没有任何启动命令被传递到service，那么参数Intent将为null。
     * 2.START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
     * 3.START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，
     *    系统会自动重启该服务，并将Intent的值传入。(redeliver在投递的意思)
     * 4.START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }
}
