package com.audioplayer.androidaudiostreaming;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.audioplayer.androidaudiostreaming.GifLib.GifImageView;
import com.audioplayer.androidaudiostreaming.mediaPlayer.controller.AudioStreamingManager;
import com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces.CurrentSessionCallback;
import com.audioplayer.androidaudiostreaming.model.ExpressionKeyFrame;
import com.audioplayer.androidaudiostreaming.model.MediaMetaData;
import com.audioplayer.androidaudiostreaming.model.ExpressionModel;
import com.google.gson.Gson;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
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
    private boolean firstTime = false;
    private long currentExpressionIndex = 0;
    private long currentEndExpressionIndex = 0;
    List<String> arrayList;
    private MediaMetaData mediaMetaData;
    private InternetCheckThread internetCheckThread;
    private int firstExp = 0;
    private int firstExpCounter = 0;
    private LinkedList<String> initialExpression;
    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: reading json " + ReadConfigurationData("/sdcard/Download/stop_speak1.txt"));


//        eventExistsOnCalendar("Event Name", new Date().getTime(),  new Date().getTime());
        pushAppointmentsToCalender(MainActivity.this, "Title", "Information", "Mumbai", 1,new Date().getTime(),true,false);
    }

    public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
        /***************** Event: note(without alert) *******************/

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 12); // id, We need to choose from
        // our mobile for primary
        // its 1
        eventValues.put("title", title);
        eventValues.put("description", addInfo);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        // values.put("allDay", 1); //If it is bithday alarm or such
        // kind (which should remind me for whole day) 0 for false, 1
        // for true
        eventValues.put("eventStatus", status); // This information is
        // sufficient for most
        // entries tentative (0),
        // confirmed (1) or canceled
        // (2):
        eventValues.put("eventTimezone", "UTC/GMT +2:00");
        /*Comment below visibility and transparency  column to avoid java.lang.IllegalArgumentException column visibility is invalid error */

    /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            /***************** Event: Reminder(with alert) Adding reminder to event *******************/

            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }

        return eventID;

    }


    public boolean eventExistsOnCalendar(String eventTitle, long startTimeMs, long endTimeMs) {
        if (eventTitle == null || "".equals(eventTitle)) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "eventExistsOnCalendar: Permission not granted ");
            return false;
        }
        // If no end time, use start + 1 hour or = 1 day. Query is slow if searching a huge time range
        if (endTimeMs <= 0) {
            endTimeMs = startTimeMs + 1000 * 60 * 60; // + 1 hour
        }

        final ContentResolver resolver = this.getContentResolver();
        final String[] duplicateProjection = {CalendarContract.Events.TITLE}; // Can change to whatever unique param you are searching for
        Cursor cursor =
                CalendarContract.Instances.query(
                        resolver,
                        duplicateProjection,
                        startTimeMs,
                        endTimeMs,
                        '"' + eventTitle + '"');

        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }

        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            if (eventTitle.equals(title)) {
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }


    public void hh (GifImageView gifImageView) {
        try {
            InputStream is = null;
            try {
                is = this.getAssets().open("slow_tele.gif");
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] fileBytes = new byte[0];
            try {
                fileBytes = new byte[is.available()];
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.read(fileBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gifImageView.setBytes(fileBytes);
            gifImageView.startAnimation();
            gifImageView.setLoopCount(-1);
        } catch (Exception e) {
            Log.e(TAG, "hh:Exception " + e.getMessage() );
            e.printStackTrace();
        }

    }


    // convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
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

    private void configAudioStreamer(MediaMetaData mediaMetaData) {
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
//        Log.d(TAG, "currentSeekBarPosition: " + progress);
        mediaMetaData.setLastSeekBarProgres(progress);
        Constants.getInstance(this).currentProgress = progress;
        if (streamingManager.isPlaying()) {
            if (streamingManager.getDuration() > 0) {
//                parseKeyFrames(progress);
                int duration = (streamingManager.getDuration() / 1000) * 60; // In seconds
                int due = (streamingManager.getDuration() - streamingManager.lastSeekPosition()) / 1000;
                int pass = duration - due;

                /*Log.e(TAG, "updateSeekBar: " + pass + " seconds");
                Log.e(TAG, "duration" + duration + " seconds");
                Log.e(TAG, "due" + due + " seconds");
*/

                long tenPercent = (duration * totalPercentage) / 100;
//                Log.d(TAG, "currentSeekBarPosition: assmt_time10per " + tenPercent);
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
/*            subscribesCallBack(this);
            if (streamingManager != null) {
                streamingManager.subscribesCallBack(this);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        try {
/*            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
/*            if (streamingManager != null) {
                streamingManager.unSubscribeCallBack();
            }*/
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
//                configAudioStreamer();
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
                    streamingManager.onSeekTo(10000);
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

        speech.add("<block>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"INTRO\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"200\",\"offsetEnd\":\"21100\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"blink.json\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":0,\"color\":8,\"time\":0,\"rate\":0,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-89.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-1\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19793000\",\"offsetEnd\":\"-19746000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-89.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-90.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-2\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19746000\",\"offsetEnd\":\"-19689000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-90.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-91.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-3\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19689000\",\"offsetEnd\":\"-19659000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-91.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-4\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19659000\",\"offsetEnd\":\"-19611000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-98.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-5\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19611000\",\"offsetEnd\":\"-19590000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":4,\"seq\":[{\"linear\":0,\"angular\":4,\"time\":1,\"type\":4,\"id\":0},{\"linear\":0,\"angular\":-5,\"time\":1,\"type\":4,\"id\":1},{\"linear\":0,\"angular\":5,\"time\":1,\"type\":4,\"id\":2},{\"linear\":0,\"angular\":-4,\"time\":1,\"type\":4,\"id\":3}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-98.png\",\"rate\":0,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":8000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":0,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-99.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-6\",\"trackNumber\":0,\"totalTrackCount\":0,\"offsetStart\":\"-19590000\",\"offsetEnd\":\"-19566000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-99.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression>\n" +
                "  <expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"That\\u0027s all for now!\",\"length\":19,\"id\":0,\"time\":0,\"data_dsp\":\"That\\u0027s all for now!\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"very happy.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":12,\"color\":8,\"time\":100,\"rate\":75,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":806,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
//        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"Today,  World Day , for Audiovisual Heritage , is celebrated , all across the globe.\",\"length\":84,\"id\":0,\"time\":0,\"data_dsp\":\"Today, World Day  for Audiovisual Heritage  is celebrated  all across the globe.\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"blink.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":100,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-407,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
//        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"A team of Indian , and Canadian researchers , discovered the earliest evidence , of dairy farming , in the Indus Valley Civilisation.\",\"length\":133,\"id\":0,\"time\":0,\"data_dsp\":\"A team of Indian  and Canadian researchers  discovered the earliest evidence  of dairy farming  in the Indus Valley Civilisation.\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"India_flag.png\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":8,\"time\":100,\"rate\":15,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-891,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
//        speech.add("<block>\n<expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"data\":\"That\\u0027s all for now!\",\"length\":19,\"id\":0,\"time\":0,\"data_dsp\":\"That\\u0027s all for now!\",\"dsp_size\":0}]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":11,\"size\":0,\"imagetype\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"frame\":\"very happy.json\",\"rate\":0,\"id\":0,\"loop\":1}]},\"rx\":{\"type\":5,\"size\":24,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":12,\"color\":8,\"time\":100,\"rate\":75,\"id\":0}]},\"dx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":569,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");


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
                expressionKeyFrame.setStartTime(startTimePositive);
                expressionKeyFrame.setEndTime(Long.parseLong(seqArrayList.get(i).getOffsetEnd()));
                int j = i;
                int incrementalValue = i+j;
//                Log.d(TAG, "fillKeyFrames: incrementalValue " + incrementalValue + "  j " + j);
                for (j = incrementalValue; j <= incrementalValue+1; j++) {
//                    Log.d(TAG, "fillKeyFrames: j " +j);
                    if(filteredBlockArray != null && j <= filteredBlockArray.size()-1) {
                        scriptExpressions.add(filteredBlockArray.get(j));
                    }

                }
                expressionKeyFrame.setToDoTask(scriptExpressions);
                Log.d(TAG, "fillKeyFrames: " + expressionKeyFrame.toString());
                expressionKeyLinkedList.add(expressionKeyFrame);
            }

            configAudioStreamer(seqArrayList.get(0));

        } catch (Exception e) {
            Log.e(TAG, "fillKeyFrames: Exception "  + e.getMessage() );
        }
    }


    public void parseKeyFrames(long seekTimeInLong) {
        try {
            Iterator<ExpressionKeyFrame> iterator = expressionKeyLinkedList.iterator();
            ExpressionKeyFrame expressionKeyFrame = new ExpressionKeyFrame();
            initialExpression = new LinkedList<String>();

            if (iterator.hasNext()) {
                expressionKeyFrame = iterator.next();

                if(firstExp == 0 || firstExp <= 2) {

                    for(int i = 0; i < expressionKeyFrame.getToDoTask().size(); i++) {
                        Log.d(TAG, "aa playExpression: parseKeyFrames: " + expressionKeyFrame.getToDoTask().get(i));
                        initialExpression.add(expressionKeyFrame.getToDoTask().get(i));
                    }
                    firstExp++;
                    iterator.remove();
                    if(firstExp == 2) {
                        Log.d(TAG, "aa playExpression:  play expression from here false "  + initialExpression.size() );
//                        playExpression(false);
                    }
                    return;
                }
//                expressionKeyFrame = iterator.next();
                currentExpressionIndex = expressionKeyFrame.getStartTime();
                currentEndExpressionIndex = expressionKeyFrame.getEndTime();
                Log.d(TAG, "parseKeyFrames: seekTimeInLong " + seekTimeInLong + " currentExpressionIndex " + currentExpressionIndex + " currentEndExpressionIndex " + currentEndExpressionIndex);
                if (seekTimeInLong >= currentExpressionIndex && seekTimeInLong <= currentEndExpressionIndex) {
                    arrayList = expressionKeyFrame.getToDoTask();
                    playExpression(false);
                    Log.d(TAG, "playExpression parseKeyFrames: seekTimeInLong: inside " + seekTimeInLong + " currentExpressionIndex " + currentExpressionIndex + " currentEndExpressionIndex " + currentEndExpressionIndex + " arrayList " + arrayList.get(0));
                    expressionKeyFrame = null;
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "parseKeyFrames: Exception " + e.getMessage());
        }
    }

    public void playExpression(boolean playSecond) {
        try {
            Log.d(TAG, "aa playExpression:firstExp " + firstExp);
            if(firstExp <= 2) {
                Log.d(TAG, "aa playExpression: initialExpression.size() " + initialExpression.size());
                Iterator<String> iterator = initialExpression.iterator();
                if (iterator.hasNext()) {
                    String expression = iterator.next();
                    iterator.remove();
                    playSystemExpressionWithCallBack(expression);
                }
            }
            String secondExp = "";
            if (arrayList != null && arrayList.size() > 0) {
                if (!playSecond) {
                    Log.d(TAG, "aa playExpression: playSecond " + playSecond + arrayList.get(0));
//                    playSystemExpressionWithCallBack("<block><expression>{\"tx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ax\":{\"type\":0,\"size\":0,\"loop\":0,\"loop1\":0,\"seqCount\":0,\"seq\":[]},\"ax_stream\":{\"type\":15,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"id\":0,\"title\":\"NEWS-4\",\"trackNumber\":0,\"totalTrackCount\":0,\"" +
//                            "offsetStart\":\"90000\",\"offsetEnd\":\"120000\",\"site\":\"https://miko2.s3.ap-south-1.amazonaws.com/test/KidNuz_08_12_20+(1).mp3\"}]},\"mx\":{\"type\":4,\"size\":0,\"motion_type\":4,\"loop\":3,\"seqCount\":6,\"seq\":[{\"linear\":-850,\"angular\":0,\"time\":6,\"type\":1,\"id\":0},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":1},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":2},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":3},{\"linear\":-850,\"angular\":0,\"time\":3,\"type\":1,\"id\":4},{\"linear\":850,\"angular\":0,\"time\":3,\"type\":1,\"id\":5}]},\"mx2\":{\"kp\":0,\"ki\":0,\"kd\":0,\"target_angle\":0,\"zonea\":0,\"zoneb\":0,\"positionScaleA\":0,\"positionScaleB\":0,\"positionScaleC\":0,\"velocityScaleStop\":0,\"velocityScaleMove\":0,\"onewheel\":0,\"falst\":0,\"mston\":0,\"mston_flag\":0,\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"steer\":0,\"seqCount\":0,\"seq\":[]},\"mx3\":{\"type\":0,\"size\":0,\"motion_type\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"ix1\":{\"type\":3,\"size\":0,\"imagetype\":1,\"loop\":0,\"seqCount\":1,\"seq\":[{\"frame\":\"b-97.png\",\"rate\":2,\"id\":0,\"loop\":0}]},\"rx\":{\"type\":5,\"size\":0,\"loop\":0,\"seqCount\":1,\"seq\":[{\"pattern\":5,\"color\":7,\"time\":0,\"rate\":17,\"id\":0}]},\"dx\":{\"type\":13,\"size\":0,\"loop\":1,\"seqCount\":1,\"seq\":[{\"length\":0,\"id\":0,\"time\":7000}]},\"atx\":{\"type\":0,\"size\":0,\"loop\":0,\"seqCount\":0,\"seq\":[]},\"msg\":{\"seqCount\":0,\"seq\":[]},\"id\":-928,\"vel2_shape\":false,\"fallstatus\":0,\"chargerStatus\":false}</expression></block>");
                } else {
                    if(arrayList.size() > 1) {
                        secondExp = arrayList.get(1);
                        arrayList.clear();
                        if(!TextUtils.isEmpty(secondExp)) {
                            Log.d(TAG, "aa playExpression: playSecond "+ playSecond + secondExp);
//                            playSystemExpressionWithCallBack(secondExp);
                            secondExp = "";
                        }
                    }
                }
            } else {
                Log.e(TAG, "aa playExpression: the arraylist is empty " );
            }
        } catch (Exception e) {
            Log.e(TAG, "playExpression:Exception " + e.getMessage());
        }
    }

    private void playSystemExpressionWithCallBack(String expression) {
        Log.d(TAG, "callMeAgain: " + expression);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playExpression(true);
            }
        },100);

    }

    public String ReadConfigurationData(String path) {
        StringBuilder buf = null;
        try {
            AssetManager manager = this.getAssets();
            buf = new StringBuilder();
            InputStream json = manager.open(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

        } catch (Exception e) {
            Log.e(TAG, "ReadConfigurationData: " + e.getMessage());
        }

        return buf.toString();
    }
}