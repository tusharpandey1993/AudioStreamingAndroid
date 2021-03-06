package com.audioplayer.androidaudiostreaming.mediaPlayer.controller;

import com.audioplayer.androidaudiostreaming.mediaPlayer.models.MediaMetaData;

public abstract class StreamingContoller {

    public abstract void onPlay(MediaMetaData infoData);

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onSeekTo(long position);

    public abstract int lastSeekPosition();

    public abstract void onSkipToNext();

    public abstract void onSkipToPrevious();

    public abstract void onAlmostFinished();

}
