package com.kui.desktopschedule;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 广播发送服务线程
 * Created by Administrator on 2017/3/14.
 */

public class ReceiverService extends Service {
    static Context context;
    private Thread thread = null;
    // 覆盖基类的抽象方法
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // 在本服务创建时将监听系统时间的BroadcastReceiver注册
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK); // 时间的流逝
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED); // 时间被改变，人为设置时间
        registerReceiver(boroadcastReceiver, intentFilter);
        thread=new Thread(){
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        context.sendBroadcast(new Intent(WidgetProvider.broadCastString));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!thread.isAlive()) {
            thread.start();
        }
        return START_STICKY;
    }

    // 在服务停止时解注册BroadcastReceiver
    @Override
    public void onDestroy() {
        unregisterReceiver(boroadcastReceiver);
        super.onDestroy();
    }

    // 用于监听系统时间变化Intent.ACTION_TIME_TICK的BroadcastReceiver，此BroadcastReceiver须为动态注册
    private BroadcastReceiver boroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!thread.isAlive()) {
                thread.start();
            }
        }
    };
}
