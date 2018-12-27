package com.example.lx.aidldemo.ui.activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lx.aidldemo.R;
import com.example.lx.aidldemo.ui.receiver.LocalReceiver;
import com.example.lx.aidldemo.ui.receiver.MyBroadcastReceiver;
import com.example.lx.aidldemo.ui.receiver.MyBroadcastReceiver1;

/**
 * 广播的安全性问题:
 * 1.其他App可能会针对性的发出与当前App intent-filter相匹配的广播，由此导致当前App不断接收到广播并处理；
 * 2.其他App可以注册与当前App一致的intent-filter用于接收广播，获取广播具体信息
 * 3.对于同一App内部发送和接收广播，将exported属性人为设置成false，使得非本App内部发出的此广播不被接收；
 * 4.在广播发送和接收时，都增加上相应的permission，用于权限验证；
 * 5.发送广播时，指定特定广播接收器所在的包名，具体是通过intent.setPackage(packageName)指定，这样此广播将只会发送到此包中的App内与之相匹配的有效广播接收器中。
 * 6.采用LocalBroadcastManager的方式
 * <p>
 * <p>
 * 静态注册：context为ReceiverRestrictedContext
 * 动态注册：context为Activity的context
 * LocalBroadcastManager的动态注册：context为Application的context
 */
public class ReceiverActivity extends AppCompatActivity {

    private MyBroadcastReceiver mReceiver;
    private MyBroadcastReceiver1 mReceiver1;
    private LocalReceiver mReceiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        initView();
        initLocalBroadcastManager();

    }

    private void initLocalBroadcastManager() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.loacl");
        filter.setPriority(10);
        mReceiver2 = new LocalReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver2, filter);
    }

    /**
     * 隐式启动需要intentFilter，只有一个Intent同时匹配action，category，data才能算完全匹配
     * action：
     * 1.区分大小写
     * 2.多个action匹配成功任意一个就算匹配成功
     * category：
     * 1.多个category都需要匹配
     * 2.没有category的时候需要协商默认的android.intent.category.DEFAULT。
     * data:
     * 1.由两部分组成，mimeType和URI，mimeType是媒体类型
     * 2.URI没有指定Scheme那么整个URI参数无效。
     * 3.URI的主机名，host未指定那么整个URI中的其他参数无效，也就意味着URI无效。
     * 4.如果要指定完整的data必要调用setDataAndType,因为setData()和setType会清除对方的值
     */
    private void initView() {
        //priority优先级：数字越高优先级越高
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net");
        filter.setPriority(10);
        mReceiver = new MyBroadcastReceiver();
        registerReceiver(mReceiver, filter, "com.example.lx.permission_test", null);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.net");
        filter1.setPriority(20);
        mReceiver1 = new MyBroadcastReceiver1();
        registerReceiver(mReceiver1, filter1, "com.example.lx.permission_test", null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mReceiver1);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver2);
    }

    /**
     * 同级别接收先后是随机的（无序的）
     * 级别低的后接收到广播
     * 接收器不能截断广播的继续传播，也不能处理广播
     * 同级别动态注册（代码中注册）高于静态注册（AndroidManifest中注册）
     *
     * @param view
     */
    public void send(View view) {
        sendBroadcast(new Intent("android.net"));
    }

    /**
     * 1.同级别接收是随机的(结合下一条)
     * 2.同级别动态注册（代码中注册）高于静态注册（AndroidManifest中注册）
     * 3.排序规则为：将当前系统中所有有效的动态注册和静态注册的BroadcastReceiver按照priority属性值从大到小排序
     * 4.先接收的BroadcastReceiver可以对此有序广播进行截断，使后面的BroadcastReceiver不再接收到此广播，
     * 也可以对广播进行修改，使后面的BroadcastReceiver接收到广播后解析得到错误的参数值。
     * 当然，一般情况下，不建议对有序广播进行此类操作，尤其是针对系统中的有序广播。
     *
     * @param view
     */
    public void sendOrder(View view) {
        //sendorBroadcast(new Intent("android.net"));
        Intent intent = new Intent();
        intent.setAction("android.net");
        //注册receiver时，直接指定发送者应该具有的权限。不然外部应用依旧可以触及到receiver
        sendOrderedBroadcast(intent, "com.example.lx.permission_test");
    }

    /**
     * 通过LocalBroadcastManager实现本地广播
     *
     * @param view
     */
    public void sendLocal(View view) {
        Intent intent = new Intent();
        intent.setAction("android.net.loacl");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
