package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class aaa {


    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("change_screen")
    private Change_screenEntity change_screen;
    @Expose
    @SerializedName("app_id")
    private int app_id;
    @Expose
    @SerializedName("action_directive")
    private Action_directiveEntity action_directive;

    public static class Change_screenEntity {
        @Expose
        @SerializedName("sub_screen")
        private String sub_screen;
        @Expose
        @SerializedName("main_screen")
        private String main_screen;
    }

    public static class Action_directiveEntity {
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("speech")
        private List<String> speech;
    }
}
