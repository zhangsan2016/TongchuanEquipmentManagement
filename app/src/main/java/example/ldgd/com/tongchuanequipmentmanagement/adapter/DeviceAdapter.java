package example.ldgd.com.tongchuanequipmentmanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.ldgd.com.tongchuanequipmentmanagement.R;
import example.ldgd.com.tongchuanequipmentmanagement.entity.DeviceParameter;

/**
 * Created by ldgd on 2018/7/14.
 * 功能：
 * 说明：
 */
public class DeviceAdapter extends BaseAdapter {


    private final Context mContext;
    private final List<DeviceParameter> deviceParameters;

    public DeviceAdapter(Context context, List<DeviceParameter> deviceParameters) {
        this.mContext = context;
        this.deviceParameters = deviceParameters;
    }

    @Override
    public int getCount() {
        return deviceParameters.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceParameters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHoder viewHoder = null;

        if (view == null) {
            viewHoder = new ViewHoder();
            view = View.inflate(mContext, R.layout.item_device, null);

            viewHoder.tv_volt = view.findViewById(R.id.tv_volt);
            viewHoder.tv_ampere = view.findViewById(R.id.tv_ampere);
            viewHoder.tv_power = view.findViewById(R.id.tv_power);

            viewHoder.tv_lux = view.findViewById(R.id.tv_lux);
            viewHoder.tv_temp = view.findViewById(R.id.tv_temp);
            viewHoder.tv_hum = view.findViewById(R.id.tv_hum);

            viewHoder.tv_pressure = view.findViewById(R.id.tv_pressure);
            viewHoder.tv_pm = view.findViewById(R.id.tv_pm);
            viewHoder.tv_waterValue = view.findViewById(R.id.tv_waterValue);

            viewHoder.btn_restart = view.findViewById(R.id.btn_restart);
            viewHoder.tv_ip = view.findViewById(R.id.tv_ip);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) view.getTag();
        }

        DeviceParameter deviceParameter = deviceParameters.get(position);
        viewHoder.tv_volt.setText(deviceParameter.getVolt() + "V");
        viewHoder.tv_ampere.setText(deviceParameter.getAmpere() + "A");
        viewHoder.tv_power.setText(deviceParameter.getPower() + "W");

        viewHoder.tv_lux.setText(deviceParameter.getLux());
        viewHoder.tv_temp.setText(deviceParameter.getTemp());
        viewHoder.tv_hum.setText(deviceParameter.getHum());

        viewHoder.tv_pressure.setText(deviceParameter.getPressure());
        viewHoder.tv_pm.setText(deviceParameter.getPm());
        viewHoder.tv_waterValue.setText(deviceParameter.getWaterValue());

        viewHoder.tv_ip.setText(deviceParameter.getDeviceIp());

        viewHoder.btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "btn_restart 被点击" + position, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private static class ViewHoder {

        public TextView tv_volt;  // 电压
        public TextView tv_ampere; // 电流
        public TextView tv_power; // 功率

        public TextView tv_lux;  // 光照
        public TextView tv_temp; // 温度
        public TextView tv_hum; // 湿度


        public TextView tv_pressure;  // 气压
        public TextView tv_pm; // PM有害气体浓度
        public TextView tv_waterValue; // 水位值

        public Button btn_restart;  // 重启
        public TextView tv_ip; // ip地址

    }

}
