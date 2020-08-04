package com.example.lx.aidldemo.ui.services

import android.app.IntentService
import android.content.Intent
import android.content.Context

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.example.lx.aidldemo.ui.services.action.FOO"
private const val ACTION_BAZ = "com.example.lx.aidldemo.ui.services.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.example.lx.aidldemo.ui.services.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.lx.aidldemo.ui.services.extra.PARAM2"

/**
 * IntentService是google在原生的Service基础上通过创建子线程的Service。也就是说IntentService是专门为android开
 * 发者提供的能在service内部实现耗时操作的service。我们可以通过重写onHandleIntent方法实现耗时操作的回调处理，
 * 而且IntentService在耗时操作完成后，会主动销毁自己，IntentService可以通过多次启动来完成多个任务，而IntentService
 * 只会被创建一次，每次启动的时候只会触发onStart方法。内部是实现了Handler异步处理耗时操作的过程，一般多用在Service中需要处理耗时操作的功能。
提问：为什么IntentService中能实现耗时操作?在onCreate中，通过HandlerThread来开启一条线程，而HandlerThread线程中
会跟我们平常用的Handler不太一样，在run方法中创建了looper对象，所以HandlerThread能让IntentService在子线程中使用handler达到耗时操作
 */
class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FOO -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionFoo(param1, param2)
            }
            ACTION_BAZ -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionBaz(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        TODO("Handle action Foo")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        TODO("Handle action Baz")
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_FOO
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, MyIntentService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }
}
