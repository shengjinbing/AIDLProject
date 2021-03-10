package com.example.lx.aidldemo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lx.aidldemo.R;

/**
 * 任务栈？返回栈？启动模式？
 * https://mp.weixin.qq.com/s/9fwQscSZ10pFdmi7bUFaaA（重要）
 *
 * 1、standard:这个是android默认的Activity启动模式，每启动一个Activity都会被实例化一个Activity，并且新创建的Activity在堆栈中会在栈顶。
 *
 * 2、singleTop:如果当前要启动的Activity就是在栈顶的位置，那么此时就会复用该Activity，并且不会重走onCreate方法，会直接它的onNewIntent方法，
 * 如果不在栈顶，就跟standard一样的。
 * 应用场景：
 * 1.如果当前activity已经在前台显示着，突然来了一条推送消息，此时不想让接收推送的消息的activity再次创建，
 * 那么此时正好可以用该启动模式，如果之前activity栈中是A-->B-->C如果点击了推动的消息还是A-->B--C，不过此时C是不会再次创建的，而是调用C的
 * onNewIntent。而如果现在activity中栈是A-->C-->B，再次打开推送的消息，此时跟正常的启动C就没啥区别了，当前栈中就是A-->C-->B-->C了。
 *
 *
 * 3、singleTask:该种情况下就比singleTop厉害了，不管在不在栈顶，在Activity的堆栈中永远保持一个。
 * 应用场景：
 * 1.这种启动模式相对于singleTop而言是更加直接，比如之前activity栈中有A-->B-->C---D，再次打开了B的时候，在B上面
 * 的activity都会从activity栈中被移除。下面的acitivity还是不用管，所以此时栈中是A-->B，一般项目中主页面Home用到该启动模式。
 *
 * 4、singleInstance:该种情况就用得比较少了，主要是指在该activity永远只在一个单独的栈中。一旦该模式的activity的实例已经存在于某个栈中，
 * 任何应用在激活该activity时都会重用该栈中的实例，解决了多个task共享一个activity。其余的基本和上面的singleTask保持一致。
 *
 *
 *
 *
 * 5、上面的各种启动模式主要是通过配置清单文件，常见还有在代码中设置flag也能实现上面的功能：
 * 常用的flag有
 * FLAG_ACTIVITY_CLEAR_TOP、
 * FLAG_ACTIVITY_NEW_TASK、
 * FLAG_ACTIVITY_SINGLE_TOP
 * FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS：当某些情况下我们不希望用户通过历史列表回到 Activity 时，此标记位便体现了它的效果。
 *
 * 1.FLAG_ACTIVITY_CLEAR_TOP：
 * 这种启动的话，只能单纯地清空栈上面的acivity，而自己会重新被创建一次，如果当前栈中有A-->B-->C这几种情况，
 * 重新打开B之后，此时栈会变成了A-->B，但是此时B会被重新创建，不会走B的onNewIntent方法。这就是单独使用FLAG_ACTIVITY_CLEAR_TOP的用处
 * ，能清空栈上面的activity，但是自己会重新创建。如果在上面的基础上再加上FLAG_ACTIVITY_SINGLE_TOP此时就不重新创建B了，
 * 也就直接走B的onNewIntent。它两者结合着使用就相当于上面的singleTask模式。如果只是单独的使用FLAG_ACTIVITY_SINGLE_TOP
 * 跟上面的singleTop就没啥区别了。
 * 重点总结：FLAG_ACTIVITY_CLEAR_TOP+FLAG_ACTIVITY_SINGLE_TOP=singleTask,此时要打开的activity不会被重建，只是走onNewIntent方法。
 * FLAG_ACTIVITY_SINGLE_TOP=singleTop
 *
 *
 * 2.FLAG_ACTIVITY_NEW_TASK(和singleTask没啥关系)
 * 在相同taskAffinity情况下：启动activity是没有任何作用的。(重点)
 * 在不同taskAffinity情况下：
 * 如果启动不同栈中的activity已经存在了某一个栈中的activity，那么此时是启动不了该activity的（重点），因为栈中已经存在了该activity；
 * 如果栈中不存在该要启动的activity，那么会启动该acvitity，并且将该activity放入该栈中。
 *
 * FLAG_ACTIVITY_NEW_TASK和FLAG_ACTIVITY_CLEAR_TOP一起使用，并且要启动的activity的taskAffinity和当前activity
 * 的taskAffinity不一样才会和singleTask一样的效果，因为要启动的activity和原先的activity不在同一个taskAffinity中，
 * 所以能启动该activity，这个地方有点绕，写个简单的公式:
 *
 * FLAG_ACTIVITY_NEW_TASK如果启动同一个不同taskAffinity的activity才会有效果。
 * FLAG_ACTIVITY_NEW_TASK和FLAG_ACTIVITY_CLEAR_TOP如果一起使用要开启的activity和现在的activity处于同一个
 * taskAffinity，那么效果还是跟没加FLAG_ACTIVITY_NEW_TASK是一样的效果。FLAG_ACTIVITY_NEW_TASK和
 * FLAG_ACTIVITY_CLEAR_TOP启动和现在的activity不是同一个taskAffinity才会和singleTask一样的效果。(重点)
 *
 * FLAG_ACTIVITY_CLEAR_TASK
 * 在相同taskAffinity情况下：和FLAG_ACTIVITY_NEW_TASK一起使用，启动activity是没有任何作用的。
 * 在不同taskAffinity情况下：和FLAG_ACTIVITY_NEW_TASK一起使用，如果要启动的activity不存在栈中，那么启动该acitivity，
 * 并且将该activity放入该栈中，如果该activity已经存在于该栈中，那么会把当前栈中的activity先移除掉，然后再将该activity放入新的栈中。
 *
 * FLAG_ACTIVITY_NEW_TASK+FLAG_ACTIVITY_SINGLE_TOP用在当app正在运行点击push消息进到某个activity中的时候，
 * 如果当前处于该activity，此时会触发activity的onNewIntent。
 * FLAG_ACTIVITY_NEW_TASK+FLAG_ACTIVITY_CLEAR_TOP用在app没在运行中，启动主页的activity，然后在相应的activity
 * 中做相应的activity跳转。
 *
 */
public class AActivity extends AppCompatActivity {

    public static final String TAG = "AACTIVITY_LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Log.d(TAG,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
       /* for (int i = 0; i < 8; i++) {
            try {
                Log.d(TAG,i+"onPause");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        /*for (int i = 0; i < 8; i++) {
            try {
                Log.d(TAG,i+"onStop");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult"+"requestCode=="+requestCode+"resultCode=="+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startBActivity(View view) {
        Intent intent = new Intent(this, BActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"onNewIntent");
    }
}
