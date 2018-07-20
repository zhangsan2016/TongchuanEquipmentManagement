package example.ldgd.com.tongchuanequipmentmanagement.acitvity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.ldgd.com.tongchuanequipmentmanagement.R;
import example.ldgd.com.tongchuanequipmentmanagement.adapter.DeviceAdapter;
import example.ldgd.com.tongchuanequipmentmanagement.entity.Alarm;
import example.ldgd.com.tongchuanequipmentmanagement.entity.DeviceParameter;
import example.ldgd.com.tongchuanequipmentmanagement.service.OnlineService;
import example.ldgd.com.tongchuanequipmentmanagement.utils.LogUtil;
import example.ldgd.com.tongchuanequipmentmanagement.utils.UrlUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int SHOW_DATA = 10;
    private ListView mListView;
    private OkHttpClient client;

    private ArrayList<DeviceParameter> deviceParameters = new ArrayList<>();
    private DeviceAdapter adapter;

    private Handler upHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SHOW_DATA:

                    //  显示数据
                    showData();

                    break;
            }

        }
    };
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用toolbar
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.collapseActionView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        getDataFromNet();

        setListener();

        initData();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_setting:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initData() {
        Alarm alarm = new Alarm();
        alarm.setAlarmIp("192.168.1.11");
        alarm.setAlarmTime(System.currentTimeMillis() + "");
        alarm.setNotes("报警，有异常");
        alarm.save();


        Alarm alarm2 = new Alarm();
        alarm2.setAlarmIp("192.168.1.95");
        alarm2.setAlarmTime(System.currentTimeMillis() + "");
        alarm2.setNotes("报警，有异常2");
        alarm2.save();


        Alarm alarm3 = new Alarm();
        alarm3.setAlarmIp("192.168.1.13");
        alarm3.setAlarmTime(System.currentTimeMillis() + "");
        alarm3.setNotes("报警，有异常3");
        alarm3.save();
    }

    private void setListener() {
        mListView.setOnItemClickListener(new MyOnItemClickListener());

        // 发送心跳包
        Intent intent = new Intent(MainActivity.this, OnlineService.class);
        startService(intent);
    }


    private void initView() {
        mListView = (ListView) this.findViewById(R.id.lv_device);

    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(MainActivity.this, "位置 = " + i, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 从网络获取设备信息
     */
    private void getDataFromNet() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    if (client == null) {
                        client = new OkHttpClient();
                    }

                    Request request = new Request.Builder()
                            .url(UrlUtil.DeviceParameterUrl)
                            .build();
                    response = client.newCall(request).execute();
                    String jsonString = response.body().string();
                    processData(jsonString);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 处理数据
     *
     * @param json
     */
    private void processData(String json) {
        // 解析json
        parseJson(json);

        // 显示数据
        upHandler.sendEmptyMessage(SHOW_DATA);

    }

    private void showData() {
        if (deviceParameters != null && deviceParameters.size() > 0) {
            //设置适配器
            adapter = new DeviceAdapter(this, deviceParameters);
            //进行ListView数据排序最关键的一个方法


            mListView.setAdapter(adapter);

            LogUtil.e("deviceParameters = " + deviceParameters.size());

        } else {
            Toast.makeText(MainActivity.this, "设备为空！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 解析json
     *
     * @param json
     */
    private void parseJson(String json) {

        Gson gson = new Gson();
        json = json.substring(6, json.length() - 1);
        deviceParameters = gson.fromJson(json, new TypeToken<List<DeviceParameter>>() {
        }.getType());

         /*   // 去掉{}第一个和最后一个
        json = json.substring(6, json.length() - 1);
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {



                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LogUtil.e("jsonObject = " + jsonObject.toString());

                String volt = jsonObject.optString("volt");
                String name = jsonObject.optString("name");
                String version = jsonObject.optString("version");

             *//*   {"volt":"0.79","hum":"32.80","pressure":"93.60","gataway":"192.168.2.1","updateTime":"17\/12\/9","creamSecPort":"8899","creamFirIp":"192.168.39.25","versionF":"0","hexMac":"6237A682797C","waterValue":"0.0","pvc":"0.0","isCreamFirstOpen":"1","creamFirPort":"8899","deviceIp":"192.168.39.114","ampere":"0.00","submask":"255.255.0.0","versionT":"5","temp":"32","power":"0.00","pm":"0.10","isCreamSecOpen":"1","lux":"0.00","versionS":"1","creamFSecIp":"192.168.39.26"}*//*

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/


    }


}
