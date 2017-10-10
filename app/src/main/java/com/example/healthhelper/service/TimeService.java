package com.example.healthhelper.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.example.healthhelper.activity.MainActivity;
import com.example.healthhelper.R;
import com.example.healthhelper.function.WaterCounter;
import com.example.healthhelper.utils.PreUtils;
import com.example.healthhelper.utils.TimeUtil;

public class TimeService extends Service {
    //当前日期
    private static int CURRENT_DAY;
    //当前时间
    private static String CURRENT_TIME;
    //广播接收器
    private BroadcastReceiver mInfoReceiver;
    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private NotificationManager notificationManager2;
    private Intent nfIntent;
    private TimeBinder mBinder = new TimeBinder();
    private  Notification timeNotification;

    public TimeService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcastReveiver();
        CURRENT_DAY = PreUtils.getInt("user_data","day");
    }
    //新建一个类，用于对服务进行操作
    public class TimeBinder extends Binder {
        public void refreshDrink(){
            reloadData();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this.getApplicationContext());
        nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this,0,nfIntent,0))
                .setContentTitle("今天已经喝水: "+ PreUtils.getInt("user_data","drink_amount")+"ml")
                .setSmallIcon(R.mipmap.service)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.logo));
        if(WaterCounter.remainWater()!= 0)
            builder.setContentText("还需要喝：" + WaterCounter.remainWater()+"ml");
        else
            builder.setContentText("今日目标已达成~再接再厉哦！");
        timeNotification = builder.build();
        notificationManager.notify(110,timeNotification);
        startForeground(110,timeNotification);

        return START_STICKY;
    }

    private void initBroadcastReveiver(){
        final IntentFilter filter = new IntentFilter();
        //监听屏幕熄灭
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //监听关机
        filter.addAction(Intent.ACTION_SHUTDOWN);
        //监听屏幕解锁
        filter.addAction(Intent.ACTION_USER_PRESENT);
        //监听关机广播
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_ATTACH_DATA);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch(action){
                    case Intent.ACTION_SCREEN_OFF:
                        break;
                    case Intent.ACTION_SHUTDOWN:

                        break;
                    case Intent.ACTION_USER_PRESENT:
                        break;
                    case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:

                        break;
                    case Intent.ACTION_DATE_CHANGED:
                    case Intent.ACTION_TIME_CHANGED:
                    case Intent.ACTION_TIME_TICK:
                        isNewDay();
                        isTimeUp();
                        break;
                    default:
                        break;
                }
            }
        };
        registerReceiver(mInfoReceiver,filter);
    }
    private void isNewDay(){
        if(CURRENT_DAY != TimeUtil.getCurrentDay()){
            initTodayData();
            PreUtils.putInt("user_data","day",TimeUtil.getCurrentDay());
        }
    }

    private  void isTimeUp(){
        CURRENT_TIME = TimeUtil.getCurTime();
        if(CURRENT_TIME.equals("11:18")){
            notificationManager2 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification notification2 = new NotificationCompat.Builder(this)
                    .setContentTitle("吃药时间")
                    .setContentText("该吃药啦~")
                    .setVibrate(new long[]{0,1000})
                    .setSmallIcon(R.mipmap.capsule)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.logo))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            notificationManager2.notify(1,notification2);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //主界面手动调用stop才能结束service
        stopForeground(true);
        unregisterReceiver(mInfoReceiver);
    }

    private void initTodayData(){
        PreUtils.putInt("user_data","drink_amount",0);
    }

    private void reloadData(){
        builder.setContentIntent(PendingIntent.getActivity(this,0,nfIntent,0))
                .setContentTitle("今天已经喝水: "+ PreUtils.getInt("user_data","drink_amount")+"ml")
                .setSmallIcon(R.mipmap.service)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.logo));
        if(WaterCounter.remainWater()!= 0)
            builder.setContentText("还需要喝：" + WaterCounter.remainWater()+"ml");
        else
            builder.setContentText("今日目标已达成~再接再厉哦！");
        timeNotification = builder.build();
        notificationManager.notify(110,timeNotification);
    }
}
