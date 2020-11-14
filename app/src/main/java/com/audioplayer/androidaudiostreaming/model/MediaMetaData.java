package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.SerializedName;

public class MediaMetaData {

    @SerializedName("site")
    private String mediaUrl;
    @SerializedName("offsetEnd")
    private String offsetEnd;
    @SerializedName("offsetStart")
    private String offsetStart;
    @SerializedName("totalTrackCount")
    private int totalTrackCount;
    @SerializedName("trackNumber")
    private int trackNumber;
    @SerializedName("title")
    private String mediaTitle;
    @SerializedName("id")
    private int mediaId;
    private int playState;
    private long lastSeekBarProgres;


    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getOffsetEnd() {
        return offsetEnd;
    }

    public void setOffsetEnd(String offsetEnd) {
        this.offsetEnd = offsetEnd;
    }

    public String getOffsetStart() {
        return offsetStart;
    }

    public void setOffsetStart(String offsetStart) {
        this.offsetStart = offsetStart;
    }

    public int getTotalTrackCount() {
        return totalTrackCount;
    }

    public void setTotalTrackCount(int totalTrackCount) {
        this.totalTrackCount = totalTrackCount;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public long getLastSeekBarProgres() {
        return lastSeekBarProgres;
    }

    public void setLastSeekBarProgres(long lastSeekBarProgres) {
        this.lastSeekBarProgres = lastSeekBarProgres;
    }

    @Override
    public String toString() {
        return "{" +
                "mediaUrl='" + mediaUrl + '\'' +
                ", offsetEnd='" + offsetEnd + '\'' +
                ", offsetStart='" + offsetStart + '\'' +
                ", totalTrackCount=" + totalTrackCount +
                ", trackNumber=" + trackNumber +
                ", mediaTitle='" + mediaTitle + '\'' +
                ", mediaId=" + mediaId +
                ", playState=" + playState +
                ", lastSeekBarProgres=" + lastSeekBarProgres +
                '}';
    }
}