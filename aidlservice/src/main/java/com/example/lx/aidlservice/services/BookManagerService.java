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

/**
 * 1.通过系统调用，用户空间可以访问内核空间，那么如果一个用户空间想与另外一个用户空间进行
 * 通信怎么办呢？很自然想到的是让操作系统内核添加支持；传统的 Linux 通信机制，比如
 * Socket，管道等都是内核支持的；但是 Binder 并不是 Linux 内核的一部分，它是怎么做到访
 * 问内核空间的呢？ Linux 的动态可加载内核模块（Loadable Kernel Module，LKM）机制解决
 * 了这个问题；模块是具有独立功能的程序，它可以被单独编译，但不能独立运行。它在运行时被链接
 * 到内核作为内核的一部分在内核空间运行。这样，Android系统可以通过添加一个内核模块运行在内
 * 核空间，用户进程之间的通过这个模块作为桥梁，就可以完成通信了。
 *
 * 2.Binder 线程池：每个 Server 进程在启动时创建一个 binder 线程池，并向其中注册一个
 * Binder 线程；之后 Server 进程也可以向 binder 线程池注册新的线程，或者 Binder 驱动在
 * 探测到没有空闲 binder 线程时主动向 Server 进程注册新的的 binder 线程。对于一个 Server
 * 进程有一个最大 Binder 线程数限制，默认为16个 binder 线程，例如 Android 的 system_server
 * 进程就存在16个线程。对于所有 Client 端进程的 binder 请求都是交由 Server 端进程的 binder
 * 线程来处理的
 *
 *
 * 3.了解了 Binder 驱动，怎么与 Binder 驱动进行通讯呢？那就是通过 ServiceManager，好多文章称
 * ServiceManager 是 Binder 驱动的守护进程，大管家，其实 ServiceManager 的作用很简单就是提
 * 供了查询服务和注册服务的功能。ServiceManager 的启动分为三步，首先打开驱动创建全局链表
 * binder_procs，然后将自己当前进程信息保存到 binder_procs 链表，最后开启 loop 不断的处理共享内
 * 存中的数据，并处理 BR_xxx 命令（ioctl 的命令，BR 可以理解为 binder reply 驱动处理完的响应）。
 */
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
