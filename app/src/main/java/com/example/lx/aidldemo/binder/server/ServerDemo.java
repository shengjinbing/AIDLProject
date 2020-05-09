package com.example.lx.aidldemo.binder.server;

import android.os.Looper;

/**
 * Created by lixiang on 2020/5/9
 * Describe:
 */
public class ServerDemo {
    public static void main(String[] args) {
        System.out.println("MyService Start");
        //准备Looper循环执行
        Looper.prepareMainLooper();
        //设置为前台优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_FOREGROUND);
        //注册服务
        //ServiceManager.addService("MyService", new MyService());
        Looper.loop();
    }
}
