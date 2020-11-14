package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataEntity {
    @Expose
    @SerializedName("mikoCoachClasses")
    private List<MikoCoachClassesEntity> mikoCoachClasses;


    public List<MikoCoachClassesEntity> getMikoCoachClasses() {
        return mikoCoachClasses;
    }

    public void setMikoCoachClasses(List<MikoCoachClassesEntity> mikoCoachClasses) {
        this.mikoCoachClasses = mikoCoachClasses;
    }

    @Override
    public String toString() {
        return "{" +
                "mikoCoachClasses=" + mikoCoachClasses +
                '}';
    }
}
