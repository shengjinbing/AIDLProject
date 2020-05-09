package com.example.lx.aidldemo.binder.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by lixiang on 2020/5/9
 * Describe:
 */
public class MyService extends Binder implements IMyService{
    public MyService() {
        this.attachInterface( this, DESCRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    /** 将MyService转换为IMyService接口 **/
    public static IMyService asInterface( android.os.IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if (((iInterface != null)&&(iInterface instanceof IMyService))){
            return ((IMyService) iInterface);
        }
        return null;
    }

    /** 服务端，接收远程消息，处理onTransact方法 **/
    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_say: {
                data.enforceInterface(DESCRIPTOR);
                String str = data.readString();
                sayHello(str);
                reply.writeNoException();
                return true;
            }}
        return super.onTransact(code, data, reply, flags);
    }

    /** 自定义sayHello()方法 **/
    @Override
    public void sayHello(String str) {
        System.out.println("MyService:: Hello, " + str);
    }

}
