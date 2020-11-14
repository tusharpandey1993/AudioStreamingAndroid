package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MikoCoachClassesEntity {
    @Expose
    @SerializedName("class_live_from")
    private String class_live_from;
    @Expose
    @SerializedName("class_zoom_URL")
    private String class_zoom_URL;
    @Expose
    @SerializedName("class_live_stream_URL")
    private String class_live_stream_URL;
    @Expose
    @SerializedName("class_end_time")
    private String class_end_time;
    @Expose
    @SerializedName("class_start_time")
    private String class_start_time;
    @Expose
    @SerializedName("class_tutor")
    private String class_tutor;
    @Expose
    @SerializedName("class_thumbnail_image_URL")
    private String class_thumbnail_image_URL;
    @Expose
    @SerializedName("class_name")
    private String class_name;
    @Expose
    @SerializedName("epoch_end_date")
    private String epoch_end_date;
    @Expose
    @SerializedName("epoch_start_date")
    private String epoch_start_date;
    @Expose
    @SerializedName("class_duration")
    private String class_duration;
    @Expose
    @SerializedName("parental_date")
    private String parental_date;
    @Expose
    @SerializedName("end_time")
    private String end_time;
    @Expose
    @SerializedName("start_time")
    private String start_time;
    @Expose
    @SerializedName("date")
    private String date;


    public String getClass_live_from() {
        return class_live_from;
    }

    public void setClass_live_from(String class_live_from) {
        this.class_live_from = class_live_from;
    }

    public String getClass_zoom_URL() {
        return class_zoom_URL;
    }

    public void setClass_zoom_URL(String class_zoom_URL) {
        this.class_zoom_URL = class_zoom_URL;
    }

    public String getClass_live_stream_URL() {
        return class_live_stream_URL;
    }

    public void setClass_live_stream_URL(String class_live_stream_URL) {
        this.class_live_stream_URL = class_live_stream_URL;
    }

    public String getClass_end_time() {
        return class_end_time;
    }

    public void setClass_end_time(String class_end_time) {
        this.class_end_time = class_end_time;
    }

    public String getClass_start_time() {
        return class_start_time;
    }

    public void setClass_start_time(String class_start_time) {
        this.class_start_time = class_start_time;
    }

    public String getClass_tutor() {
        return class_tutor;
    }

    public void setClass_tutor(String class_tutor) {
        this.class_tutor = class_tutor;
    }

    public String getClass_thumbnail_image_URL() {
        return class_thumbnail_image_URL;
    }

    public void setClass_thumbnail_image_URL(String class_thumbnail_image_URL) {
        this.class_thumbnail_image_URL = class_thumbnail_image_URL;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getEpoch_end_date() {
        return epoch_end_date;
    }

    public void setEpoch_end_date(String epoch_end_date) {
        this.epoch_end_date = epoch_end_date;
    }

    public String getEpoch_start_date() {
        return epoch_start_date;
    }

    public void setEpoch_start_date(String epoch_start_date) {
        this.epoch_start_date = epoch_start_date;
    }

    public String getClass_duration() {
        return class_duration;
    }

    public void setClass_duration(String class_duration) {
        this.class_duration = class_duration;
    }

    public String getParental_date() {
        return parental_date;
    }

    public void setParental_date(String parental_date) {
        this.parental_date = parental_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "class_live_from='" + class_live_from + '\'' +
                ", class_zoom_URL='" + class_zoom_URL + '\'' +
                ", class_live_stream_URL='" + class_live_stream_URL + '\'' +
                ", class_end_time='" + class_end_time + '\'' +
                ", class_start_time='" + class_start_time + '\'' +
                ", class_tutor='" + class_tutor + '\'' +
                ", class_thumbnail_image_URL='" + class_thumbnail_image_URL + '\'' +
                ", class_name='" + class_name + '\'' +
                ", epoch_end_date='" + epoch_end_date + '\'' +
                ", epoch_start_date='" + epoch_start_date + '\'' +
                ", class_duration='" + class_duration + '\'' +
                ", parental_date='" + parental_date + '\'' +
                ", end_time='" + end_time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
