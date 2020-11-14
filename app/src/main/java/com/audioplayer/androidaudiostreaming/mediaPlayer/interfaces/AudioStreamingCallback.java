package com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces;


import com.audioplayer.androidaudiostreaming.model.MediaMetaData;

public interface AudioStreamingCallback {

    // This will start playing the audio by stopping current audio if isPlaying
    default void play(MediaMetaData mediaMetaData) {
    }

    // This will check is something is playing it will pause and save the seek position
    default void pause() {
    }

    // This will stop the audio and move the seek position to 0
    default void stop() {
    }

    // This will check the current seek poisition and resume the audio from that seek
    default void resume() {
    }

    // This will stop the current audio and play the next audio
    default void playNext() {
    }

    // This will play previously played audio if there is any audio listed in queue
    default void playPrevious() {
    }

    // This will loop the audio infinite or by number of counts
    default void loop() {
    }

    // The buffer and audio will seek to the last seekposition and resume the audio from there
    default void resumeOnInternetRestart() {
    }

    // This will return if any audio is currently playing from MediaPlayer Singleton Class
    default void isPlaying() {
    }

    // This will return weather any audio is paused
    default void isPaused() {
    }

    // This will return if any audio is stopped
    default void isStopped() {
    }

    // This will check is audio is looped (finite or infitine)
    default void isLooped() {
    }

    // This will return current audio playing position
    default void getCurrentPosition() {
    }

    // This will seek the audio to desired seek position ( have to check if audio is in the limit of seek position)
    default void seekTo(long position) {
    }

    // This will return the last seek position (Very important)
    default void lastSeekPositon() {
    }

    // This will return if audio starts or resumes buffering
    default void onStartBuffering() {
    }

    // This will return if audio is any more buffering or not
    default void onStopBuffering() {
    }

    // This will return if audio is completely buffered or not( check is it clashing with stop buffering)
    default void isBufferingCompleted() {
    }

    // This will return tha audio playing is completed and move the seek position to 0
    default void onPlayComplete() {
    }

    // This will return any kind of error
    default void onError() {
    }

    // This will send a message to backend that audio is almost complete ( 10 seconds remaining or 10% of audio is remaining)
    default void almostFinished() {
    }

    // This will return if internet isOn
    default void isInternetOn() {
    }

    // This will add songs in Queue ( in a linear list which will keep the record of all media played list)
    default void addToPlayedList() {

    }
}