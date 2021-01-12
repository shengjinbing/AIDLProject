package com.example.lx.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.lx.aidlservice.Book;
import com.example.lx.aidlservice.IBookManager;
import com.example.lx.aidlservice.IOnNewBookArrivedListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "AIDLCLIENT_LOG";
    private IBookManager mIBookManager;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient(){

        /**
         * 当Binder死亡的时候回调
         */
        @Override
        public void binderDied() {
            if (mIBookManager == null){
                return;
            }
            mIBookManager.asBinder().unlinkToDeath(mDeathRecipient,0);
            mIBookManager = null;
            Log.d(TAG,"binderDied");
            //TODO:这里重新绑定远程Service
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    ServiceConnection mServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("BBBBB",Thread.currentThread()+"11111111");
            mIBookManager = IBookManager.Stub.asInterface(service);

            try {
                //0是标记位，我们可以直接设置为0
                service.linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                List<Book> listBook = mIBookManager.getListBook();
                Log.d(TAG,listBook.size()+","+listBook.toString());
                mIBookManager.addBook(new Book(2,"数学"));
                Log.d(TAG, mIBookManager.getListBook().size()+","+ mIBookManager.getListBook().toString());

                mIBookManager.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {


        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.d(TAG,newBook.toString());
        }
    };

    public void bindservice_remote(View view) {
        Intent intent = new Intent();
        intent.setAction("it.test.aidl");
        ComponentName cn = new ComponentName("com.example.lx.aidlservice", "com.example.lx.aidlservice.services.BookManagerService");
        intent.setComponent(cn);
        //这样可以成功
        //intent.setPackage("com.example.administrator.ipcdemo");
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindservice_remote() {
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindservice_remote();
    }
}
