package com.audioplayer.androidaudiostreaming.zoom;

import android.content.Context;
import android.util.Log;

import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;
import us.zoom.sdk.ZoomSDKRawDataMemoryMode;

public class InitAuthSDKHelper implements AppConstant, ZoomSDKInitializeListener {

    private final static String TAG = "InitAuthSDKHelper";

    private static InitAuthSDKHelper mInitAuthSDKHelper;

    private ZoomSDK mZoomSDK;

    private InitAuthSDKCallback mInitAuthSDKCallback;

    private InitAuthSDKHelper() {
        mZoomSDK = ZoomSDK.getInstance();
    }

    public synchronized static InitAuthSDKHelper getInstance() {
        mInitAuthSDKHelper = new InitAuthSDKHelper();
        return mInitAuthSDKHelper;
    }

    /**
     * init sdk method
     */
    public void initSDK(Context context, InitAuthSDKCallback callback) {
        if (!mZoomSDK.isInitialized()) {
            mInitAuthSDKCallback = callback;

            ZoomSDKInitParams initParams = new ZoomSDKInitParams();
            initParams.appKey = "mu9ttrSjxtZJcy9ywJ9CxAh6w3yjiBTcURRf"; // TODO: Retrieve your SDK key and enter it here
            initParams.appSecret = "4FigqN6kIU2lgSNWamJuF07zuEazz4EytRYn"; // TODO: Retrieve your SDK secret and enter it here
//            initParams.jwtToken = SDK_JWTTOKEN;
            initParams.enableLog = true;
            initParams.enableGenerateDump =true;
            initParams.logSize = 50;
            initParams.domain= WEB_DOMAIN;
            initParams.videoRawDataMemoryMode = ZoomSDKRawDataMemoryMode.ZoomSDKRawDataMemoryModeStack;



            Log.d(TAG, "onCheckedChanged:appKey "  + initParams.appKey);
            Log.d(TAG, "onCheckedChanged:appSecret "  + initParams.appSecret);
            Log.d(TAG, "onCheckedChanged:domain "  + initParams.domain);
            Log.d(TAG, "onCheckedChanged:jwtToken "  + initParams.jwtToken);
            mZoomSDK.initialize(context, this, initParams);
        }
    }

    /**
     * init sdk callback
     *
     * @param errorCode         defined in {@link us.zoom.sdk.ZoomError}
     * @param internalErrorCode Zoom internal error code
     */
    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (mInitAuthSDKCallback != null) {
            mInitAuthSDKCallback.onZoomSDKInitializeResult(errorCode, internalErrorCode);
        }
    }

    @Override
    public void onZoomAuthIdentityExpired() {
        Log.e(TAG,"onZoomAuthIdentityExpired in init");
    }

    public void reset(){
        mInitAuthSDKCallback = null;
    }
}
