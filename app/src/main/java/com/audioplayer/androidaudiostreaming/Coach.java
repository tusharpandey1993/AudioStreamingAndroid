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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach);
        long currentTimeLong = new Date().getTime();
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
        Log.d(TAG, "onCreate: time " + time );
        /*init();

1606458600000
1606482000000
        GetNextScheduledClassesRes getNextScheduledClassesRes = new GetNextScheduledClassesRes();

        getNextScheduledClassesRes = new Gson().fromJson(StringResponse(), GetNextScheduledClassesRes.class);

        showNextScreen(getNextScheduledClassesRes);*/
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
            if (getNextScheduledClassesRes != null && getNextScheduledClassesRes.getStatus() != null && getNextScheduledClassesRes.getStatus().equalsIgnoreCase("success")) {
                if (getNextScheduledClassesRes.getData() != null) {
                    if (getNextScheduledClassesRes.getData().getMikoCoachClasses() != null && getNextScheduledClassesRes.getData().getMikoCoachClasses().size() > 0) {
                        long currentTimeMins = TimeUnit.MILLISECONDS.toMinutes(new java.util.Date().getTime());
                        arrayList = getNextScheduledClassesRes.getData().getMikoCoachClasses();
                        for (int i = 0; i < getNextScheduledClassesRes.getData().getMikoCoachClasses().size(); i++) {
                            long courseStartTimeInMins = TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(i).getEpoch_start_date()));
                            long HOURS = TimeUnit.HOURS.toMinutes(Long.parseLong(getNextScheduledClassesRes.getData().getMikoCoachClasses().get(i).getEpoch_start_date()));
                            Log.d(TAG, "showNextScreen: " + HOURS);
                            long difference = Math.abs(courseStartTimeInMins - currentTimeMins);
                            if (difference <= Constants.getInstance(this).MIKO_COACH_LIST_THREASHOLD_TIME) {
                                Log.d(TAG, "showNextScreen: show single view ");
                            } else {
                                Log.d(TAG, "showNextScreen: show list");
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
                "        \"date\": \"Nov 27 Fri\",\n" +
                "        \"start_time\": \"08:52 AM\",\n" +
                "        \"end_time\": \"10:51 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"08:52 AM EET\",\n" +
                "        \"epoch_start_date\": \"1606467136000\",\n" +
                "        \"epoch_end_date\": \"1606474276000\",\n" +
                "        \"id\": \"15\",\n" +
                "        \"class_name\": \"Bioinformatics\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Krish\",\n" +
                "        \"class_start_time\": \"2020-11-27 06:52:16\",\n" +
                "        \"class_end_time\": \"2020-11-27 08:51:16\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/mikoapp/playlist.m3u8\",\n" +
                "        \"class_zoom_ID\": \"985 7642 9304\",\n" +
                "        \"class_live_from\": \"Kathmandu\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 28 Sat\",\n" +
                "        \"start_time\": \"10:52 AM\",\n" +
                "        \"end_time\": \"11:52 AM\",\n" +
                "        \"parental_date\": \"28 Nov, 2020\",\n" +
                "        \"parental_start_time\": \"10:52 AM EET\",\n" +
                "        \"epoch_start_date\": \"1606560736000\",\n" +
                "        \"epoch_end_date\": \"1606564336000\",\n" +
                "        \"id\": \"16\",\n" +
                "        \"class_name\": \"Zoology\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Rohit Sharma\",\n" +
                "        \"class_start_time\": \"2020-11-28 08:52:16\",\n" +
                "        \"class_end_time\": \"2020-11-28 09:52:16\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/mikoapp/playlist.m3u8\",\n" +
                "        \"class_zoom_ID\": \"939 8919 9398\",\n" +
                "        \"class_live_from\": \"Novosibirsk\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 30 Mon\",\n" +
                "        \"start_time\": \"11:53 AM\",\n" +
                "        \"end_time\": \"01:53 PM\",\n" +
                "        \"parental_date\": \"30 Nov, 2020\",\n" +
                "        \"parental_start_time\": \"11:53 AM EET\",\n" +
                "        \"epoch_start_date\": \"1606737196000\",\n" +
                "        \"epoch_end_date\": \"1606701196000\",\n" +
                "        \"id\": \"17\",\n" +
                "        \"class_name\": \"Psychology\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Ajit Desai\",\n" +
                "        \"class_start_time\": \"2020-11-30 09:53:16\",\n" +
                "        \"class_end_time\": \"2020-11-30 11:53:16\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/mikoapp/playlist.m3u88\",\n" +
                "        \"class_zoom_ID\": \"946 6472 7140\",\n" +
                "        \"class_live_from\": \"Indonesia\"\n" +
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
