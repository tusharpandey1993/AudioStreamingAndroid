package com.audioplayer.androidaudiostreaming.mediaPlayer.controller;

import com.audioplayer.androidaudiostreaming.model.MediaMetaData;

public abstract class StreamingContoller {

    public abstract void onPlay(MediaMetaData infoData);

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onSeekTo(long position);

    public abstract int lastSeekPosition();

    public abstract void onSkipToNext();

    public abstract void onSkipToPrevious();

    public abstract int getDuration();

    public abstract int getBufferedMediaLength();

    public abstract int getMediaState();

    public abstract int almostFinished();

/*

    public abstract void PlaybackStarted();

    public abstract void PlaybackFinished();

    public abstract void PlaybackStopped();

    public abstract void PlayBackFailed();

    public abstract void PlayBackIntervalCallback();

    public abstract void PlaybackNearlyFinished();
*/

}
