package edu.niu.cs.z1888485.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "myAlarm")
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "alarmMessage")
    String alarmMessage;
    @ColumnInfo(name = "alarmDate")
    String alarmDate;
    @ColumnInfo(name = "alarmTime")
    String alarmTime;

    @ColumnInfo(name = "voiceMessage")
    String voiceMessage;
    @ColumnInfo(name = "alarmActive")
    boolean alarmActive;
    @ColumnInfo(name = "alarmRepeat")
    String alarmRepeat;


    public Alarm(String alarmMessage, String alarmDate, String alarmTime, String voiceMessage, boolean alarmActive , String alarmRepeat ) {
        this.alarmMessage = alarmMessage;
        this.alarmDate = alarmDate;
        this.alarmTime = alarmTime;
        this.voiceMessage = voiceMessage;
        this.alarmActive = alarmActive;
        this.alarmRepeat = alarmRepeat;

    }

    public String getAlarmRepeat() {
        return alarmRepeat;
    }

    public void setAlarmRepeat(String alarmRepeat) {
        this.alarmRepeat = alarmRepeat;
    }

    public boolean isAlarmActive()
    {
        return alarmActive;
    }

    public void setAlarmActive(boolean alarmActive) {
        this.alarmActive = alarmActive;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }



    public String getAlarmDate() {
        return alarmDate;
    }



    public String getAlarmTime() {
        return alarmTime;
    }

    
    public String getVoiceMessage() {
        return voiceMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
