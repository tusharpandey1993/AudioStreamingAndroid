package com.audioplayer.androidaudiostreaming;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.audioplayer.androidaudiostreaming.mediaPlayer.controller.AudioStreamingManager;
import com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces.CurrentSessionCallback;
import com.audioplayer.androidaudiostreaming.mediaPlayer.models.MediaMetaData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.bravobit.ffmpeg.FFmpeg;

public class MainActivity extends AppCompatActivity implements CurrentSessionCallback, View.OnClickListener {

    public static AudioStreamingManager streamingManager;
    private static MediaMetaData currentSong;
    private List<MediaMetaData> listOfSongs = new ArrayList<MediaMetaData>();
    private static final String TAG = "MainActivity";
    private AudioStreamingManager audioStreamingManager;
    private CurrentSessionCallback currentSessionCallback;
    private Button play, SeekTo, lastSeekPosition, stop, Resume, previous, next;
    public static long lastPosition;
    public static int bufferedMediaDuartion;
    public static long canPlayTillSeconds;
    TextView textCurrentTime,textTotalDuration;
    SeekBar playerSeekBar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        init();
        this.streamingManager = AudioStreamingManager.getInstance(this);
        configAudioStreamer();
//        new WorkerThread().start();
        playerSeekBar.setMax(100);


        /*try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playPauseEvent();
                }
            },2000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playPauseEvent();
                }
            },14000);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void init() {
        Resume = findViewById(R.id.Resume);
        lastSeekPosition = findViewById(R.id.lastSeekPosition);
        stop = findViewById(R.id.stop);
        SeekTo = findViewById(R.id.SeekTo);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        textCurrentTime = findViewById(R.id.textCurrentTime);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        textTotalDuration = findViewById(R.id.textTotalDuration);
//        mediaPlayer  = new MediaPlayer();

        Resume.setOnClickListener(this);
        lastSeekPosition.setOnClickListener(this);
        stop.setOnClickListener(this);
        SeekTo.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
    }
    public void updateSeekBar(){

        if(streamingManager.isPlaying()){
            playerSeekBar.setProgress((int)((float) streamingManager.lastSeekPosition()/streamingManager.getDuration() *100));
            handler.postDelayed(updater,1000);
//            textTotalDuration.setText(streamingManager.getDuration());
        }

    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration =streamingManager.lastSeekPosition();
            textCurrentTime.setText(miliSecondsToTimer(currentDuration));
        }
    };

    public String miliSecondsToTimer(long miliSeconds){

        String timerString= "";
        String secondsString = "";

        int hours = (int) (miliSeconds /(1000 *60 *  60));
        int minutes  = (int)(miliSeconds %(1000 *60 *60)/(1000* 60));
        int seconds = (int) (miliSeconds  %(1000 *60 *60)% (1000* 60)/1000);

        if(hours>0){
            timerString  = hours +":";
        }

        if(seconds<10){
            secondsString = "0" +seconds ;
        }else{
            secondsString = "" +seconds;
        }

        timerString = timerString + minutes +":" +secondsString;

        return timerString;

    }

    private void configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(this);
        streamingManager.subscribesCallBack(this);
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.setPlayMultiple(true);
        String response = loadJSONFromAsset(this);
        listOfSongs = getMusicList(response, "music");
        streamingManager.setMediaList(listOfSongs);
        playSong(listOfSongs.get(0));
        handler = new Handler();

        updateSeekBar();

//        checkAlreadyPlaying();
//        playPauseEvent();
    }

    private void playPauseEvent() {
        Log.d(TAG, "playPauseEvent: streamingManager.isPlaying(): " + streamingManager.isPlaying() + " currentSong: " + currentSong);
        if (streamingManager.isPlaying()) {
            streamingManager.onPause();
        } else {
            streamingManager.onPlay(currentSong);

        }
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

    public static List<MediaMetaData> getMusicList(String response, String name) {
        List<MediaMetaData> listArticle = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(response).getJSONArray(name);
            for (int i = 0; i < array.length(); i++) {
                MediaMetaData infoData = new MediaMetaData();
                JSONObject musicObj = array.getJSONObject(i);
                infoData.setMediaId(musicObj.optString("id"));
                infoData.setMediaUrl(musicObj.optString("site") + musicObj.optString("source"));
                infoData.setMediaTitle(musicObj.optString("title"));
                infoData.setMediaArtist(musicObj.optString("artist"));
                infoData.setMediaAlbum(musicObj.optString("album"));
                infoData.setMediaComposer(musicObj.optString(""));
                infoData.setMediaDuration(musicObj.optString("duration"));
                infoData.setMediaArt(musicObj.optString("site") + musicObj.optString("image"));
                listArticle.add(infoData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listArticle;
    }


    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("music.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
//                btn_play.Pause();
                if (currentSong != null) {
                    currentSong.setPlayState(PlaybackStateCompat.STATE_PAUSED);
                }
                break;
            case PlaybackStateCompat.STATE_NONE:
                currentSong.setPlayState(PlaybackStateCompat.STATE_NONE);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
//                btn_play.Pause();
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
                Log.d(TAG, "onCreate: " + streamingManager.getDuration());
                playerSeekBar.setProgress((int)((float) TimeUnit.MILLISECONDS.toSeconds(streamingManager.lastSeekPosition())));
                  Log.d(TAG, "onCreate: " + streamingManager.getDuration());
                break;
            case R.id.stop:
                streamingManager.onStop();
                streamingManager.cleanupPlayer(this,true,true);
                break;
            case R.id.SeekTo:
                streamingManager.onSeekTo(streamingManager.getDuration() - 20000);
                break;
            case R.id.next:
                streamingManager.onSkipToNext();
                break;
            case R.id.previous:
                streamingManager.onSkipToPrevious();
                break;
        }

    }


}
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
}