package com.audioplayer.androidaudiostreaming.model;


public class ExpressionMsg {

    public AxStreamModel axStreamModel;

    public AxStreamModel getAxStreamModel() {
        return axStreamModel;
    }

    public void setAxStreamModel(AxStreamModel axStreamModel) {
        this.axStreamModel = axStreamModel;
    }

    @Override
    public String toString() {
        return "ExpressionMsg{" +
                "axStreamModel=" + axStreamModel +
                '}';
    }
}
