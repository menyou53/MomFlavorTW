package com.example.momflavortw.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class Extragoods2Adapter extends RecyclerView.Adapter<Extragoods2Adapter.Extragoods2ViewHolder> {
    private ArrayList<Extragoods2> mExtragoodsArrayList;

    public Extragoods2Adapter(ArrayList<Extragoods2> extragoodsArrayList){
        mExtragoodsArrayList = extragoodsArrayList;
    }

    @NonNull
    @Override
    public Extragoods2Adapter.Extragoods2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart2_extragoods_item,parent,false);
        return new Extragoods2ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Extragoods2Adapter.Extragoods2ViewHolder holder, int position) {
        final Extragoods2 extragoods = mExtragoodsArrayList.get(position);
        extragoods.setPriceCount(extragoods.getPrice() * extragoods.getNum());
        holder.textViewName.setText(extragoods.getName());
        holder.textViewNum.setText(String.valueOf(extragoods.getNum()));
        holder.textCartPrice.setText(String.valueOf(extragoods.getPriceCount()));
        Picasso.get()
                .load(extragoods.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mExtragoodsArrayList.size();
    }

    public class Extragoods2ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNum,textViewName,textCartPrice;
        public ImageView imageView;
        public CardView cardView;

        public Extragoods2ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNum = itemView.findViewById(R.id.text_view_product_count2_extragoods);
            textViewName = itemView.findViewById(R.id.text_view_product_name2_extragoods);
            textCartPrice = itemView.findViewById(R.id.text_cart2_price_extragoods);
            imageView = itemView.findViewById(R.id.image_view_cart2_extragoods);
            cardView = itemView.findViewById(R.id.cardview_cart2_extragoods);
        }
    }
}
