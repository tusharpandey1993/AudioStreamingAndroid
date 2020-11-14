package com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces;

import com.audioplayer.androidaudiostreaming.model.MediaMetaData;

public interface CurrentSessionCallback {

    default void updatePlaybackState(int state){
    }

    default void playSongComplete(){
    }

    default void currentSeekBarPosition(int progress){
    }

    default void playCurrent(int indexP, MediaMetaData currentAudio){
    }

    default void playNext(int indexP, MediaMetaData currentAudio){
    }

    default void playPrevious(int indexP, MediaMetaData currentAudio){
    }

    default void almostFinish(int indexP, MediaMetaData currentAudio){
    }

    void onMediaPlayerError(Object cause);

    void onError(Object cause);

    void onRegainInternet();
}
