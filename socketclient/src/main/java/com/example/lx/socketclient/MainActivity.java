package com.example.lx.socketclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * （6）标志位：指数据包的属性，用于控制TCP的状态机；
 * URG: 表示报文段是否包含紧急数据，当URG=1时才表示有紧急数据；
 * ACK: 表示报文段确认序号是否有效，当ACK=1才表示有效，而TCP建立连接之后，发送报文的标志位必须是ACK=1；
 * PSH: 用于告知接收方是否需要立即将数据传递给上层，当PSH=1生效，否则就缓存起来；
 * RST: 当RST=1，表示当前链接出现了不可知的错误，需要重新建立连接以确保正常的通讯；
 * SYN: 在建立连接时使用，当SYN=1时表示用于请求建立连接或者相应建立连接，用于TCP握手使用；
 * FIN: 用于标记数据是否发送完毕，当FIN=1时生效
 *
 * 作者：Amter
 * 链接：https://juejin.cn/post/6844904158655414286
 * 来源：掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
