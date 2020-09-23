package com.example.momflavortw.ui.notifications.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

class HistoyAdapter extends RecyclerView.Adapter<HistoyAdapter.ImageViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<Purchased> mPurchased;

    public HistoyAdapter(Context context,List<Purchased> purchaseds){
        mContext = context;
        mPurchased = purchaseds;
    }


    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.history_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoyAdapter.ImageViewHolder holder, int position) {
        final Purchased purchased = mPurchased.get(position);
        holder.historyName.setText(purchased.getName());

        String year = purchased.getDate().substring(0,4);
        String month = purchased.getDate().substring(5,7);
        String day = purchased.getDate().substring(8,10);
        holder.historyDate.setText(year+"年"+month+"月"+day+"日");

        Picasso.get()
                .load(purchased.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.historyImage);

        final String date = purchased.getDate();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"date="+date);

                Bundle bundle = new Bundle();
                bundle.putString("date",date);
                Navigation.findNavController(v).navigate(R.id.action_fragment_history_to_fragment_history2, bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPurchased.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView historyName,historyDate;
        public ImageView historyImage;
        public CardView cardView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            historyName = itemView.findViewById(R.id.text_view_history_name);
            historyImage = itemView.findViewById(R.id.image_view_history);
            cardView = itemView.findViewById(R.id.cardview_history);
            historyDate = itemView.findViewById(R.id.text_view_history_date);
        }
    }
}
