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
 * AIDL的语法十分简单，与Java语言基本保持一致，需要记住的规则有以下几点：
 * 1.AIDL文件以 .aidl 为后缀名
 * 2.AIDL支持的数据类型分为如下几种：
 * 3.八种基本数据类型：byte、char、short、int、long、float、double、boolean
 * 4.String，CharSequence
 * 5.实现了Parcelable接口的数据类型
 * 6.List 类型。List承载的数据必须是AIDL支持的类型，或者是其它声明的AIDL对象
 * 7.Map类型。Map承载的数据必须是AIDL支持的类型，或者是其它声明的AIDL对象
 *
 * AIDL文件可以分为两类。一类用来声明实现了Parcelable接口的数据类型，以供其他AIDL文件使用那些非默认支持的数据类型。
 * 还有一类是用来定义接口方法，声明要暴露哪些接口给客户端调用，定向Tag就是用来标注这些方法的参数值
 * 定向Tag。定向Tag表示在跨进程通信中数据的流向，用于标注方法的参数值，分为 in、out、inout 三种。
 * 其中 in 表示数据只能由客户端流向服务端， out 表示数据只能由服务端流向客户端，
 * 而 inout 则表示数据可在服务端与客户端之间双向流通。此外，如果AIDL方法接口的参数值类型是：
 * 基本数据类型、String、CharSequence或者其他AIDL文件定义的方法接口，那么这些参数值的定向 Tag 默认是
 * 且只能是 in，所以除了这些类型外，其他参数值都需要明确标注使用哪种定向Tag。定向Tag具体的使用差别后边会有介绍
 * 明确导包。在AIDL文件中需要明确标明引用到的数据类型所在的包名，即使两个文件处在同个包名下。
 *
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
 *
 *
 * 面试官：看你简历上写熟悉 AIDL，说一说 oneway（d单行） 吧
 * oneway 主要有两个特性：异步调用和串行化处理。异步调用是指应用向 binder 驱动发送数据后不需要挂起线程等待 binder 驱动的回复，而是直接结束。
 * 像一些系统服务调用应用进程的时候就会使用 oneway，比如 AMS 调用应用进程启动 Activity，这样就算应用进程中做了耗时的任务，也不会阻塞系统服务的运行。
 * 串行化处理是指对于一个服务端的 AIDL 接口而言，所有的 oneway 方法不会同时执行，binder 驱动会将他们串行化处理，排队一个一个调用。
 * 面试官：有了解过相关的 binder 协议吗？
 * 涉及到的 binder 命令也有规律，由外部发送给 binder 驱动的都是 BC_ 开头，由 binder 驱动发往外部的都是 BR_开头。
 * 面试官：怎么理解客户端线程挂起等待呢？有没有实际占用 CPU 的调度？
 * 这里的挂起相当于 Thread 的 sleep，是真正的"休眠"，底层调用的是 waiteventinterruptible() Linux 系统函数。
 * 面试官：你是从哪里了解到 waiteventinterruptible() 函数的呢？
 * 在学习 Handle 机制的时候，Handle 中最关键的地方就是 Looper 的阻塞与唤醒，阻塞是调用了 nativePollOnce() 方法，当时对它的底层实现感兴趣，
 * 就去了解了一下，也学习到 Linux 用来实现阻塞/唤醒的 select、poll 和 epoll 机制
 *
 *
 * 1.基于 mmap 又是如何实现一次拷贝的？
 * Client 与 Server 处于不同进程有着不同的虚拟地址规则，所以无法直接通信。而一个页框可以映射给多个页，那么就可以将一块物理内存分别与 Client 和 Server 的虚拟内存块进行映射。
 * 如图， Client 就只需 copy_from_user 进行一次数据拷贝，Server 进程就能读取到数据了。另外映射的虚拟内存块大小将近 1M (1M-8K)，所以 IPC 通信传输的数据量也被限制为此值。
 * 2.怎么理解页框和页？
 * 页框是指一块实际的物理内存，页是指程序的一块内存数据单元。内存数据一定是存储在实际的物理内存上，即页必然对应于一个页框，页数据实际是存储在页框上的。
 * 页框和页一样大，都是内核对内存的分块单位。一个页框可以映射给多个页，也就是说一块实际的物理存储空间可以映射给多个进程的多个虚拟内存空间，这也是 mmap 机制依赖的基础规则。
 *
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
        books.add(new Book(1, "android"));
        books.add(new Book(2, "IOS"));
        new Thread(new AddBookRunnable()).start();

    }

    public class AddBookRunnable implements Runnable {

        @Override
        public void run() {
            while (!mIsDestory.get()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "sleep");
                }
                int bookid = books.size() + 1;
                Book book = new Book(bookid, "数学" + bookid);
                books.add(book);
                Log.d(TAG, books.size() + "本书");
                for (int i = 0; i < mIOnNewBookArrivedListeners.size(); i++) {
                    try {
                        mIOnNewBookArrivedListeners.get(i).onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.d(TAG, "RemoteException");
                    }
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mIsDestory.set(true);
        super.onDestroy();
    }

}
