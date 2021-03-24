package com.audioplayer.androidaudiostreaming;

import android.util.Log;
import android.view.View;


public interface OnClickInterface {

    default void  onClick(View view, int position){

        Log.d("OnClickInterface", "onClick: " + position);
    }

    default void  onClickChildView(View view,String viewID, int position){

        Log.d("onClickChildView", "onClick: " + position);
    }

    //Crash handled by @Bhakti
    default void  onAppsStoreChildViewClicked(View view,String viewID, int pos,String status,int AppID){

        Log.d("onClickChildView", "onClick: " + pos);
    }
}
