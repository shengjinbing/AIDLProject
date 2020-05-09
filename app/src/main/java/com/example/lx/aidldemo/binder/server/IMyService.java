package com.example.lx.aidldemo.binder.server;

import android.os.IInterface;
import android.os.RemoteException;

/**
 * Created by lixiang on 2020/5/9
 * Describe:
 */
public interface IMyService extends IInterface {
    static final java.lang.String DESCRIPTOR = "com.gityuan.frameworkBinder.MyServer";
    public void sayHello(String str) throws RemoteException;
    static final int TRANSACTION_say = android.os.IBinder.FIRST_CALL_TRANSACTION;
}
