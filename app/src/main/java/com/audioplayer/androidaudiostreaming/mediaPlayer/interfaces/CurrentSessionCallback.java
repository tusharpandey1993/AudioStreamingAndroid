package com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces;

import android.util.Log;
import com.audioplayer.androidaudiostreaming.mediaPlayer.models.MediaMetaData;

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
}
