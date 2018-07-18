package example.ldgd.com.tongchuanequipmentmanagement.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.ddpush.im.v1.client.appuser.Message;
import org.ddpush.im.v1.client.appuser.UDPClientBase;

import java.util.Arrays;

import example.ldgd.com.tongchuanequipmentmanagement.R;
import example.ldgd.com.tongchuanequipmentmanagement.acitvity.MainActivity;
import example.ldgd.com.tongchuanequipmentmanagement.utils.LogUtil;
import example.ldgd.com.tongchuanequipmentmanagement.utils.Util;

public class OnlineService extends Service {

    WakeLock wakeLock;
    MyUdpClient myUdpClient;
    private final String TAG_REQUEST = "MY_TAG";
    private SharedPreferences preferences;
    byte[] uuid = new byte[]{1, 2, 1, 2, 1, 2, 1, 3};

    private int notificationId = 1;

    public class MyUdpClient extends UDPClientBase {

        public MyUdpClient(byte[] uuid, int appid, String serverAddr,
                           int serverPort) throws Exception {
            super(uuid, appid, serverAddr, serverPort);
        }

        @Override
        public boolean hasNetworkConnection() {
            return Util.hasNetwork(OnlineService.this);
        }

        @Override
        public void trySystemSleep() {
            tryReleaseWakeLock();
        }

        @Override
        public void onPushMessage(Message message) {
            // 测试
            System.out.println("message = " + Arrays.toString(message.getData()));
            LogUtil.e("message = " + Arrays.toString(message.getData()));

            if (message == null) {
                return;
            }
            if (message.getData() == null || message.getData().length == 0) {
                return;
            }

            getNotificationManager().notify(notificationId++, getNotification("报警...", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub


        super.onCreate();

    }

    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        resetClient();
        return super.onStartCommand(intent, flags, startId);
    }

    protected void resetClient() {
        if (this.myUdpClient != null) {
            try {
                myUdpClient.stop();
            } catch (Exception e) {
            }
        }
        try {

            myUdpClient = new MyUdpClient(uuid, 1, this.getResources()
                    .getString(R.string.ip), 9966);
            myUdpClient.setHeartbeatInterval(50);
            myUdpClient.start();
            LogUtil.e("心跳包开启成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(),
                    "开启心跳包异常：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void tryReleaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld() == true) {
            wakeLock.release();
        }
    }


    /**
     * 获取NotificationManager
     *
     * @return
     */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 获取Notification
     *
     * @param title
     * @return
     */
    private Notification getNotification(String title, String sting) {


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.th_birthday);
        builder.setTicker("铜川报警信息");//设置提示
        builder.setContentText(sting);  // 设置内容
        builder.setWhen(System.currentTimeMillis());//设置展示时间
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.th_birthday));
        builder.setContentIntent(pi);
        //  builder.setContent(remoteViews); // 通过设置RemoteViews对象来设置通知的布局，这里我们设置为自定义布局
        builder.setContentTitle(title);

        // 设置点击事件
        Intent cancelReceive = new Intent();
        cancelReceive.setAction(AlarmReceiver.CANCE_ACTION);
        Bundle cancelBundle = new Bundle();
        cancelBundle.putInt("userAnswer", 66);//This is the value I want to pass
        cancelReceive.putExtras(cancelBundle);
        PendingIntent pendingIntentCance = PendingIntent.getBroadcast(this, 12345, cancelReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent neglectReceive = new Intent();
        neglectReceive.setAction(AlarmReceiver.NEGLECT_ACTION);
        PendingIntent pendingIntentNeglect = PendingIntent.getBroadcast(this, 12345, neglectReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent closeReceive = new Intent();
        closeReceive.setAction(AlarmReceiver.CLOSE_ACTION);
        PendingIntent pendingIntentClose = PendingIntent.getBroadcast(this, 12345, closeReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(0, "取消", pendingIntentCance);
        builder.addAction(0, "忽略", pendingIntentNeglect);
        builder.addAction(0, "确定", pendingIntentClose);


        return builder.build();

    }




    public static class AlarmReceiver extends BroadcastReceiver {

        private static final String CANCE_ACTION = "CANCE_ACTION";
        private static final String NEGLECT_ACTION = "NEGLECT_ACTION";
        private static final String CLOSE_ACTION = "CLOSE_ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.e("shuffTest" + "I Arrived!!!");
            //Toast.makeText(context, "Alarm worked!!", Toast.LENGTH_LONG).show();

            int userAnswer = intent.getIntExtra("userAnswer", 0);
            LogUtil.e("shuffTest=" + userAnswer);

            String action = intent.getAction();

            if (CANCE_ACTION.equals(action)) {
                Log.v("shuffTest", "Pressed YES");
            } else if (NEGLECT_ACTION.equals(action)) {
                Log.v("shuffTest", "Pressed NO");
            } else if (CLOSE_ACTION.equals(action)) {
                Log.v("shuffTest", "Pressed MAYBE");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
