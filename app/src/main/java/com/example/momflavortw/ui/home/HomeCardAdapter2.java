package com.example.momflavortw.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCardAdapter2 extends RecyclerView.Adapter<HomeCardAdapter2.ImageViewHolder> {


    private Context mContext;
    private List<Upload> mUploads;

    public HomeCardAdapter2(Context context,List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.home_card_item2,parent,false);
        return new HomeCardAdapter2.ImageViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final com.example.momflavortw.ui.home.Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;
        public CardView cardView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.home_card2_text);
            imageView = itemView.findViewById(R.id.home_card2_image);
            cardView = itemView.findViewById(R.id.home_cardview2);
        }
    }
}
