package com.audioplayer.androidaudiostreaming;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private Button mButtonPlay;
    private Button mButtonStop;
    private Button mButtonPause;
    private Button mButtonResume;

    private SeekBar mSeekBar;

    private TextView mPass;
    private TextView mDuration;
    private TextView mDue;

    private MediaPlayer mPlayer;
    private Handler mHandler;
    private Runnable mRunnable;

    private int mInterval = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Get the widget reference from xml layout
        mButtonPlay = findViewById(R.id.btn_play);
        mButtonStop = findViewById(R.id.btn_stop);
        mButtonPause = findViewById(R.id.btn_pause);
        mButtonResume = findViewById(R.id.btn_resume);
        mSeekBar = findViewById(R.id.seek_bar);
        mPass = findViewById(R.id.tv_pass);
        mDuration = findViewById(R.id.tv_duration);
        mDue = findViewById(R.id.tv_due);

        // Initialize the handler
        mHandler = new Handler();

        // Initially disable the buttons
        mButtonStop.setEnabled(false);
        mButtonPause.setEnabled(false);
        mButtonResume.setEnabled(false);

        // Click listener for playing button
        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If media player another instance already running then stop it first
                stopPlaying();

                // Initialize media player
                mPlayer = MediaPlayer.create(mContext,R.raw.happyfriendshipday);

                // Start the media player
                mPlayer.start();
                Toast.makeText(mContext,"Media Player is playing.", Toast.LENGTH_SHORT).show();

                // Get the current audio stats
                getAudioStats();
                // Initialize the seek bar
                initializeSeekBar();

                // Disable the start and resume button
                mButtonPlay.setEnabled(false);
                mButtonResume.setEnabled(false);
                mButtonPause.setEnabled(true);
                mButtonStop.setEnabled(true);
            }
        });

        // Set a click listener for top playing button
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                mButtonStop.setEnabled(false);
                mButtonResume.setEnabled(false);
                mButtonPause.setEnabled(false);
                mButtonPlay.setEnabled(true);
            }
        });

        // Click listener for pause button
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer!=null && mPlayer.isPlaying()){
                    /*
                        void pause ()
                            Pauses playback. Call start() to resume.

                        Throws
                            IllegalStateException : if the internal player engine has not been initialized.
                    */
                    mPlayer.pause();
                    mButtonPause.setEnabled(false);
                    mButtonStop.setEnabled(false);
                    mButtonPlay.setEnabled(false);
                    mButtonResume.setEnabled(true);
                    mButtonPlay.setEnabled(true);
                }
            }
        });

        // Click listener for resume button
        mButtonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer!=null){
                    /*
                        void seekTo (int msec)
                            Seeks to specified time position. Same as seekTo(long, int) with mode = SEEK_PREVIOUS_SYNC.

                        Parameters
                            msec int : the offset in milliseconds from the start to seek to

                        Throws
                            IllegalStateException : if the internal player engine has not been initialized
                    */
                    //mPlayer.seekTo(mPlayer.getCurrentPosition());

                    /*
                        void start ()
                            Starts or resumes playback. If playback had previously been paused,
                            playback will continue from where it was paused. If playback had been
                            stopped, or never started before, playback will start at the beginning.

                        Throws
                            IllegalStateException : if it is called in an invalid state
                    */

                    // Start the media player from paused position, so at this situation do not
                    // need to call seekTo() method
                    mPlayer.start();
                    mButtonResume.setEnabled(false);
                    mButtonPause.setEnabled(true);
                    mButtonPlay.setEnabled(true);
                    mButtonStop.setEnabled(true);
                }
            }
        });

        // Set a change listener for seek bar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mPlayer!=null && b){
                    mPlayer.seekTo(i*mInterval);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void stopPlaying(){
        // If media player is not null then try to stop it
        if(mPlayer!=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            Toast.makeText(mContext,"Stop playing.",Toast.LENGTH_SHORT).show();
            if(mHandler!=null){
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    protected void getAudioStats(){
        int duration  = mPlayer.getDuration()/1000; // In milliseconds
        int due = (mPlayer.getDuration() - mPlayer.getCurrentPosition())/1000;
        int pass = duration - due;

        mPass.setText("" + pass + " seconds");
        mDuration.setText("" + duration + " seconds");
        mDue.setText("" + due + " seconds");
    }

    protected void initializeSeekBar(){
        mSeekBar.setMax(mPlayer.getDuration()/mInterval);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mPlayer!=null){
                    int mCurrentPosition = mPlayer.getCurrentPosition()/mInterval; // In milliseconds
                    mSeekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                mHandler.postDelayed(mRunnable,mInterval);
            }
        };
        mHandler.postDelayed(mRunnable,mInterval);
    }
}