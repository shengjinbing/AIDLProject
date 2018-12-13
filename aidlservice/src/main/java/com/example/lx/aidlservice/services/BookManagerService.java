package com.example.lx.aidlservice.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


import com.example.lx.aidlservice.Book;
import com.example.lx.aidlservice.IBookManager;
import com.example.lx.aidlservice.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    private final static String TAG = "AIDLSERVIVE_LOG";
    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mIOnNewBookArrivedListeners = new CopyOnWriteArrayList<>();

    private AtomicBoolean mIsDestory = new AtomicBoolean(false);

    public BookManagerService() {
    }

    Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getListBook() throws RemoteException {
            return books;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            books.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (!mIOnNewBookArrivedListeners.contains(listener)) {
                mIOnNewBookArrivedListeners.add(listener);
            }
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (mIOnNewBookArrivedListeners.contains(listener)) {
                mIOnNewBookArrivedListeners.remove(listener);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        books.add(new Book(1,"android"));
        books.add(new Book(2,"IOS"));
        new Thread(new AddBookRunnable()).start();

    }

    public class AddBookRunnable implements Runnable{

        @Override
        public void run() {
            while (!mIsDestory.get()){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG,"sleep");
                }
                int bookid = books.size() + 1;
                Book book = new Book(bookid, "数学" + bookid);
                books.add(book);
                Log.d(TAG,books.size()+"本书");
                for (int i = 0; i < mIOnNewBookArrivedListeners.size(); i++) {
                    try {
                        mIOnNewBookArrivedListeners.get(i).onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.d(TAG,"RemoteException");
                    }
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        mIsDestory.set(true);
        super.onDestroy();
    }

}
