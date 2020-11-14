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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Coach extends AppCompatActivity implements CoachAdapter.ItemListener {

    private static final String TAG = "Coach";
    private RecyclerView coach_list;
    private TextView day, time, classTitle, tutorName, timeRemainingInHour;
    private Button left,right;
    int rvSelectedItemPos = 0;
    private boolean rightPressed = false;
    private List<MikoCoachClassesEntity> arrayList = new ArrayList<>();
    private CoachAdapter coachAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach);

        init();


        GetNextScheduledClassesRes getNextScheduledClassesRes = new GetNextScheduledClassesRes();

        getNextScheduledClassesRes = new Gson().fromJson(StringResponse(), GetNextScheduledClassesRes.class);

        showNextScreen(getNextScheduledClassesRes);
    }

    private void init() {
        coach_list = findViewById(R.id.coach_list);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvSelectedItemPos  >= 0) {

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

                if (rvSelectedItemPos >= arrayList.size()) {
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
                "        \"date\": \"Nov 13 Fri\",\n" +
                "        \"start_time\": \"03:00 PM\",\n" +
                "        \"end_time\": \"04:30 PM\",\n" +
                "        \"parental_date\": \"Today\",\n" +
                "        \"parental_start_time\": \"03:00 PM EST\",\n" +
                "        \"epoch_start_date\": \"1605236400000\",\n" +
                "        \"epoch_end_date\": \"1605241800000\",\n" +
                "        \"class_name\": \"Singing\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"John Spivak\",\n" +
                "        \"class_start_time\": \"2020-11-13 20:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-13 21:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"India\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 13 Fri\",\n" +
                "        \"start_time\": \"04:00 PM\",\n" +
                "        \"end_time\": \"05:00 PM\",\n" +
                "        \"parental_date\": \"Today\",\n" +
                "        \"parental_start_time\": \"04:00 PM EST\",\n" +
                "        \"epoch_start_date\": \"1605240000000\",\n" +
                "        \"epoch_end_date\": \"1605243600000\",\n" +
                "        \"class_name\": \"Animation\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Akhilesh Varma\",\n" +
                "        \"class_start_time\": \"2020-11-13 21:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-13 22:00:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"India\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 1\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 2\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 3\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 4\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 5\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 6\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 14 Sat\",\n" +
                "        \"start_time\": \"12:00 AM\",\n" +
                "        \"end_time\": \"12:30 AM\",\n" +
                "        \"parental_date\": \"Tomorrow\",\n" +
                "        \"parental_start_time\": \"12:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1605355200000\",\n" +
                "        \"epoch_end_date\": \"1605357000000\",\n" +
                "        \"class_name\": \"Dance\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Abhinav Sinha 7\",\n" +
                "        \"class_start_time\": \"2020-11-14 05:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-14 05:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
                "        \"class_live_from\": \"USA\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"Nov 25 Wed\",\n" +
                "        \"start_time\": \"05:00 AM\",\n" +
                "        \"end_time\": \"06:30 AM\",\n" +
                "        \"parental_date\": \"25 Nov, 2020\",\n" +
                "        \"parental_start_time\": \"05:00 AM EST\",\n" +
                "        \"epoch_start_date\": \"1606280400000\",\n" +
                "        \"epoch_end_date\": \"1606285800000\",\n" +
                "        \"class_name\": \"Media\",\n" +
                "        \"class_thumbnail_image_URL\": \"https://dummyimage.com/150x100/000/fff.jpg\",\n" +
                "        \"class_tutor\": \"Rohan Sharma\",\n" +
                "        \"class_start_time\": \"2020-11-25 10:00:00\",\n" +
                "        \"class_end_time\": \"2020-11-25 11:30:00\",\n" +
                "        \"class_live_stream_URL\": \"http://714976022.r.fdccdn.net/714976022/_definst_/miko/playlist.m3u8\",\n" +
                "        \"class_zoom_URL\": \"https://zoom.us/j/95859961547?pwd=VDlpbDFvdAndfi893ndjBOWhZQXVsK0Nidz09\",\n" +
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
