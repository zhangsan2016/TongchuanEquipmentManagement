package example.ldgd.com.tongchuanequipmentmanagement.acitvity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        getDataFromNet();

        setListener();


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
