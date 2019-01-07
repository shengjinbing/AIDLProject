package com.example.lx.aidlservice.binder;

import java.util.List;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.example.lx.aidlservice.Book;

/**
 * 1.AIDL的文件本质只是系统为我们提供了一种快速实现Binder的工具，仅此而已
 * 2.Binder 通信采用 C/S 架构，从组件视角来说，包含 Client、 Server、 ServiceManager
 * 以及 Binder 驱动，其中 ServiceManager 用于管理系统中的各种服务。
 * 3.Binder 在 framework 层进行了封装，通过 JNI 技术调用 Native（C/C++）层的 Binder 架构。
 * 4.Binder 在 Native 层以 ioctl 的方式与 Binder 驱动通讯。
 *
 *
 *
 * 内核模块/驱动
 * 通过系统调用，用户空间可以访问内核空间，那么如果一个用户空间想与另外一个用户空间进行通信怎么办呢？
 * 很自然想到的是让操作系统内核添加支持；传统的 Linux 通信机制，比如 Socket，管道等都是内核支持的；
 * 但是 Binder 并不是 Linux 内核的一部分，它是怎么做到访问内核空间的呢？ Linux 的动态可加载内核
 * 模块（Loadable Kernel Module，LKM）机制解决了这个问题；模块是具有独立功能的程序，它可以被单
 * 独编译，但不能独立运行。它在运行时被链接到内核作为内核的一部分在内核空间运行。这样，Android系统
 * 可以通过添加一个内核模块运行在内核空间，用户进程之间的通过这个模块作为桥梁，就可以完成通信了。
 * 在 Android 系统中，这个运行在内核空间的，负责各个用户进程通过 Binder 通信的内核模块叫做
 * Binder 驱动;
 *
 */
public class BookManagerImpl extends Binder implements IBookManager {

    /**
     * Construct the stub at attach it to the interface.
     */
    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * Cast an IBinder object into an IBookManager interface, generating a proxy
     * if needed.
     */
    public static IBookManager asInterface(IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof IBookManager))) {
            return ((IBookManager) iin);
        }
        return new Proxy(obj);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    /**
     * 1.Parcel就是一个存放读取数据的容器， android系统中的binder进程间通信(IPC)就使用了Parcel类来
     * 进行客户端与服务端数据的交互，而且AIDL的数据也是通过Parcel来交互的。在Java空间和C++都实现了
     * Parcel，由于它在C/C++中，直接使用了内存来读取数据，因此，它更有效率。
     * 2.这个方法运行在客户端，当客户端发起跨进程请求的时，远程请求会通过系统的底层封装后交由此方法来处理。
     *   如果返回false，那么客户端会请求失败，因此可以用这个特性来做权限验证。
     *
     * @param code
     * @param data 取出目标方法所需要的参数
     * @param reply 如果目标方法有返回值的话就像reply中写入
     * @param flags
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                //方法id
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if ((0 != data.readInt())) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
            default:
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        // TODO 待实现
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        // TODO 待实现
    }

    private static class Proxy implements IBookManager {
        private IBinder mRemote;

        Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null)) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }
    }

}
