package com.example.hardapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Reminder {
    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String content;
    @Expose
    private String date;

    @Expose
    private String time;

    @Expose
    private AlarmItem item;

    public Reminder() {
    }

    public AlarmItem getAlarm() {
        return item;
    }
    public void setAlarm(AlarmItem item){
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
}
