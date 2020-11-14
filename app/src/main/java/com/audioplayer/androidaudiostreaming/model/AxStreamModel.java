package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AxStreamModel {

    @SerializedName("type ")
    private Integer type;

    @SerializedName("size ")
    private Integer size;

    @SerializedName("loop ")
    private Integer loop;

    @SerializedName("seqCount ")
    private Integer seqCount;

    @SerializedName("seq ")
    private ArrayList<MediaMetaData> seq = null;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getLoop() {
        return loop;
    }

    public void setLoop(Integer loop) {
        this.loop = loop;
    }

    public Integer getSeqCount() {
        return seqCount;
    }

    public void setSeqCount(Integer seqCount) {
        this.seqCount = seqCount;
    }

    public ArrayList<MediaMetaData> getSeq() {
        return seq;
    }

    public void setSeq(ArrayList<MediaMetaData> seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "AxStreamModel{" +
                "type=" + type +
                ", size=" + size +
                ", loop=" + loop +
                ", seqCount=" + seqCount +
                ", seq=" + seq +
                '}';
    }
}