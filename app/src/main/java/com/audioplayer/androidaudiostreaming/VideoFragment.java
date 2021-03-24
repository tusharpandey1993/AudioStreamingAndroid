package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

public class VideoFragment extends Fragment implements OnClickInterface {

    FragmentActivity mActivity;
    private View mView;
    private RecyclerView phase2Icons;
    private ArrayList<Integer> icons;
    private VideoConversationAdapter videoConversationAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.video, container, false);

        phase2Icons = mView.findViewById(R.id.phase2Icons);

        icons = new ArrayList<>();
        icons.add(R.drawable.batter_low);
        icons.add(R.drawable.obstacle_detected);
        icons.add(R.drawable.batter_charging);

        videoConversationAdapter = new VideoConversationAdapter(mActivity,this, icons);



        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        phase2Icons.setLayoutManager(layoutManager);


        phase2Icons.setAdapter(videoConversationAdapter);

        return mView;
    }
}
