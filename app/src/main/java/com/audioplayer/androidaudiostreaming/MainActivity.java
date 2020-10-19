package com.audioplayer.androidaudiostreaming;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.audioplayer.androidaudiostreaming.mediaPlayer.controller.AudioStreamingManager;
import com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces.CurrentSessionCallback;
import com.audioplayer.androidaudiostreaming.mediaPlayer.models.MediaMetaData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity implements CurrentSessionCallback, View.OnClickListener, View.OnTouchListener {

    public static AudioStreamingManager streamingManager;
    private MediaMetaData currentSong;
    private List<MediaMetaData> listOfSongs = new ArrayList<MediaMetaData>();
    private static final String TAG = "MainActivity";
    private CurrentSessionCallback currentSessionCallback;
    private Button play, seekTo, lastSeekPosition, stop, Resume, previous, next, loopCount, loopOff, loopOnce, loopInfinite;
    public static long lastPosition;
    public static int bufferedMediaDuartion;
    TextView textCurrentTime,textTotalDuration;
    SeekBar playerSeekBar;
    Handler handler;
    private long totalSongDuration;
    private int totalPercentage = 10;
    private int almostFinishedThreshold = 10;
    public long almostFinishedDuration;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        init();
        this.streamingManager = AudioStreamingManager.getInstance(this);
        playerSeekBar.setMax(100);
//        Log.d(TAG, "configAudioStreamer: "+ getMimeType(this, Uri.parse("")));
        Log.d(TAG, "configAudioStreamer: "+ getMimeType(this, Uri.parse("https://srv-file21.gofile.io/download/bYQFYE/wink.json")));

    }



    public static String getMimeType(Context context, Uri uri) {
        String extension = null;

        //Check uri format to avoid null
        if(uri != null && uri.getScheme() != null) {
            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                //If scheme is a content
                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
            } else {
                //If scheme is a File
                //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

            }
        }
        return  extension;
    }


    private void init() {
        Resume = findViewById(R.id.Resume);
        lastSeekPosition = findViewById(R.id.lastSeekPosition);
        stop = findViewById(R.id.stop);
        seekTo = findViewById(R.id.seekTo);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        loopCount = findViewById(R.id.loopCount);
        loopOnce = findViewById(R.id.loopOnce);
        loopOff = findViewById(R.id.loopOff);
        loopInfinite = findViewById(R.id.loopInfinite);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        textTotalDuration = findViewById(R.id.textTotalDuration);


        Resume.setOnClickListener(this);
        lastSeekPosition.setOnClickListener(this);
        stop.setOnClickListener(this);
        seekTo.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        loopCount.setOnClickListener(this);
        loopOff.setOnClickListener(this);
        loopOnce.setOnClickListener(this);
        loopInfinite.setOnClickListener(this);
//        playerSeekBar.setOnTouchListener(this);
        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(streamingManager != null && fromUser){
                    streamingManager.onSeekTo(progress * 1000);
                }
            }
        });
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration =streamingManager.lastSeekPosition();
            textCurrentTime.setText(Utility.getInstance(MainActivity.this).miliSecondsToTimer(currentDuration));
        }
    };

    public void updateSeekBar(){

        if(streamingManager.isPlaying()){
            mediaPlayer = streamingManager.getMediaPlayerObject();
            playerSeekBar.setProgress(streamingManager.lastSeekPosition()/streamingManager.getDuration() *100);
            playerSeekBar.setSecondaryProgress(streamingManager.getBufferedMediaLength());
            handler.postDelayed(updater,500);

        }
    }


    private void configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(this);
        streamingManager.subscribesCallBack(this);
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.setPlayMultiple(true);
        String response = Utility.getInstance(this).loadJSONFromAsset(this, "music.json");
        listOfSongs = Utility.getInstance(this).getMusicList(response, "music");
        streamingManager.setMediaList(listOfSongs);
        currentSong = listOfSongs.get(0);

        playSong(currentSong);

        updateSeekBar();

    }



    private void playPauseEvent() {
        if (streamingManager.isPlaying()) {
            streamingManager.onPause();
        } else {
            streamingManager.onPlay(currentSong);
        }
        updateSeekBar();
    }

    private void playSong(MediaMetaData media) {
        if (streamingManager != null) {
            streamingManager.onPlay(media);
        }
    }

    private void checkAlreadyPlaying() {
        if (streamingManager.isPlaying()) {
            currentSong = streamingManager.getCurrentAudio();
            if (currentSong != null) {
                currentSong.setPlayState(streamingManager.mLastPlaybackState);
            }
        }
    }



    @Override
    public void updatePlaybackState(int state) {
        Log.d("updatePlaybackState: ", "" + state);
        switch (state) {
            case PlaybackStateCompat.STATE_PLAYING:
//                btn_play.Play();
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PLAYING);
                }
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PAUSED);
                }
                break;
            case PlaybackStateCompat.STATE_NONE:
                currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                }
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                }
                break;
        }
    }

    @Override
    public void playSongComplete() {
        Log.d(TAG, "playSongComplete:");
    }

    @Override
    public void currentSeekBarPosition(int progress) {

        if(streamingManager.isPlaying()) {
            if(streamingManager.getDuration() >0) {
                int duration  = (streamingManager.getDuration()/1000) * 60; // In seconds
                int due = (streamingManager.getDuration() - streamingManager.lastSeekPosition())/1000;
                int pass = duration - due;

//                Log.e(TAG, "updateSeekBar: "+ pass + " seconds");
//                Log.e(TAG, "duration" + duration + " seconds");
//                Log.e(TAG, "due" + due + " seconds");


                long tenPercent = (duration * totalPercentage) / 100;
            }
        }
        updateSeekBar();

    }

    @Override
    public void playCurrent(int indexP, MediaMetaData currentAudio) {
        Log.d(TAG, "x: currentAudio " +currentAudio);
        showMediaInfo(currentAudio);
    }

    @Override
    public void playNext(int indexP, MediaMetaData currentAudio) {
        Log.d(TAG, "playNext: currentAudio " +currentAudio);
    }

    @Override
    public void playPrevious(int indexP, MediaMetaData currentAudio) {
        Log.d(TAG, "playPrevious: currentAudio " +currentAudio);

    }

    private void showMediaInfo(MediaMetaData media) {
        Log.d(TAG, "showMediaInfo: media " +media);
        currentSong = media;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            subscribesCallBack(this);
            if (streamingManager != null) {
                streamingManager.subscribesCallBack(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void subscribesCallBack(CurrentSessionCallback callback) {
        this.currentSessionCallback = callback;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                configAudioStreamer();
                break;
            case R.id.Resume:
                playPauseEvent();
                break;
            case R.id.lastSeekPosition:
                Toast.makeText(this, "" + TimeUnit.MILLISECONDS.toMinutes(streamingManager.lastSeekPosition()) , Toast.LENGTH_SHORT).show();

                break;
            case R.id.stop:
                streamingManager.onStop();
                streamingManager.cleanupPlayer(this,true,true);
                break;
            case R.id.seekTo:
                if(currentSong != null) {
                    Log.d(TAG, "onClick: " + currentSong.getOffsetStart());
                    streamingManager.onSeekTo(Long.parseLong(currentSong.getOffsetStart()));
                    playerSeekBar.setProgress((int)((float) TimeUnit.MILLISECONDS.toSeconds(streamingManager.getDuration() - 100000)));
                }
                break;
            case R.id.next:
                streamingManager.onSkipToNext();
                break;
            case R.id.previous:
                streamingManager.onSkipToPrevious();
                break;
            case R.id.loopCount:
                streamingManager.loopLimited(3);
                break;
            case R.id.loopOff:
                streamingManager.loopOff();
                break;
            case R.id.loopOnce:
                streamingManager.loopOnce();
                break;
            case R.id.loopInfinite:
                streamingManager.loopForever();
                break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
/*
class WorkerThread extends Thread {


    public WorkerThread() {
        // When false, (i.e. when it's a user thread),
        // the Worker thread continues to run.
        // When true, (i.e. when it's a daemon thread),
        // the Worker thread terminates when the main
        // thread terminates.
        setDaemon(true);
    }

    @SuppressLint("NewApi")
    public void run() {
        int count = 0;

        while (true) {
            if(MainActivity.streamingManager!= null){
//                MainActivity.lastPosition = TimeUnit.MILLISECONDS.toSeconds(streamingManager.lastSeekPosition());
//                MainActivity.bufferedMediaDuartion = streamingManager.getBufferedMediaLength();

//                Log.d("MainActivity", "current Position: " + MainActivity.lastPosition);
//                Log.d("MainActivity", "Buffered Media: " + MainActivity.bufferedMediaDuartion);
//                Log.d("MainActivity", "State Media: " + streamingManager.getMediaState());

//                MainActivity.canPlayTillSeconds = streamingManager.getDuration() * ((long) (0.1 * MainActivity.bufferedMediaDuartion));
//                Log.d("MainActivity", "canPlayTillSeconds: " + TimeUnit.MILLISECONDS.toMinutes(MainActivity.canPlayTillSeconds));

//                if(streamingManager.getDuration() - MainActivity.lastPosition <= 10){
//                    Log.d("MainActivity", "run: almost finished");
//                }

            } else {
//                Log.d("MainActivity", "streamingManager: was null");
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                // handle exception here
            }
        }
    }
}*/