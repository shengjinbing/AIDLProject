package com.example.lx.aidldemo.ui.activitys;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lx.aidldemo.MainActivity;
import com.example.lx.aidldemo.R;
import com.example.lx.aidldemo.utils.IntentConstant;
import com.example.lx.aidldemo.utils.SystemUtil;

import java.util.List;

/**
 * 进程间不能共享数据
 *
 * 前台进程
 * 用户正在交互的 Activity（ onResume() ）
 * 当某个 Service 绑定正在交互的 Activity
 * 被主动调用为前台 Service（ startForeground() ）
 * 组件正在执行生命周期的回调（ onCreate() 、onStart() 、onDestory() ）
 * BroadcastReceiver 正在执行 onReceive()
 *
 * 作者：_yuanhao
 * 链接：https://juejin.cn/post/6844903983656468488
 *
 * Android的进程间通信方式。
 * 1.Android为何要自己搞一个binder，使用linux原有的通信方式不行么？（安全、性能好，方便易用）
 * Linux现有的所有进程间IPC方式：
 * 管道：在创建时分配一个page大小的内存，缓存区大小比较有限；
 * 消息队列：信息复制两次，额外的CPU消耗；不合适频繁或信息量大的通信；
 * 共享内存：无须复制，共享缓冲区直接付附加到进程虚拟地址空间，速度快；但进程间的同步问题操作系统无法实现，必须各进程利用同步工具解决；
 * 套接字：作为更通用的接口，传输效率低，主要用于不通机器或跨网络的通信；
 * 信号量：常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段。
 * 信号: 不适用于信息交换，更适用于进程中断控制，比如非法内存访问，杀死某个进程等
 * 5个角度来展开对Binder的分析：https://blog.csdn.net/gityuan/article/details/88969180
 * （1）从性能的角度
 * 数据拷贝次数：Binder数据拷贝只需要一次，而管道、消息队列、Socket都需要2次，但共享内存方式一次内存拷贝都不需要；从性能角度看，Binder性能仅次于共享内存。
 *  (2）从稳定性的角度
 * Binder是基于C/S架构的，简单解释下C/S架构，是指客户端(Client)和服务端(Server)组成的架构，Client端有什么需求，直接发送给Server端去完成，
 * 架构清晰明朗，Server端与Client端相对独立，稳定性较好；而共享内存实现方式复杂，没有客户与服务端之别， 需要充分考虑到访问
 * 临界资源的并发同步问题，否则可能会出现死锁等问题；从这稳定性角度看，Binder架构优越于共享内存。
 * （3）从安全的角度
 * 传统Linux IPC的接收方无法获得对方进程可靠的UID/PID，从而无法鉴别对方身份；而Android作为一个开放的开源体系，拥有非常多
 * 的开发平台，App来源甚广，因此手机的安全显得额外重要；对于普通用户，绝不希望从App商店下载偷窥隐射数据、后台造成手机耗电等等问题，传统Linux IPC无任何保护措施，完全由上层协议来确保。
 * （4）从语言层面的角度
 * 而对于Binder恰恰也符合面向对象的思想，将进程间通信转化为通过对某个Binder对象的引用调用该对象的方法，而其独特之处在于Binder对
 * 象是一个可以跨进程引用的对象，它的实体位于一个进程中，而它的引用却遍布于系统的各个进程之中
 * 比如在Android OS中的Zygote进程的IPC采用的是Socket（套接字）机制，Android中的Kill Process采用的signal（信号）机制等等
 *
 * 2.binder通信的内存大小限制。https://www.jianshu.com/p/ea4fc6aefaa8
 * 普通的APP来说，我们都是Zygote进程孵化出来的，Zygote进程的初始化Binder服务的时候提前调用了ProcessState这个类，
 * 所以普通的APP进程上限就是1MB-8KB。(8k是两个pagesize，一个pagesize是申请物理内存的最小单元;
 * Binder服务的初始化有两步，open打开Binder驱动，mmap在Binder驱动中申请内核空间内存，所以我们只要手写open，
 * mmap就可以轻松突破这个限制
 *
 * 3.binder的架构（Application、ServiceManager、系统Service、binder驱动），以获取系统服务的过程举例分析。
 * 4.Application#onCreate里面可以使用binder服务么(可以)？Application的binder机制是何时启动的
 *  （zygote在fork好应用进程后，会给应用启动binder机制）？binder机制启动的几个关键步骤。
 * 5.binder线程池默认最大数量（15）？
 * 6.binder和AIDL。https://www.yuque.com/guoquanliu/big9q9/xm6llb
 * Android Bander设计与实现 - 设计篇 https://blog.csdn.net/universus/article/details/6211589
 * 7.oneway。https://mp.weixin.qq.com/s/Jc2mrxeMVTJXudoPx5K4-w
 *   异步调用和串行化处理（重点）
 * 8.为什么SystemServer进程与Zygote进程通讯采用Socket而不是Binder
 * 1.fork出来的子进程可能会死锁.请不要,在不能把握问题的原委的情况下就在多线程程序里fork子进程.
 * 以下是说明死锁的理由：
 * 一般的,fork做如下事情
 *    1. 父进程的内存数据会原封不动的拷贝到子进程中
 *    2. 子进程在单线程状态下被生成
 * 2.怕父进程binder线程有锁，然后子进程的主线程一直在等其子线程(从父进程拷贝过来的子进程)的资源，但是其实父进程的子进程并没
 * 有被拷贝过来，造成死锁，所以fork不允许存在多线程。而非常巧的是Binder通讯偏偏就是多线程，所以干脆父进程（Zgote）这个时候就不使用binder线程
 */
public class ProcessActivity extends AppCompatActivity {
    private final static String TAG = "BBBBB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        //SystemUtil.printProcessName(this,TAG);
        initIntent();
        startThread();
    }

    private void initIntent() {
        Intent intent = getIntent();
        int age = intent.getIntExtra(IntentConstant.Name, 0);
        Log.d(TAG,"age="+age);
    }
    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        Log.d(TAG, "age=" + MainActivity.age);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
