package com.example.lx.socketclient;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 1.TCP建立连接
 * 必须进行三次握手：若A要与B进行连接，则必须
 * 第一次握手：建立连接。客户端发送连接请求报文段，将SYN位置为1，Sequence Number为x；然后，客户端进入SYN_SEND状态，等待服务器的确认。即A发送信息给B
 * 第二次握手：服务器收到客户端的SYN报文段，需要对这个SYN报文段进行确认。即B收到连接信息后向A返回确认信息
 * 第三次握手：客户端收到服务器的（SYN+ACK）报文段，并向服务器发送ACK报文段。即A收到确认信息后再次向B返回确认连接信息
 *
 * 2.TCP释放连接
 * TCP释放连接需要四次挥手过程，现在假设A主动释放连接：（数据传输结束后，通信的双方都可释放连接）
 * 第一次挥手：A发送释放信息到B；（发出去之后，A->B发送数据这条路径就断了）
 * 第二次挥手：B收到A的释放信息之后，回复确认释放的信息：我同意你的释放连接请求
 * 第三次挥手：B发送“请求释放连接“信息给A
 * 第四次挥手：A收到B发送的信息后向B发送确认释放信息：我同意你的释放连接请求
 *
 * 1.TCP和UDP
 * 流套接字（streamsocket） ：基于 TCP协议，采用 流的方式 提供可靠的字节流服务
 * 数据报套接字(datagramsocket)：基于 UDP协议，采用 数据报文 提供数据打包发送的服务
 */
public class TCPClientActivity extends Activity implements OnClickListener {

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditText;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_RECEIVE_NEW_MSG: {
                mMessageTextView.setText(mMessageTextView.getText()
                        + (String) msg.obj);
                break;
            }
            case MESSAGE_SOCKET_CONNECTED: {
                mSendButton.setEnabled(true);
                break;
            }
            default:
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        mMessageTextView = (TextView) findViewById(R.id.msg_container);
        mSendButton = (Button) findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
        mMessageEditText = (EditText) findViewById(R.id.msg);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mSendButton) {
            final String msg = mMessageEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                mPrintWriter.println(msg);
                mMessageEditText.setText("");
                String time = formatDateTime(System.currentTimeMillis());
                final String showedMsg = "self " + time + ":" + msg + "\n";
                mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String formatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("192.168.199.110", 8989);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed, retry...");
            }
        }

        try {
            // 接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                System.out.println("receive :" + msg);
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "server " + time + ":" + msg
                            + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg)
                            .sendToTarget();
                }
            }
            System.out.println("quit...");
            mPrintWriter.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startService(View view) {
        Intent service = new Intent(this,TCPServerService.class);
       startService(service);
    }
}
