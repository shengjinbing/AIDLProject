package com.example.lx.aidldemo.binder.client;

import android.os.RemoteException;


/**
 * Created by lixiang on 2020/5/9
 * Describe:
 */
public class ClientDemo {
    public static void main(String[] args) throws RemoteException {
        System.out.println("Client start");
        //IBinder binder = ServiceManager.getService("MyService"); //获取名为"MyService"的服务
        //IMyService myService = new MyServiceProxy(binder); //创建MyServiceProxy对象
        //myService.sayHello("binder"); //通过MyServiceProxy对象调用接口的方法
        System.out.println("Client end");
    }
}
