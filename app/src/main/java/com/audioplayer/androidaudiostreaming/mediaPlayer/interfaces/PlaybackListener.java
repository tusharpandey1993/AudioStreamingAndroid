package com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces;


import com.audioplayer.androidaudiostreaming.model.MediaMetaData;

public interface PlaybackListener {

    default void start(){}

    default void stop(boolean notifyListeners){}

    default void setState(int state){}

    int getState();

    boolean isConnected();

    boolean isPlaying();

    int getCurrentStreamPosition();

    int getMediaDuration();

    int getBufferedMediaLength();

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

        void onMediaPlayerError(Object cause);
    }

    default void setCallback(Callback callback){}


    void onPreparedListener();


}
