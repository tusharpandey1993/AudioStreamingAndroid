package com.audioplayer.androidaudiostreaming;

import com.audioplayer.androidaudiostreaming.mediaPlayer.models.MediaMetaData;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static Utility instance;
    public static Context mContext;

    public static Utility getInstance(Context context) {
        if (instance == null) {
            synchronized (Utility.class) {
                instance = new Utility();
                instance.mContext = context;
            }
        }
        return instance;
    }

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
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


    public List<MediaMetaData> getMusicList(String response, String name) {
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
                infoData.setOffsetStart(musicObj.optString("offsetStart"));
                infoData.setOffsetEnd(musicObj.optString("offsetEnd"));
                listArticle.add(infoData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listArticle;
    }

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

}
