package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpressionKeyFrame {

    @SerializedName("id")
    public int id;

    @SerializedName("startTime")
    private long startTime;

    @SerializedName("endTime")
    private long endTime;

    @SerializedName("toDoTask")
    private List<String> toDoTask;

    @SerializedName("position")
    private int position;

    private int slotPosition;


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

    public List<String> getToDoTask() {
        return toDoTask;
    }

    public void setToDoTask(List<String> toDoTask) {
        this.toDoTask = toDoTask;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(int slotPosition) {
        this.slotPosition = slotPosition;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", toDoTask=" + toDoTask +
                ", position=" + position +
                ", slotPosition=" + slotPosition +
                '}';
    }
}
