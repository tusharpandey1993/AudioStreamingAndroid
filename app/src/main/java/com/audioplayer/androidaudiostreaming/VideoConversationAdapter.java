package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class VideoConversationAdapter extends RecyclerView.Adapter<VideoConversationAdapter.MyViewHolder> {

    private Context mActivity;
    private int userType;
    private OnClickInterface onClickListner;
    ArrayList<Integer> icons;

    public VideoConversationAdapter(Context context, OnClickInterface mListner, ArrayList<Integer> iconsArray) {
        this.mActivity= context;
        this.userType= userType;
        this.onClickListner = mListner;
        this.icons = iconsArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teleconnect_top_items, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        try {
            holder.item_icon.setBackgroundResource(icons.get(position));
            holder.item_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListner.onClick(v, position);
                }
            });
        } catch (Exception e){
            Log.e("TAG", "onBindViewHolder: " + e.getMessage() );
        }

    }

    @Override
    public int getItemCount() {
        return icons.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView item_icon;;
        public MyViewHolder(View v) {
            super(v);
            try {
                item_icon = v.findViewById(R.id.icons);
            } catch (Exception e){
                Log.e("TAG", "MyViewHolder: " + e.getMessage() );
            }
        }
    }

}
