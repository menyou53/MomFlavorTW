package com.example.momflavortw.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momflavortw.R;
import com.example.momflavortw.ui.image.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class ExtragoodsAdapter extends RecyclerView.Adapter<ExtragoodsAdapter.ImageViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Extragoods> mExtragoods;

    private int count;

    public ExtragoodsAdapter(Context context,List<Extragoods> extragoods){
        mContext = context;
        mExtragoods = extragoods;
    }


    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ExtragoodsAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_extragoods_item,parent,false);
        return new ExtragoodsAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        final Extragoods extragoods = mExtragoods.get(position);
        extragoods.setPriceCount(extragoods.getPrice()*extragoods.getNum());
        holder.textViewName.setText(extragoods.getName());
        holder.textViewNum.setText(String.valueOf(0));
        holder.textCartPrice.setText(String.valueOf(extragoods.getPriceCount()));
        final int[] stock = new int[mExtragoods.size()];
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("ExtraGoods").document(extragoods.getProduct());
        reference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Upload upload = document.toObject(Upload.class);
                            stock[position] = upload.getStock();
                        }
                    }
                });
        count= Integer.parseInt((String) holder.textViewNum.getText());
        holder.imageUrlExtraGoods.setText(extragoods.getImageUrl());
        Picasso.get()
                .load(extragoods.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);
        holder.minusImg.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count= Integer.parseInt((String) holder.textViewNum.getText());
                if(count == 0){
                }else if(count>0) {
                    count--;
                    extragoods.setPriceCount(extragoods.getPrice()*count);
                    holder.textViewNum.setText(String.valueOf(count));
                    holder.textCartPrice.setText(String.valueOf(extragoods.getPriceCount()));
                }

            }
        });


        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt((String) holder.textViewNum.getText());
                if(count < stock[position] && count >= 1){
                    count++;
                    extragoods.setPriceCount(extragoods.getPrice()*count);
                    holder.textViewNum.setText(String.valueOf(count));
                    holder.textCartPrice.setText(String.valueOf(extragoods.getPriceCount()));
                }else if(count == 0 && count < stock[position]){
                    holder.minusImg.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                    count++;
                    extragoods.setPriceCount(extragoods.getPrice()*count);
                    holder.textViewNum.setText(String.valueOf(count));
                    holder.textCartPrice.setText(String.valueOf(extragoods.getPriceCount()));
                }
                if(count >= stock[position]){
                    Toast.makeText(v.getContext(),"現在庫存為"+stock[position],Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mExtragoods.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNum,textViewName,textCartPrice,imageUrlExtraGoods;
        public ImageView imageView;
        public CardView cardView;
        public ImageView addImg;
        public ImageView minusImg;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNum = itemView.findViewById(R.id.product_count_extragoods);
            textViewName = itemView.findViewById(R.id.product_name_extragoods);
            textCartPrice = itemView.findViewById(R.id.cart_price_extragoods);
            imageView = itemView.findViewById(R.id.image_view_extragoods);
            cardView = itemView.findViewById(R.id.cardview_extragoods);
            addImg = itemView.findViewById(R.id.imageViewAdd_extragoods);
            minusImg = itemView.findViewById(R.id.imageViewMinus_extragoods);
            imageUrlExtraGoods = itemView.findViewById(R.id.imageUrlExtraGoods);
        }
    }
}
