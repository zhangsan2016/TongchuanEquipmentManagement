package example.ldgd.com.tongchuanequipmentmanagement.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by ldgd on 2018/7/18.
 * 功能：存储下位机传上来的报警信息
 * 说明：
 */

public class Alarm extends LitePalSupport {

    /**
     * 报警的ip地址
     */
    private String alarmIp;

    /**
     * 报警的时间
     */
    private String alarmTime;

    /**
     * 备注
     */
    private String notes;




    public String getAlarmIp() {
        return alarmIp;
    }

    public void setAlarmIp(String alarmIp) {
        this.alarmIp = alarmIp;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
