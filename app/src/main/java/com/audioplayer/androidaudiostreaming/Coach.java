package com.audioplayer.androidaudiostreaming;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.audioplayer.androidaudiostreaming.model.GetNextScheduledClassesRes;
import com.audioplayer.androidaudiostreaming.model.MikoCoachClassesEntity;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Coach extends AppCompatActivity implements CoachAdapter.ItemListener {

    private static final String TAG = "Coach";
    private RecyclerView coach_list;
    private TextView day, time, classTitle, tutorName, timeRemainingInHour, msh;
    private Button left,right;
    int rvSelectedItemPos = 0;
    private boolean rightPressed = false;
    private List<MikoCoachClassesEntity> arrayList = new ArrayList<>();
    private CoachAdapter coachAdapter;
    private static Coach mInstance;

    public static synchronized Coach getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach);
/*

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime do_Not_Do_This = ZonedDateTime.now();
            Log.d(TAG, "ZonedDateTime onCreate: " + do_Not_Do_This);
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        Log.d(TAG, "timeStamp onCreate: " + timeStamp);


        long currentTime = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "currentTime onCreate: " + currentTime);
*/
//        1606530136000
        /*long bEtime = 1606744860000l;
        long backEndMins = TimeUnit.MILLISECONDS.toMinutes(bEtime);
        long CurrentMins = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        Log.d(TAG, "backEndLong onCreate: " + backEndMins);
        Log.d(TAG, "CurrentMins onCreate: " + CurrentMins);
        Log.d(TAG, "CurrentMins local time : " + Calendar.getInstance().getTime());
        Log.d(TAG, "CurrentMins Calendar.getInstance().getTimeInMillis() : " + Calendar.getInstance().getTimeInMillis());
        Log.d(TAG, "onCreate: " + (backEndMins - CurrentMins));
        Log.d(TAG, "onCreate: Time "  + System.currentTimeMillis());

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getTimeInstance() ;
        Log.d(TAG, "Time: " + dateFormat.format(date));
*/
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        String dateInString = "2020-12-01 13:00:00";
        try {
            Date date1 = sdf.parse(dateInString);

            Log.d(TAG, "onCreate: hello " + date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        /*Date temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-12-01 13:00:00");
        temp.getTime();*/

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        Log.d(TAG, "onCreate: " + currentLocalTime);

        long ss = new Date().getHours();
        long ss2 = new Date().getHours();
        long getMinutes = new Date().getMinutes();
        Log.d(TAG, "onCreate:localToUTC " + localToUTC(new Date().getTime()));
        /*long currentTimeLong = new Date().getTime();
        long currentTimeMins = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        long bEtime = 1606442460000l;
        long backEndLong = TimeUnit.MILLISECONDS.toMinutes(bEtime);
        Log.d(TAG, "onCreate:  + currentTimeLong " + currentTimeLong);
        Log.d(TAG, "onCreate:  + bEtime " + bEtime);
        Log.d(TAG, "onCreate:  + Long difference  " + (currentTimeLong - bEtime));
        Log.d(TAG, "onCreate:  + backEndLong " + backEndLong);
        Log.d(TAG, "onCreate:  + currentTimeMins " + currentTimeMins);
        Log.d(TAG, "onCreate: difference " + (currentTimeMins-backEndLong) );
        long time= System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        int mseconds = c.get(Calendar.MILLISECOND);
        Log.d(TAG, "onCreate: time " + time );*/
        init();

//1606458600000
//1606482000000  (UNIX_TIMESTAMP(DATE_FORMAT(CONVERT_TZ(`2020-11-30 14:01:00`,'UTC','Asia/Kolkata'),'%Y-%m-%d %h:%i:%s'))*1000)
        GetNextScheduledClassesRes getNextScheduledClassesRes = new GetNextScheduledClassesRes();

        getNextScheduledClassesRes = new Gson().fromJson(StringResponse(), GetNextScheduledClassesRes.class);
        Log.d(TAG, "onCreate: " + getNextScheduledClassesRes.toString());
        showNextScreen(getNextScheduledClassesRes);
    }
    public static long localToUTC(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            Date date = new Date(time);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String strDate = dateFormat.format(date);
            SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            Date utcDate = dateFormatLocal.parse(strDate);
            long utcMillis = utcDate.getTime();
            return utcMillis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    public String formatDate(String dateStr, String dateFormat, String requiredFormatDate) {
        if (dateStr == null) return null;
        String date = dateStr;
        SimpleDateFormat currentFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        SimpleDateFormat requiredFormat = new SimpleDateFormat(requiredFormatDate, Locale.getDefault());
        Date getDate = null;
        try {
            getDate = currentFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date = requiredFormat.format(getDate);
        return date;
    }
    private void init() {
        coach_list = findViewById(R.id.coach_list);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        msh = findViewById(R.id.msh);
        msh.setText(Constants.getInstance(this).BeforeClass);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvSelectedItemPos  > 0) {

                    if(rightPressed) {
                        rightPressed = false;
                        rvSelectedItemPos--;
                        rvSelectedItemPos--;
                    } else {
                        rvSelectedItemPos--;
                    }

                    coach_list.scrollToPosition(rvSelectedItemPos);
                    CoachAdapter.setRvSelectedItemPos(rvSelectedItemPos);
                }
                if (coachAdapter != null)
                    coachAdapter.notifyDataSetChanged();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rvSelectedItemPos == arrayList.size()-1) {
                    return;
                }
                if (rvSelectedItemPos < arrayList.size()) {
                    if(!rightPressed) {
                        rightPressed = true;
                        rvSelectedItemPos++;
                        rvSelectedItemPos++;
                    } else {

                        rvSelectedItemPos++;
                    }

                    coach_list.scrollToPosition(rvSelectedItemPos);
                    CoachAdapter.setRvSelectedItemPos(rvSelectedItemPos);
                }

                if (coachAdapter != null)
                    coachAdapter.notifyDataSetChanged();
            }
        });

    }

    private void showNextScreen(GetNextScheduledClassesRes getNextScheduledClassesRes) {
        try {
//            SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss h zzz").parse(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(0).getStart_time()).getTime();
//            Log.d(TAG, "showNextScreen:SimpleDateFormat  "  + ft.format();
            long currentTimeLong = localToUTC(new Date().getTime());

            long courseStartTimeInMins = 0;
            long courseEndTimeInMins = 0;
            if (getNextScheduledClassesRes != null && getNextScheduledClassesRes.getStatus() != null && getNextScheduledClassesRes.getStatus().equalsIgnoreCase("success")) {
                if (getNextScheduledClassesRes.getData() != null) {
                    if (getNextScheduledClassesRes.getData().getMikoCoachClasses() != null && getNextScheduledClassesRes.getData().getMikoCoachClasses().size() > 0) {
                        long currentTimeMins = TimeUnit.MILLISECONDS.toMinutes(currentTimeLong);
                        arrayList = getNextScheduledClassesRes.getData().getMikoCoachClasses();
                        for (int i = 0; i < getNextScheduledClassesRes.getData().getMikoCoachClasses().size(); i++) {
//                            courseStartTimeInMins = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(i).getClass_start_time()));
                            long HOURS = TimeUnit.HOURS.toMinutes(Long.parseLong(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(i).getEpoch_start_date()));
                            Log.d(TAG, "showNextScreen: " + HOURS);

                            try {
                                Log.d(TAG, "showNextScreen: 6 c ");
                                courseStartTimeInMins = TimeUnit.MILLISECONDS.toMinutes(new SimpleDateFormat(Constants.getInstance(this).TimeFormat).parse(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(i).getClass_start_time()).getTime());
                                courseEndTimeInMins = TimeUnit.MILLISECONDS.toMinutes(new SimpleDateFormat(Constants.getInstance(this).TimeFormat).parse(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(i).getClass_end_time()).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            long difference = courseStartTimeInMins - currentTimeMins;
                            long Enddifference = courseStartTimeInMins  - courseEndTimeInMins;
                            Log.d(TAG, "showNextScreen Time:difference " + difference);
                            Log.d(TAG, "showNextScreen Time:Enddifference " + Enddifference);
//                            Log.d(TAG, "showNextScreen Time:courseStartTimeInMins " + courseStartTimeInMins);
//                            Log.d(TAG, "showNextScreen Time:courseEndTimeInMins " + courseEndTimeInMins);
                            if (difference <= 15 && difference >= Enddifference) {
                                Log.d(TAG, "showNextScreen Time: show single view ");
                                return;
                            } else {
                                Log.d(TAG, "showNextScreen Time: show list");
                                setAdapter(getNextScheduledClassesRes);
                                return;
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "showNextScreen: show fallback screen ");
            }
        } catch (Exception e) {
            Log.e(TAG, "showNextScreen: Exception" + e.getMessage());
        }
    }

    public void setAdapter(GetNextScheduledClassesRes getNextScheduledClassesRes) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        coachAdapter = new CoachAdapter(this, getNextScheduledClassesRes.getData().getMikoCoachClasses(), this);
        coach_list.setLayoutManager(layoutManager);
        coach_list.setAdapter(coachAdapter);
    }

    public String StringResponse() {
        String response = "{\n" +
                "  \"status\": \"success\",\n" +
                "  \"data\": {\n" +
                "    \"mikoCoachClasses\": [\n" +
                "      {\n" +
                "        \"date\": \"Dec 4 Fri\",\n" +
                "        \"start_time\": \"06:30 PM\",\n" +
                "        \"end_time\": \"07:30 PM\",\n" +
                "        \"parental_date\": \"Today\",\n" +
                "        \"parental_start_time\": \"06:30 PM IST\",\n" +
                "        \"epoch_start_date\": \"1607063400000\",\n" +
                "        \"epoch_end_date\": \"1607067000000\",\n" +
                "        \"id\": \"19\",\n" +
                "        \"class_name\": \"Coding with C#\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Nita Ramdas Shetty\",\n" +
                "        \"class_start_time\": \"2020-12-04 13:00:00\",\n" +
                "        \"class_end_time\": \"2020-12-04 14:00:00\",\n" +
                "        \"class_live_stream_URL\": \"https://pvqybn5qyz24-hls-live.5centscdn.com/Miko/955ad3298db330b5ee880c2c9e6f23a0.sdp/playlist.m3u8\",\n" +
                "        \"class_zoom_ID\": \"949 8218 2677\",\n" +
                "        \"class_live_from\": \"India\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Dec 4 Fri\",\n" +
                "        \"start_time\": \"06:30 PM\",\n" +
                "        \"end_time\": \"07:30 PM\",\n" +
                "        \"parental_date\": \"Today\",\n" +
                "        \"parental_start_time\": \"06:30 PM IST\",\n" +
                "        \"epoch_start_date\": \"1607063400000\",\n" +
                "        \"epoch_end_date\": \"1607067000000\",\n" +
                "        \"id\": \"19\",\n" +
                "        \"class_name\": \"Coding with C#\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Nita Ramdas Shetty\",\n" +
                "        \"class_start_time\": \"2020-12-04 13:00:00\",\n" +
                "        \"class_end_time\": \"2020-12-04 14:00:00\",\n" +
                "        \"class_live_stream_URL\": \"https://pvqybn5qyz24-hls-live.5centscdn.com/Miko/955ad3298db330b5ee880c2c9e6f23a0.sdp/playlist.m3u8\",\n" +
                "        \"class_zoom_ID\": \"949 8218 2677\",\n" +
                "        \"class_live_from\": \"India\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Dec 4 Fri\",\n" +
                "        \"start_time\": \"06:30 PM\",\n" +
                "        \"end_time\": \"07:30 PM\",\n" +
                "        \"parental_date\": \"Today\",\n" +
                "        \"parental_start_time\": \"06:30 PM IST\",\n" +
                "        \"epoch_start_date\": \"1607063400000\",\n" +
                "        \"epoch_end_date\": \"1607067000000\",\n" +
                "        \"id\": \"19\",\n" +
                "        \"class_name\": \"Coding with C#\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Nita Ramdas Shetty\",\n" +
                "        \"class_start_time\": \"2020-12-04 13:00:00\",\n" +
                "        \"class_end_time\": \"2020-12-04 14:00:00\",\n" +
                "        \"class_live_stream_URL\": \"https://pvqybn5qyz24-hls-live.5centscdn.com/Miko/955ad3298db330b5ee880c2c9e6f23a0.sdp/playlist.m3u8\",\n" +
                "        \"class_zoom_ID\": \"949 8218 2677\",\n" +
                "        \"class_live_from\": \"India\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        return response;
    }


    @Override
    public void onItemClick(ArrayList<MikoCoachClassesEntity> item) {

    }

}
