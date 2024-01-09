package com.example.hardapplication;

import java.time.LocalDateTime;

public class AlarmItem {
    public LocalDateTime time;
    public int id;
    public String title;

    public AlarmItem(LocalDateTime time, int id, String title){
        this.id = id;
        this.time = time;
        this.title = title;
    }
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public LocalDateTime getTime(){
        return time;
    }

}
