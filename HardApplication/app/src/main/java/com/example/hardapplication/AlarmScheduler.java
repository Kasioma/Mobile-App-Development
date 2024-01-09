package com.example.hardapplication;


public interface AlarmScheduler {
    public void schedule(AlarmItem item);
    public void cancel(AlarmItem item);
}
