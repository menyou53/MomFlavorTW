package com.example.momflavortw.ui.notifications.history;

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

class History2Adapter extends RecyclerView.Adapter<History2Adapter.ImageViewHolder>implements View.OnClickListener {

    private Context mContext;
    private List<Purchased> mPurchased;

    public History2Adapter(Context context,List<Purchased> purchaseds){
        mContext = context;
        mPurchased = purchaseds;
    }


    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.history2_item,parent,false);
        return new History2Adapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull History2Adapter.ImageViewHolder holder, int position) {
        final Purchased purchased = mPurchased.get(position);
        holder.textName.setText(purchased.getName());
        holder.textNum.setText(String.valueOf(purchased.getNum()));
        holder.textPrice.setText(String.valueOf(purchased.getPrice()));
        Picasso.get()
                .load(purchased.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mPurchased.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public CardView cardView;
        public TextView textName,textNum,textPrice;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_history2);
            cardView = itemView.findViewById(R.id.cardview_history2);
            textName = itemView.findViewById(R.id.History2CardTextName);
            textNum = itemView.findViewById(R.id.History2CardTextNum);
            textPrice = itemView.findViewById(R.id.History2CardTextPrice);
        }
    }
}
