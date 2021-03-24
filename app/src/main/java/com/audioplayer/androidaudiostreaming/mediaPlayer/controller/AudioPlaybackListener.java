package com.audioplayer.androidaudiostreaming.mediaPlayer.controller;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaTimestamp;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.renderscript.Allocation;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.audioplayer.androidaudiostreaming.Constants;
import com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces.PlaybackListener;
import com.audioplayer.androidaudiostreaming.model.MediaMetaData;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AudioPlaybackListener implements PlaybackListener, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnBufferingUpdateListener, Allocation.OnBufferAvailableListener {
    private static final String TAG = "AudioPlaybackListener";

    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;

    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_FOCUSED = 2;

    private final Context mContext;
    private final WifiManager.WifiLock mWifiLock;

    private int mState;
    private Callback mCallback;
    private boolean mPlayOnFocusGain;
    private volatile int mCurrentPosition;
    private volatile int mDuration = 0;
    private volatile int bufferPercent = 0;
    private volatile String mCurrentMediaId;

    private int mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
    private final AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private boolean isLooping = false;
    private int loopCountRequest;
    private int loopCount = 0;
    private int offSetStart;
    private long offSetEnd;
    private boolean retrying = false;
    private MediaMetaData currentAudioRetry;

    public void setLoopCountToZero() {
        int loopCountRequest;
        int loopCount = 0;
    }

    public AudioPlaybackListener(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.mWifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "dmAudioStreaming_Lock");
        this.mState = PlaybackStateCompat.STATE_NONE;
    }

//    private final IntentFilter mAudioNoisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//    private final BroadcastReceiver mAudioNoisyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
//                Log.d(TAG, "Headphones disconnected.");
//                if (isPlaying()) {
//                    TODO PAUSE THE PLAYING SONG
//                }
//            }
//        }
//    };

    @Override
    public void start() {
    }

    @Override
    public void stop(boolean notifyListeners) {
        mState = PlaybackStateCompat.STATE_STOPPED;
        if (notifyListeners && mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
        mCurrentPosition = getCurrentStreamPosition();
        // Give up Audio focus
        giveUpAudioFocus();
//        unregisterAudioNoisyReceiver();
        // Relax all resources
        relaxResources(true);
    }

    @Override
    public void setState(int state) {
        this.mState = state;
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isPlaying() {
        return mPlayOnFocusGain || (mMediaPlayer != null && mMediaPlayer.isPlaying());
    }

    @Override
    public int getCurrentStreamPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : mCurrentPosition;
    }

    @Override
    public int getMediaDuration() {
//        try {
//            if(mMediaPlayer != null) {
//                Log.d(TAG, "getMediaDuration: not null " + mMediaPlayer.getDuration());
//            } else {
//                Log.d(TAG, "getMediaDuration: mediaplayer is null");
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "getMediaDuration: excep "  + e.getMessage());
//        }

        return  mMediaPlayer != null ? mMediaPlayer.getDuration() : mDuration;
    }

    @Override
    public int getBufferedMediaLength() {
        return bufferPercent;
    }


    @Override
    public void updateLastKnownStreamPosition() {
        if (mMediaPlayer != null) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void play(MediaMetaData item) {
        if(item != null) {
            Log.d(TAG, "play: 2");
            mPlayOnFocusGain = true;
            tryToGetAudioFocus();
//        registerAudioNoisyReceiver();
            String mediaId = String.valueOf(item.getMediaId());
            boolean mediaHasChanged = !TextUtils.equals(mediaId, mCurrentMediaId);
            if (mediaHasChanged) {
                mCurrentPosition = 0;
                mCurrentMediaId = mediaId;
            }
            Log.d(TAG, "play:retryResume " + retrying);
            if (mState == PlaybackStateCompat.STATE_PAUSED && !mediaHasChanged && mMediaPlayer != null) {
                configMediaPlayerState();
            } else {
                mState = PlaybackStateCompat.STATE_STOPPED;
                relaxResources(false); // release everything except MediaPlayer
                String source = item.getMediaUrl();
                if (source != null) {
                    source = source.replaceAll(" ", "%20"); // Escape spaces for URLs
                }
                try {
                    Log.d(TAG, "play: 3");
                    createMediaPlayerIfNeeded();
                    mState = PlaybackStateCompat.STATE_BUFFERING;
                    mMediaPlayer.setAudioAttributes(
                            new AudioAttributes
                                    .Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build());
                    Log.d(TAG, "play: source : "+ source + "  mState : "  +mState);
                    mMediaPlayer.setDataSource(source);

                    mMediaPlayer.prepareAsync();
                    offSetStart = Integer.parseInt(item.getOffsetStart());
                    offSetEnd = Long.parseLong(item.getOffsetEnd());
                    mWifiLock.acquire();

                    if (mCallback != null) {
                        mCallback.onPlaybackStatusChanged(mState);
                    }

                } catch (IOException ex) {
                    Log.e(TAG, ex +"Exception playing song");
                    if (mCallback != null) {
                        mCallback.onError(ex.getMessage());
                    }
                }
            }
        }

    }

    @Override
    public void pause() {
        try {
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                // Pause media player and cancel the 'foreground service' state.
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mCurrentPosition = mMediaPlayer.getCurrentPosition();
                }
                // while paused, retain the MediaPlayer but give up audio focus
                relaxResources(false);
            }
            mState = PlaybackStateCompat.STATE_PAUSED;
            if (mCallback != null) {
                mCallback.onPlaybackStatusChanged(mState);
            }
//            unregisterAudioNoisyReceiver();
        } catch (IllegalStateException ex) {
            Log.e(TAG, ex + "Exception pause IllegalStateException");
            ex.printStackTrace();
        }
    }


    @Override
    public void seekTo(int position) {
        Log.d(TAG, "seekTo called with " + TimeUnit.MILLISECONDS.toMinutes(position));

        if (mMediaPlayer == null) {
            // If we do not have a current media player, simply update the current position
            mCurrentPosition = position;
        } else {
            if (mMediaPlayer.isPlaying()) {
                mState = PlaybackStateCompat.STATE_BUFFERING;
            }
//            registerAudioNoisyReceiver();
            mMediaPlayer.seekTo(position);
            if (mCallback != null) {
                mCallback.onPlaybackStatusChanged(mState);
            }
        }
    }

    public void loopOff() {
        isLooping = false;
        setLoopCountToZero();
    }

    public void loopForever() {
        isLooping = true;
        setLoopCountToZero();
        loopCountRequest = Integer.MAX_VALUE;
    }

    public void loopLimited(int count) {
        isLooping = true;
        setLoopCountToZero();
        loopCountRequest = count;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void retryResume(MediaMetaData currentAudio) {
        currentAudioRetry = currentAudio;
        if(mMediaPlayer != null) {
            if(bufferPercent == 100) {
                mMediaPlayer.seekTo(Constants.getInstance(mContext).currentProgress);
                mState = PlaybackStateCompat.STATE_PLAYING;
            } else {
                reconfigureMediaPlayer(currentAudio);
            }
        } else {
            reconfigureMediaPlayer(currentAudio);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void reconfigureMediaPlayer(MediaMetaData currentAudio) {
        retrying = true;
        this.play(currentAudio);
        this.configMediaPlayerState();
        this.pause();
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onPreparedListener() {
        Log.d(TAG, "onPreparedListener: mMediaPlayer is prepared" );

    }

    @Override
    public void setCurrentStreamPosition(int pos) {
        Log.d(TAG, "setCurrentStreamPosition: " + pos);
        this.mCurrentPosition = pos;
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        this.mCurrentMediaId = mediaId;
    }

    @Override
    public String getCurrentMediaId() {
        return mCurrentMediaId;
    }

    private void tryToGetAudioFocus() {
        Log.d(TAG, "tryToGetAudioFocus");
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_FOCUSED;
        } else {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    private void giveUpAudioFocus() {
        Log.d(TAG, "giveUpAudioFocus");
        if (mAudioManager.abandonAudioFocus(this) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    private void configMediaPlayerState() {
        Log.d(TAG, "configMediaPlayerState. mAudioFocus=" + mAudioFocus);
        if (mAudioFocus == AUDIO_NO_FOCUS_NO_DUCK) {
            // If we don't have audio focus and can't duck, we have to pause,
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                mPlayOnFocusGain = false;
                pause();
            }
        } else {  // we have audio focus:
//            registerAudioNoisyReceiver();
            if (mAudioFocus == AUDIO_NO_FOCUS_CAN_DUCK) {
                mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK); // we'll be relatively quiet
            } else {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL); // we can be loud again
                } // else do something for remote client.
            }
            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    Log.d(TAG, "configMediaPlayerState startMediaPlayer. seeking to " +
                            mCurrentPosition);
                    if (mCurrentPosition == mMediaPlayer.getCurrentPosition()) {
                        Log.d(TAG, "configMediaPlayerState: play: 4 ");
//                        mDuration = mMediaPlayer.getDuration();
//                        Log.d(TAG, "configMediaPlayerState: mDuration:" + mDuration  + " actual duration  " + mMediaPlayer.getDuration());
                        Log.d(TAG, "configMediaPlayerState:offSetStart " + offSetStart);
                        if(offSetStart > 0) {
                            seekToFirstKeyframe();
                        } else {
                            mMediaPlayer.start();
                        }
                        mState = PlaybackStateCompat.STATE_PLAYING;
                    } else {
                        mMediaPlayer.seekTo(mCurrentPosition);
                        mState = PlaybackStateCompat.STATE_BUFFERING;
                    }
                }
                mPlayOnFocusGain = false;
            }
        }
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    private void seekToFirstKeyframe() {
        mMediaPlayer.start();
        mMediaPlayer.pause();
        this.seekTo(offSetStart);
        mMediaPlayer.start();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.d(TAG, "onAudioFocusChange. focusChange=" + focusChange);
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // We have gained focus:
            mAudioFocus = AUDIO_FOCUSED;

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // We have lost focus. If we can duck (low playback volume), we can keep playing.
            // Otherwise, we need to pause the playback.
            boolean canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
            mAudioFocus = canDuck ? AUDIO_NO_FOCUS_CAN_DUCK : AUDIO_NO_FOCUS_NO_DUCK;

            // If we are playing, we need to reset media player by calling configMediaPlayerState
            // with mAudioFocus properly set.
            if (mState == PlaybackStateCompat.STATE_PLAYING && !canDuck) {
                // If we don't have audio focus and can't duck, we save the information that
                // we were playing, so that we can resume playback once we get the focus back.
                mPlayOnFocusGain = true;
            }
        } else {
            Log.e(TAG, "onAudioFocusChange: Ignoring unsupported focusChange: " + focusChange);
        }
        configMediaPlayerState();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d(TAG, "onSeekComplete from MediaPlayer:" + mp.getCurrentPosition());
        mCurrentPosition = mp.getCurrentPosition();
        if (mState == PlaybackStateCompat.STATE_BUFFERING) {
//            registerAudioNoisyReceiver();
            mMediaPlayer.start();
            mState = PlaybackStateCompat.STATE_PLAYING;
        }
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        Log.d(TAG, "onCompletion from MediaPlayer");
        // The media player finished playing the current song, so we go ahead
        // and start the next.
        if (mCallback != null) {
            mCallback.onCompletion();
        }

        if(mMediaPlayer != null) {
            if(isLooping){
                loopCount++;
                mMediaPlayer.seekTo(0);
                mMediaPlayer.start();
                if(loopCount == loopCountRequest){
                    mMediaPlayer.stop();
                    isLooping = false;
                    mMediaPlayer.setLooping(false);
                }
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        Log.d(TAG, "onPrepared from MediaPlayer");
        configMediaPlayerState();
        mDuration = mMediaPlayer.getDuration();
    }

    /**
     * Called when there's an error playing media. When this happens, the media
     * player goes to the Error state. We warn the user about the error and
     * reset the media player.
     *
     * @see MediaPlayer.OnErrorListener
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mCallback.onMediaPlayerError(what);
        Log.e(TAG, "Media player error: what=" + what + ", extra=" + extra);
        if (mCallback != null) {
            mCallback.onError(String.valueOf(what));
        }
        return true; // true indicates we handled the error
    }

    /**
     * Makes sure the media player exists and has been reset. This will create
     * the media player if needed, or reset the existing media player if one
     * already exists.
     */
    private void createMediaPlayerIfNeeded() {
        Log.d(TAG, "createMediaPlayerIfNeeded. needed? " +(mMediaPlayer == null));
        if (mMediaPlayer == null) {
            Log.d(TAG, "createMediaPlayerIfNeeded: " + mMediaPlayer);
            mMediaPlayer = new MediaPlayer();

            // Make sure the media player will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            mMediaPlayer.setWakeMode(mContext.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            // we want the media player to notify us when it's ready preparing,
            // and when it's done playing:
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
        } else {
            Log.d(TAG, "createMediaPlayerIfNeeded: media player is not null " );
            mMediaPlayer.reset();
        }
    }

    /**
     * Releases resources used by the service for playback. This includes the
     * "foreground service" status, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also
     *                           be released or not
     */
    private void relaxResources(boolean releaseMediaPlayer) {
        Log.d(TAG, "relaxResources. releaseMediaPlayer=" + releaseMediaPlayer);

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentMediaId = null;
            mCurrentPosition = 0;
        }

        // we can also release the Wifi lock, if we're holding it
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int bufferProgress) {
        bufferPercent = bufferProgress;
        Log.d(TAG, "hello onBufferingUpdate i: " + bufferPercent  );
        if(retrying && bufferProgress == 100) {
            retrying = false;
            this.play(currentAudioRetry);
            this.configMediaPlayerState();
            this.pause();

            this.seekTo(Constants.getInstance(mContext).currentProgress);
            this.play(currentAudioRetry);
        }
    }

    @Override
    public void onBufferAvailable(Allocation allocation) {
        Log.d(TAG, "hello onBufferAvailable: " + allocation.toString());
    }

}