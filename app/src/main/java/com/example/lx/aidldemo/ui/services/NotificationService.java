package com.example.lx.aidldemo.ui.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.lx.aidldemo.MainActivity;
import com.example.lx.aidldemo.R;

/**
 * Created by lixiang on 2020-02-22
 * Describe:
 */
public class NotificationService extends Service {

    public static final String CHANNEL_ID = "com.medi.nimsdk.NotificationService";
    public static final String CHANNEL_NAME = "com.medi.nimsdk";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerNotificationChannel();
        int notifyId = (int) System.currentTimeMillis();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //跳转活动
        Intent intent =new Intent (this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pi);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mBuilder.setContentTitle(getResources().getString(R.string.app_real_name));
        }
        startForeground(notifyId, mBuilder.build());
    }

    /**
     * 注册通知通道
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

}


