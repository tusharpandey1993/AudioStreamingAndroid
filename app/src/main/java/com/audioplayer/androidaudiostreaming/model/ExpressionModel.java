package com.audioplayer.androidaudiostreaming.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpressionModel {

    @SerializedName("ax_stream")
    private Ax_stream ax_stream;

    public Ax_stream getAx_stream() {
        return ax_stream;
    }

    public void setAx_stream(Ax_stream ax_stream) {
        this.ax_stream = ax_stream;
    }

    @Override
    public String toString() {
        return "badJson{" +
                "ax_stream=" + ax_stream +
                '}';
    }

    public static class Ax_stream {
        @SerializedName("seq")
        private List<Seq> seq;
        @SerializedName("seqCount")
        private int seqCount;
        @SerializedName("loop")
        private int loop;
        @SerializedName("size")
        private int size;
        @SerializedName("type")
        private int type;

        public List<Seq> getSeq() {
            return seq;
        }

        public void setSeq(List<Seq> seq) {
            this.seq = seq;
        }

        public int getSeqCount() {
            return seqCount;
        }

        public void setSeqCount(int seqCount) {
            this.seqCount = seqCount;
        }

        public int getLoop() {
            return loop;
        }

        public void setLoop(int loop) {
            this.loop = loop;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Ax_stream{" +
                    "seq=" + seq +
                    ", seqCount=" + seqCount +
                    ", loop=" + loop +
                    ", size=" + size +
                    ", type=" + type +
                    '}';
        }
    }

    public static class Seq {
        @SerializedName("site")
        private String site;
        @SerializedName("offsetEnd")
        private String offsetEnd;
        @SerializedName("offsetStart")
        private String offsetStart;
        @SerializedName("totalTrackCount")
        private int totalTrackCount;
        @SerializedName("trackNumber")
        private int trackNumber;
        @SerializedName("title")
        private String title;
        @SerializedName("id")
        private int id;

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "{" +
                    "site='" + site + '\'' +
                    ", offsetEnd='" + offsetEnd + '\'' +
                    ", offsetStart='" + offsetStart + '\'' +
                    ", totalTrackCount=" + totalTrackCount +
                    ", trackNumber=" + trackNumber +
                    ", title='" + title + '\'' +
                    ", id=" + id +
                    '}';
        }
    }


}
