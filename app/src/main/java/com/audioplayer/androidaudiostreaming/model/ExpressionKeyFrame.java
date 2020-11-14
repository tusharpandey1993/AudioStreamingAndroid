package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.SerializedName;

public class ExpressionKeyFrame {

    @SerializedName("id")
    public int id;

    @SerializedName("startTime")
    private long startTime;

    @SerializedName("endTime")
    private long endTime;

    @SerializedName("toDoTask")
    private String toDoTask;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getToDoTask() {
        return toDoTask;
    }

    public void setToDoTask(String toDoTask) {
        this.toDoTask = toDoTask;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", toDoTask='" + toDoTask + '\'' +
                '}';
    }
}
