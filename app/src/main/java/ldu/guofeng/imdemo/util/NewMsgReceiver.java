package ldu.guofeng.imdemo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ldu.guofeng.imdemo.R;
import ldu.guofeng.imdemo.activity.MainActivity;
import ldu.guofeng.imdemo.base.Constant;
import ldu.guofeng.imdemo.base.IMApplication;

/**
 * 广播接收器
 * 作用：有新消息时，推送通知栏
 */

public class NewMsgReceiver extends BroadcastReceiver {

    private NotificationManager myManager = null;
    private Notification myNotification = null;
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String form = intent.getStringExtra("form");
        int type = intent.getIntExtra("type", 0);
        String t = null;
        if (type == Constant.MSG_TYPE_TEXT) {
            t = "文本消息";
        } else if (type == Constant.MSG_TYPE_LOC) {
            t = "位置消息";
        } else if (type == 0) {
            t = "未知消息";
        }
        String message = form + "发来" + t;
        //从系统服务中获得通知管理器
        myManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        //定义一个PendingIntent，点击Notification后启动一个Activity
        PendingIntent pi = PendingIntent.getActivity(
                context,
                100,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        //通过Notification.Builder来创建通知
        Notification.Builder myBuilder = new Notification.Builder(IMApplication.getMyAppContext());
        myBuilder.setContentTitle(IMApplication.getMyAppContext().getString(R.string.app_name))
                .setContentText(message)
                .setTicker("新消息")
                //设置状态栏中的小图片
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                .setContentIntent(pi);  //关联PendingIntent
        myNotification = myBuilder.build();
        //通过通知管理器来发起通知，ID区分通知
        myManager.notify(NOTIFICATION_ID, myNotification);
    }
}
