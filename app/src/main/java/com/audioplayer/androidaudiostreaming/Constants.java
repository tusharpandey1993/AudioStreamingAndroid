package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import com.audioplayer.androidaudiostreaming.mediaPlayer.controller.AudioPlaybackListener;
import com.audioplayer.androidaudiostreaming.mediaPlayer.controller.AudioStreamingManager;

public class Constants {

    public static Constants instance;

    public static Constants getInstance(Context context) {
        if (instance == null) {
            instance = new Constants();
        }
        return instance;
    }

    public int currentProgress = 0;
    int MIKO_COACH_LIST_THREASHOLD_TIME          =  15;

}