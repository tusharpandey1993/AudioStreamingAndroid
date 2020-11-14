package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.util.Log;
import com.audioplayer.androidaudiostreaming.mediaPlayer.interfaces.CurrentSessionCallback;

public class InternetCheckThread extends Thread {

    private final String TAG = "AnalyticsQueue";

    private static InternetCheckThread instance = null;
    private static Context mContext;
    private static boolean demonFlag;
    private int maxRetires;
    private int counter;
    private static CurrentSessionCallback mCurrentSessionCallback;

   /* public static InternetCheckThread getInstance() {
        mContext = context;
        if (instance == null) {
            instance = new InternetCheckThread();

            Log.e("analytics: ", "new instance");
        }
        return instance;
    }*/

    public InternetCheckThread(Context context, CurrentSessionCallback currentSessionCallback) {
        mCurrentSessionCallback = currentSessionCallback;
        Thread thread = new Thread(instance);
        thread.start();
        this.mContext = context;
        demonFlag = true;
    }

    public void stopThread() {
        demonFlag = false;
    }

    public void maxRetries(int noOfRetries) {
        maxRetires = noOfRetries;
    }

    @Override
    public void run() {

        while (true) {
            try {
                while (demonFlag) {
                    try {
                        if(counter >= maxRetires) {
                            counter ++;
                            if (Utils.getInstance().isNetworkAvailable(mContext)) {
                                Log.d(TAG, "run:isNetworkAvailable  ");
                                if(mCurrentSessionCallback != null) {
                                    mCurrentSessionCallback.onRegainInternet();
                                }
                                demonFlag = false;
                            }
                            // For Reject Call before Calling
                            Thread.sleep(1000);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Error in getting pending messages: " + e.getMessage());
                        break;
                    }
                }
                Thread.interrupted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
