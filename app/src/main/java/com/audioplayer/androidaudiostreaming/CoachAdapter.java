package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.audioplayer.androidaudiostreaming.model.MikoCoachClassesEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.MyVi> {

    private static final String TAG = "CoachAdapter";
    List<MikoCoachClassesEntity> mValues;
    Context mContext;
    protected ItemListener mListener;
    private long CurrentTimeMins;
    private long CurrentTimeHour;

    private static int rvSelectedPosition = 0;

    public CoachAdapter(Context context, List<MikoCoachClassesEntity> values, ItemListener itemListener) {
        CurrentTimeMins = TimeUnit.MILLISECONDS.toMinutes(new java.util.Date().getTime());
        CurrentTimeHour = TimeUnit.MILLISECONDS.toHours(new java.util.Date().getTime());

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }


    public static void setRvSelectedItemPos(int pos) {
        rvSelectedPosition = pos;
    }

    @Override
    public MyVi onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.coach_item, parent, false);
        return new MyVi(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyVi holder, int position) {

        try {
            if (rvSelectedPosition == position) {
                Log.d(TAG, "onBindViewHolder: rv  == positon ");
            } else {
                Log.d(TAG, "onBindViewHolder: rv != positon ");
            }

            // This is to set Today, Tomorrow and date if hour > 48
            long hourBE = TimeUnit.MILLISECONDS.toHours((Long.parseLong(mValues.get(position).getEpoch_start_date())));
            int hourDifference = Math.toIntExact(Math.abs(CurrentTimeHour - hourBE));

            if (hourDifference <= 24) {
                holder.day.setText("Today");
            } else if (hourDifference <= 48) {
                holder.day.setText("Tomorrow");
            } else {
                holder.day.setText(mValues.get(position).getDate());
            }

            holder.time.setText(mValues.get(position).getStart_time());
            holder.classTitle.setText(mValues.get(position).getClass_name());
            holder.tutorName.setText(mValues.get(position).getClass_tutor());


            // This is to set Count Down time in Hours or mins
            long startTime = TimeUnit.MILLISECONDS.toMinutes((Long.parseLong(mValues.get(position).getEpoch_start_date())));

            long difference = Math.abs(CurrentTimeMins - startTime);

            // We will show mins only if hour < 24 and nothing if > 24
            if (hourDifference < 24) {
                if (difference < 60) {
                    holder.timeRemainingInHour.setText("" + difference + "mins");
                } else {
                    holder.timeRemainingInHour.setText("" + hourDifference + "hrs");
                }
            } else {
                holder.timeRemainingInHour.setText("");
            }

            if (mValues != null && mValues.size() == +1 + position) {
//                holder.bottomLine.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: exception " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(ArrayList<MikoCoachClassesEntity> item);
    }


    public class MyVi extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView day, time, classTitle, tutorName, timeRemainingInHour;
        ArrayList<MikoCoachClassesEntity> item;
        private View bottomLine;

        public MyVi(View v) {
            super(v);
            try {
                day = v.findViewById(R.id.day);
                time = v.findViewById(R.id.time);
                classTitle = v.findViewById(R.id.classTitle);
                tutorName = v.findViewById(R.id.tutorName);
                timeRemainingInHour = v.findViewById(R.id.timeRemainingInHour);
                bottomLine = v.findViewById(R.id.bottomLine);

                v.setOnClickListener(this);
            } catch (Exception e) {
                Log.e(TAG, "MyVi: exception" + e.getMessage());
            }

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
}