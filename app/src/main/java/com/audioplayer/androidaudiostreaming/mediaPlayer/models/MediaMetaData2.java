package com.audioplayer.androidaudiostreaming.mediaPlayer.models;

public class MediaMetaData2 {

    private String mediaId;
    private String mediaUrl;
    private String mediaTitle;
    private String mediaArtist;
    private String mediaAlbum;
    private String mediaComposer;
    private String mediaDuration;
    private String mediaArt;
    private int playState;
    private String offsetStart;
    private String offsetEnd;

    public MediaMetaData2() {
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getMediaArtist() {
        return mediaArtist;
    }

    public void setMediaArtist(String mediaArtist) {
        this.mediaArtist = mediaArtist;
    }

    public String getMediaAlbum() {
        return mediaAlbum;
    }

    public void setMediaAlbum(String mediaAlbum) {
        this.mediaAlbum = mediaAlbum;
    }

    public String getMediaComposer() {
        return mediaComposer;
    }

    public void setMediaComposer(String mediaComposer) {
        this.mediaComposer = mediaComposer;
    }

    public String getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    public String getMediaArt() {
        return mediaArt;
    }

    public void setMediaArt(String mediaArt) {
        this.mediaArt = mediaArt;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }


    public String getOffsetStart() {
        return offsetStart;
    }

    public void setOffsetStart(String offsetStart) {
        this.offsetStart = offsetStart;
    }

    public String getOffsetEnd() {
        return offsetEnd;
    }

    public void setOffsetEnd(String offsetEnd) {
        this.offsetEnd = offsetEnd;
    }

    @Override
    public String toString() {
        return "MediaMetaData{" +
                "mediaId='" + mediaId + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", mediaTitle='" + mediaTitle + '\'' +
                ", mediaArtist='" + mediaArtist + '\'' +
                ", mediaAlbum='" + mediaAlbum + '\'' +
                ", mediaComposer='" + mediaComposer + '\'' +
                ", mediaDuration='" + mediaDuration + '\'' +
                ", mediaArt='" + mediaArt + '\'' +
                ", playState=" + playState +
                ", offsetStart='" + offsetStart + '\'' +
                ", offsetEnd='" + offsetEnd + '\'' +
                '}';
    }
}
