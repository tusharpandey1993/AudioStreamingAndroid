package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNextScheduledClassesRes {


    @Expose
    @SerializedName("data")
    private DataEntity data;
    @Expose
    @SerializedName("status")
    private String status;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }
}
