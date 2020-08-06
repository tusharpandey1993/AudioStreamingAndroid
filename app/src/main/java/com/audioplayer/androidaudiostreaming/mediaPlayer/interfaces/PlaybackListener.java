package com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces;

import com.audioplayer.androidaudiostreaming.mediaPlayer.models.MediaMetaData;

public interface PlaybackListener {

    default void start(){}

    default void stop(boolean notifyListeners){}

    default void setState(int state){}

    int getState();

    boolean isConnected();

    boolean isPlaying();

    int getCurrentStreamPosition();

    default void setCurrentStreamPosition(int pos){}

    default void updateLastKnownStreamPosition(){}

    default void play(MediaMetaData item){}

    default void pause(){}

    default void seekTo(int position){}

    default void setCurrentMediaId(String mediaId){}

    String getCurrentMediaId();

    interface Callback {
        default void onCompletion(){}

        default void onPlaybackStatusChanged(int state){}

        default void onError(String error){}

        default void setCurrentMediaId(String mediaId){}
    }

    default void setCallback(Callback callback){}
}
