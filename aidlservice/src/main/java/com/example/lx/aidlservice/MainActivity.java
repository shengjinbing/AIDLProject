package com.example.lx.aidlservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * AIDL的语法十分简单，与Java语言基本保持一致，需要记住的规则有以下几点：
 1.AIDL文件以 .aidl 为后缀名
 2.AIDL支持的数据类型分为如下几种：
 3.八种基本数据类型：byte、char、short、int、long、float、double、boolean
 4.String，CharSequence
 5.实现了Parcelable接口的数据类型
 6.List 类型。List承载的数据必须是AIDL支持的类型，或者是其它声明的AIDL对象
 7.Map类型。Map承载的数据必须是AIDL支持的类型，或者是其它声明的AIDL对象

 AIDL文件可以分为两类。一类用来声明实现了Parcelable接口的数据类型，以供其他AIDL文件使用那些非默认支持的数据类型。
 还有一类是用来定义接口方法，声明要暴露哪些接口给客户端调用，定向Tag就是用来标注这些方法的参数值
 定向Tag。定向Tag表示在跨进程通信中数据的流向，用于标注方法的参数值，分为 in、out、inout 三种。
 其中 in 表示数据只能由客户端流向服务端， out 表示数据只能由服务端流向客户端，
 而 inout 则表示数据可在服务端与客户端之间双向流通。此外，如果AIDL方法接口的参数值类型是：
 基本数据类型、String、CharSequence或者其他AIDL文件定义的方法接口，那么这些参数值的定向 Tag 默认是
 且只能是 in，所以除了这些类型外，其他参数值都需要明确标注使用哪种定向Tag。定向Tag具体的使用差别后边会有介绍
 明确导包。在AIDL文件中需要明确标明引用到的数据类型所在的包名，即使两个文件处在同个包名下。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
