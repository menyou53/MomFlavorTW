package com.example.momflavortw.ui.cart;


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

class Cart2Adapter extends RecyclerView.Adapter<Cart2Adapter.ImageViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Cart> mCarts;
    public Cart2Adapter(Context context,List<Cart> carts){
        mContext = context;
        mCarts = carts;
    }



    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart2_item,parent,false);
        return new ImageViewHolder(v);

    }



    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        final Cart cartCurrent = mCarts.get(position);
        cartCurrent.setmPriceCount(cartCurrent.getPrice() * cartCurrent.getNum());
        holder.textViewName.setText(cartCurrent.getName());
        holder.textViewNum.setText(String.valueOf(cartCurrent.getNum()));
        holder.textCartPrice.setText(String.valueOf(cartCurrent.getPriceCount()));
        Picasso.get()
                .load(cartCurrent.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);

    }



    @Override
    public int getItemCount() {
        return mCarts.size();
    }

    public Cart2Adapter(Context context){
        this.mContext = context;
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNum,textViewName,textCartPrice;
        public ImageView imageView;
        public CardView cardView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewNum = itemView.findViewById(R.id.text_view_product_count2);
            textViewName = itemView.findViewById(R.id.text_view_product_name2);
            textCartPrice = itemView.findViewById(R.id.text_cart2_price);
            imageView = itemView.findViewById(R.id.image_view_cart2);
            cardView = itemView.findViewById(R.id.cardview_cart2);
        }
    }

}