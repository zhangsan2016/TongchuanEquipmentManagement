package example.ldgd.com.tongchuanequipmentmanagement.appliction;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by ldgd on 2018/6/12.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);

    }
}
