package com.example.shoppingplatform.viewholder;

import android.view.View;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.example.shoppingplatform.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoHolder extends RecyclerView.ViewHolder {
    public StandardGSYVideoPlayer player;

    public VideoHolder(@NonNull View view) {
        super(view);
        player = view.findViewById(R.id.player);
    }
}
