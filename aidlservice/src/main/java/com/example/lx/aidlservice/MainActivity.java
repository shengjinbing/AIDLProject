package com.example.lx.aidlservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lx.aidlservice.services.TestService;

/**
 * AIDL的语法十分简单，与Java语言基本保持一致，需要记住的规则有以下几点：
 * 1.AIDL文件以 .aidl 为后缀名
 * 2.AIDL支持的数据类型分为如下几种：
 * 3.八种基本数据类型：byte、char、short、int、long、float、double、boolean
 * 4.String，CharSequence
 * 5.实现了Parcelable接口的数据类型
 * 6.List 类型。List承载的数据必须是AIDL支持的类型，或者是其它声明的AIDL对象
 * 7.Map类型。Map承载的数据必须是AIDL支持的类型，或者是其它声明的AIDL对象
 * <p>
 * AIDL文件可以分为两类。一类用来声明实现了Parcelable接口的数据类型，以供其他AIDL文件使用那些非默认支持的数据类型。
 * 还有一类是用来定义接口方法，声明要暴露哪些接口给客户端调用，定向Tag就是用来标注这些方法的参数值
 * 定向Tag。定向Tag表示在跨进程通信中数据的流向，用于标注方法的参数值，分为 in、out、inout 三种。
 * 其中 in 表示数据只能由客户端流向服务端， out 表示数据只能由服务端流向客户端，
 * 而 inout 则表示数据可在服务端与客户端之间双向流通。此外，如果AIDL方法接口的参数值类型是：
 * 基本数据类型、String、CharSequence或者其他AIDL文件定义的方法接口，那么这些参数值的定向 Tag 默认是
 * 且只能是 in，所以除了这些类型外，其他参数值都需要明确标注使用哪种定向Tag。定向Tag具体的使用差别后边会有介绍
 * 明确导包。在AIDL文件中需要明确标明引用到的数据类型所在的包名，即使两个文件处在同个包名下。
 *
 *
 *
 * 分析服务源码：
 *
 * 开启过程
 * 1.通过ContextImpl中的startServiceCommon()方法开启服务
 *   ActivityManager.getService().startService()
 * 2.在AMS中通过ActiveServices这个辅助AMS进行service管理的类，包括service的启动、绑定和停止等方法。
 * 3.接下来跟Activity的启动过程一样，交给app.thread是IApplicationThread类型是ApplicationThread的实现类，
 *    app.thread.scheduleCreateService()，所以接下来启动过程交给ApplicationThread类
 * 4.sendMessage消息给ActivityThread的H，处理消息，首先通过类加载器创建Service的实例
 * 5.然后创建Application对象并且调用onCreate方法，当然Application对象只创建一次。
 * 6.创建ConTextImpl对象并通过attach方法建立二者的关系。
 * 7.最后调用Service的Oncreate的方法，然后通过handleServiceArgs方法调用Service的onStartCommand()方法。
 *
 * 绑定过程
 * 1.首先将ServiceConnection不能为空否则报异常，转化为ServiceDispatcher.InnerConnection（充当Binder角色）对象，之所以不能直接用
 *   ServiceConnection是因为服务的绑定有可能是跨进程的，因此ServiceConnection只要借助Binder才能让远程服务端回调自己
 *   的方法。这个过程由LoadedApk来完成。
 * 2. IBinder binder = s.onBind(data.intent);
 *   ActivityManager.getService().publishService(data.token, data.intent, binder);通过这个方法来告诉客户端
 *   已经成功连接Service了(回调此方法onServiceConnected)。（Service多次绑定onbinder方法只执行一次）
 *
 *
 *
 *
 * 开启服务和绑定服务涉及的各种情况
 * 1.多次startService会重复调用onStartCommand方法。
 * 2.不调用bindService方法直接调用unbindService方法，程序奔溃。
 * 3.多次调用bindService不进行任何方法调用。
 * 4.多次调用unbindService程序奔溃。
 * 5.先绑定服务再打开服务直接调用onStartCommand方法，之后直接调用stopservice不生效，调用unbindService方法回调
 *   onUnbind和onDestroy方法。
 * 6.混合开启服务时只有调用unbindService方法才能停止服务。（stopservice一直不生效不生效）
 */
public class MainActivity extends AppCompatActivity {

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(this, TestService.class);
    }

    /**
     * onStart已经过时被onStartCommand取代
     * 1.onCreate —》onStartCommand
     * 2.多次开启服务走onStartCommand方法
     * @param view
     */
    public void startService(View view) {
      startService(mIntent);
    }

    /**
     *
     * BIND_AUTO_CREATETA他的意思是什么呢，自定的建立create服务，这是你绑定多久，他都自动的去建立服务
     * ，也就是说大家想想一下，我们在这个调用的时候，他跟start不太一样，start的话实际上它相当于就是new 一
     * 个service，他会有我们类的这种方法，所以实际上我们的start，大家下来追踪以下代码就会知道我们调用，
     * startservice它实际上他是会通过我们activity去new一个service的实例这个大家要注意，但是我们用Bind
     * 去绑定服务，你知道一个是客户端一个是服务器端，特别是万一在不同的进程，你凭什么去new别人一个实例。
     * 所以这个实例是谁new的呢是服务它自己new的它自己，他自己实例化了自己，这是他门很大一区别，所以这个绑定
     * 说白了你就是发了一个请求，当它接收到了请求了，谁接收啊，就是我们的serviceMessenger，来接收，接收到了
     * 这样的请求之后，他就会考虑自己要不要去把这个服务实例化，所以大家要注意这点，那好我们就用默认的参数。
     * @param view
     */
    public void bindService(View view) {
        //1.onCreate —》onBind  —》（onServiceConnected）
        //2、多次调用bindService，服务本身未执行任何操作。
        //BIND_AUTO_CREATE 只要绑定存在，就自动建立
        bindService(mIntent,mServiceConnection ,BIND_AUTO_CREATE);
    }

    /**
     * onUnbind —》onDestroy
     * 1.所以一次unBindService就能结束服务。（若多次调用unBindService，第一次有用，后面会出错）
     */
    public void unbindService(View view) {
        unbindService(mServiceConnection);
    }

    /**
     * 正确步骤：
     * 1.开启服务 保证长期后台运行.
     *
     * 2.如果需要调用服务里面的方法 去绑定服务 调用方法 -> 一定要记得解除绑定服务(否则不能停止服务).
     *
     * 3.如果服务用完了 stopService()的方法 把服务给停止掉.
     * @param view
     */
    public void stopService(View view) {
        stopService(mIntent);
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
