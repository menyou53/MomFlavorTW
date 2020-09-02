package com.example.shoppingplatform.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.shoppingplatform.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TitleHolder extends RecyclerView.ViewHolder {
    public TextView title;

    public TitleHolder(@NonNull View view) {
        super(view);
        title = view.findViewById(R.id.bannerTitle);
    }
}
