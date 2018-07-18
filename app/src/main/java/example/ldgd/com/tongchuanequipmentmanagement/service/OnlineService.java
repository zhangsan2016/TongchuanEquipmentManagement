package example.ldgd.com.tongchuanequipmentmanagement.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

import org.ddpush.im.v1.client.appuser.Message;
import org.ddpush.im.v1.client.appuser.UDPClientBase;

import java.util.Arrays;

import example.ldgd.com.tongchuanequipmentmanagement.R;
import example.ldgd.com.tongchuanequipmentmanagement.utils.LogUtil;
import example.ldgd.com.tongchuanequipmentmanagement.utils.Util;

public class OnlineService extends Service {
    WakeLock wakeLock;
    MyUdpClient myUdpClient;
    private final String TAG_REQUEST = "MY_TAG";
    private SharedPreferences preferences;
    byte[] uuid = new byte[]{1, 2, 1, 2, 1, 2, 1, 3};

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


}
