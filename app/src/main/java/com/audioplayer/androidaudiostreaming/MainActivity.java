package com.audioplayer.androidaudiostreaming;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
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
import com.audioplayer.androidaudiostreaming.model.ExpressionKeyFrame;
import com.audioplayer.androidaudiostreaming.model.MediaMetaData;
import com.audioplayer.androidaudiostreaming.model.ExpressionModel;
import com.google.gson.Gson;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity implements CurrentSessionCallback, View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static AudioStreamingManager streamingManager;
    private MediaMetaData currentSong;
    private List<MediaMetaData> listOfSongs;
    private AudioStreamingManager audioStreamingManager;
    private CurrentSessionCallback currentSessionCallback;
    private Button play, SeekTo, lastSeekPosition, stop, Resume, previous, next, loopCount, loopOff, loopOnce, loopInfinite, retryPlayResume;
    public static long lastPosition;
    public static int bufferedMediaDuartion;
    TextView textCurrentTime, textTotalDuration;
    SeekBar playerSeekBar;
    Handler handler;
    private int totalPercentage = 10;










    // This is for hardcoded reponse speech and speechArray
    private ArrayList<String> speech;
    private ArrayList<String> speechArray;

    // To add filtered Speech array that is with ax_stream and not otherise.
    private ArrayList<String> filteredBlockArray;

    // This is for
    private ArrayList<MediaMetaData> seqArrayList;
    private ExpressionKeyFrame expressionKeyFrame;

    private LinkedList<ExpressionKeyFrame> expressionKeyLinkedList;
    private long currentExpressionIndex = 0;
    private long currentEndExpressionIndex = 0;
    private MediaMetaData mediaMetaData;
    private InternetCheckThread internetCheckThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        aaa();
    }

    private void init() {
        Resume = findViewById(R.id.Resume);
        lastSeekPosition = findViewById(R.id.lastSeekPosition);
        stop = findViewById(R.id.stop);
        SeekTo = findViewById(R.id.SeekTo);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        loopCount = findViewById(R.id.loopCount);
        loopOff = findViewById(R.id.loopOff);
        loopOnce = findViewById(R.id.loopOnce);
        loopOnce = findViewById(R.id.loopOnce);
        loopInfinite = findViewById(R.id.loopInfinite);
        retryPlayResume = findViewById(R.id.retryPlayResume);
        textCurrentTime = findViewById(R.id.textCurrentTime);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        textTotalDuration = findViewById(R.id.textTotalDuration);


        Resume.setOnClickListener(this);
        lastSeekPosition.setOnClickListener(this);
        stop.setOnClickListener(this);
        SeekTo.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        loopCount.setOnClickListener(this);
        loopOff.setOnClickListener(this);
        loopOnce.setOnClickListener(this);
        loopInfinite.setOnClickListener(this);
        retryPlayResume.setOnClickListener(this);
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            long currentDuration = streamingManager.lastSeekPosition();
            textCurrentTime.setText(miliSecondsToTimer(currentDuration));
        }
    };

    public String miliSecondsToTimer(long miliSeconds) {

        String timerString = "";
        String secondsString = "";

        int hours = (int) (miliSeconds / (1000 * 60 * 60));
        int minutes = (int) (miliSeconds % (1000 * 60 * 60) / (1000 * 60));
        int seconds = (int) (miliSeconds % (1000 * 60 * 60) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;

        return timerString;

    }

    private void configAudioStreamer() {
        streamingManager = AudioStreamingManager.getInstance(this);
        streamingManager.subscribesCallBack(this);
        //Set PlayMultiple 'true' if want to playing sequentially one by one songs
        // and provide the list of songs else set it 'false'
        streamingManager.setPlayMultiple(true);
//        String response = loadJSONFromAsset(this);
        listOfSongs.add(mediaMetaData);
        streamingManager.setMediaList(listOfSongs);
        currentSong = mediaMetaData;
        Log.d(TAG, "configAudioStreamer: " + currentSong.toString());
        playSong(currentSong);
        handler = new Handler();

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
        Log.d(TAG, "currentSeekBarPosition: " + progress);
        mediaMetaData.setLastSeekBarProgres(progress);
        Constants.getInstance(this).currentProgress = progress;
        if (streamingManager.isPlaying()) {
            if (streamingManager.getDuration() > 0) {
//                parseKeyFrames(progress);
                int duration = (streamingManager.getDuration() / 1000) * 60; // In seconds
                int due = (streamingManager.getDuration() - streamingManager.lastSeekPosition()) / 1000;
                int pass = duration - due;

                Log.e(TAG, "updateSeekBar: " + pass + " seconds");
                Log.e(TAG, "duration" + duration + " seconds");
                Log.e(TAG, "due" + due + " seconds");


                long tenPercent = (duration * totalPercentage) / 100;
                Log.d(TAG, "currentSeekBarPosition: assmt_time10per " + tenPercent);
            }
        }
        parseKeyFrames(progress);

    }

    @Override
    public void playCurrent(int indexP, MediaMetaData currentAudio) {
        Log.d(TAG, "x: currentAudio " + currentAudio);
        showMediaInfo(currentAudio);
    }

    @Override
    public void playNext(int indexP, MediaMetaData currentAudio) {
        Log.d(TAG, "playNext: currentAudio " + currentAudio);
    }

    @Override
    public void playPrevious(int indexP, MediaMetaData currentAudio) {
        Log.d(TAG, "playPrevious: currentAudio " + currentAudio);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMediaPlayerError(Object cause) {
        Log.d(TAG, "onError onMediaPlayerError: cause " + cause);
        if (cause.toString().contains("1")) {
            internetCheckThread = new InternetCheckThread(MainActivity.this, currentSessionCallback);
            internetCheckThread.start();
            if (internetCheckThread != null && threadIsAlive()) {
                internetCheckThread.maxRetries(30);
            }
            streamingManager.retryResume();
        }
    }

    public boolean threadIsAlive() {
        boolean threadAlive = false;
        if (internetCheckThread != null) {
            if (internetCheckThread.isAlive()) {
                threadAlive = true;
            }
        }
        return threadAlive;
    }

    @Override
    public void onError(Object cause) {
        Log.d(TAG, "onError: " + cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRegainInternet() {
        if (internetCheckThread != null && threadIsAlive()) {
            internetCheckThread.stopThread();
        }
        streamingManager.retryResume();
    }


    private void showMediaInfo(MediaMetaData media) {
        Log.d(TAG, "showMediaInfo: media " + media);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                Toast.makeText(this, "" + TimeUnit.MILLISECONDS.toMinutes(streamingManager.lastSeekPosition()), Toast.LENGTH_SHORT).show();
                playerSeekBar.setProgress((int) ((float) TimeUnit.MILLISECONDS.toSeconds(streamingManager.lastSeekPosition())));

                break;
            case R.id.stop:
                streamingManager.onStop();
                streamingManager.cleanupPlayer(this, true, true);
                break;
            case R.id.SeekTo:
                if (currentSong != null) {
                    Log.d(TAG, "onClick: " + currentSong.getOffsetStart());
                    streamingManager.onSeekTo(19798000);
                    playerSeekBar.setProgress((int) ((float) TimeUnit.MILLISECONDS.toSeconds(streamingManager.getDuration() - 100000)));
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
            case R.id.retryPlayResume:
                streamingManager.retryResume();
                break;
        }

    }

    public void aaa() {

        init();
        this.streamingManager = AudioStreamingManager.getInstance(this);
        playerSeekBar.setMax(100);

        listOfSongs = new ArrayList<MediaMetaData>();
        seqArrayList = new ArrayList<>();
        filteredBlockArray = new ArrayList<>();
        speechArray = new ArrayList<>();
        speech = new ArrayList<>();

        speech.add("<block> \n<expression>{\"tx\":{\"type\":11,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"data\":\"\",\"length\":0,\"id\":0,\"time\":-1,\"data_dsp\":\"\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"INTRO\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"6000\",\"offsetEnd\":\"20000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"blink.json\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":0,\"color\":8,\"time\":0,\"rate\":0,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-89.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-1\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"30000\",\"offsetEnd\":\"70000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-89.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-90.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-2\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"72000\",\"offsetEnd\":\"80000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-90.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-91.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-3\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"90000\",\"offsetEnd\":\"110000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-91.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-4\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"120000\",\"offsetEnd\":\"112000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-98.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-5\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"132000\",\"offsetEnd\":\"124000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-98.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-99.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-6\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"144000\",\"offsetEnd\":\"174000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-99.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"Today,  World Day , for Audiovisual Heritage , is celebrated , all across the globe.\",\"length\":84,\"id\":0,\"time\":0,\"data_dsp\":\"Today, World Day  for Audiovisual Heritage  is celebrated  all across the globe.\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"blink.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":100,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-407,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"A team of Indian , and Canadian researchers , discovered the earliest evidence , of dairy farming , in the Indus Valley Civilisation.\",\"length\":133,\"id\":0,\"time\":0,\"data_dsp\":\"A team of Indian  and Canadian researchers  discovered the earliest evidence  of dairy farming  in the Indus Valley Civilisation.\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"India_flag.png\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":100,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-891,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"That\\u0027s all for now!\",\"length\":19,\"id\":0,\"time\":0,\"data_dsp\":\"That\\u0027s all for now!\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"very happy.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":12,\"color\":8,\"time\":100,\"rate\":75,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":569,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");


        speechArray = speech;
        for (int i = 0; i < speechArray.size(); i++) {
            parseAIMLexpression(speechArray.get(i));
        }
        fillKeyFrames(seqArrayList, filteredBlockArray);

    }

    public void parseAIMLexpression(final String string2) {

        if (string2 != null && string2.length() > 0) {
            try {
                Log.e(TAG, "string is xml " + string2);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                DocumentBuilder builder = factory.newDocumentBuilder();
                String string = string2.replaceAll("[^\\x20-\\x7e]", "");

                StringBuilder xmlStringBuilder = new StringBuilder();
                xmlStringBuilder.append(string);
                ByteArrayInputStream input = new ByteArrayInputStream(
                        xmlStringBuilder.toString().getBytes("UTF-8"));
                org.w3c.dom.Document doc = builder.parse(input);

                if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase("block")) {
                    Log.e("ZZ", "check data");
                    return;
                }

                NodeList nList = doc.getElementsByTagName("expression");

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Node eElement = nNode;
                        NodeList nodeList = ((Node) eElement).getChildNodes();

                        for (int k = 0; k < nodeList.getLength(); k++) {
                            Node n1 = nodeList.item(k);
                            String value = n1.getTextContent();
                            if (value.contains("ax_stream")) {
                                filteredBlockArray.add("<block><expression>" + nList.item(i).getTextContent() + "</expression></block>");
                                if (value.contains(".mp3")) {
                                    ExpressionModel badJson = new Gson().fromJson(value, ExpressionModel.class);
                                    mediaMetaData = new Gson().fromJson(badJson.getAx_stream().getSeq().get(k).toString(), MediaMetaData.class);
                                    seqArrayList.add(mediaMetaData);
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "error in parseAIMLexpression the xml" + e.getMessage());
            }
        }
    }

    // This is to create a json for playing expressions at specific time rate
    private void fillKeyFrames(ArrayList<MediaMetaData> seqArrayList, ArrayList<String> filteredBlockArray) {
        try {
            expressionKeyLinkedList = null;
            expressionKeyLinkedList = new LinkedList<ExpressionKeyFrame>();
            List<String> scriptExpressions;

            for (int i = 0; i < seqArrayList.size(); i++) {
                expressionKeyFrame = new ExpressionKeyFrame();
                scriptExpressions = new ArrayList<>();

                expressionKeyFrame.setId(i);
                long startTimePositive = Math.abs(Long.parseLong(seqArrayList.get(i).getOffsetStart()));
                currentExpressionIndex = Long.parseLong(seqArrayList.get(0).getOffsetStart());
                currentEndExpressionIndex = Long.parseLong(seqArrayList.get(0).getOffsetEnd());
                expressionKeyFrame.setStartTime(startTimePositive);
                expressionKeyFrame.setEndTime(Long.parseLong(seqArrayList.get(i).getOffsetEnd()));
                int j = i;
                int incrementalValue = i+j;
                for (j = incrementalValue; j <= incrementalValue+1; j++) {
                    scriptExpressions.add(filteredBlockArray.get(j));
                }
                expressionKeyFrame.setToDoTask(scriptExpressions);
                expressionKeyFrame.setSlotPosition(seqArrayList.get(i).getSlotPosition());
                Log.d(TAG, "fillKeyFrames: " + expressionKeyFrame.toString());
                expressionKeyLinkedList.add(expressionKeyFrame);
            }

            configAudioStreamer();

        } catch (Exception e) {
            Log.e(TAG, "fillKeyFrames: "  + e.getMessage() );
        }
    }

    public void parseKeyFrames(long seekTimeInLong) {
        try {
            Iterator<ExpressionKeyFrame> iterator = expressionKeyLinkedList.iterator();
            if (iterator.hasNext()) {
                if (seekTimeInLong >= currentExpressionIndex && seekTimeInLong <= currentExpressionIndex) {
                    currentExpressionIndex = iterator.next().getStartTime();
                    currentEndExpressionIndex = iterator.next().getEndTime();
                    Log.d(TAG, "playExpression parseKeyFrames: seekTimeInLong: inside " + seekTimeInLong  + " currentExpressionIndex " + currentExpressionIndex);

                    iterator.remove();
                    playExpression(iterator.next().getToDoTask());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "parseKeyFrames: Exception " + e.getMessage() );
        }
    }

    private void playExpression(List<String> arrayList) {
        try {
            for (int i = 0; i <= 0; i++) {
                Log.d(TAG, "playExpression:parseKeyFrames " + arrayList.get(i));
            }
        } catch (Exception e) {
            Log.e(TAG, "playExpression:Exception ", e.getCause());
        }
    }
}