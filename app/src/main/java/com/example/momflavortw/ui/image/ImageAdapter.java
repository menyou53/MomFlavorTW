package com.example.momflavortw.ui.image;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<Upload> mUploads;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref = db.collection("product");

    public ImageAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }




    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.textPrice.setText(uploadCurrent.getPrice()+"元");

        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putString("product",uploadCurrent.getProduct());
                bundle.putInt("stock",uploadCurrent.getStock());
                bundle.putInt("price",uploadCurrent.getPrice());
                bundle.putString("imageUrl",uploadCurrent.getImageUrl());
                bundle.putString("videoUrl",uploadCurrent.getVideoUrl());
                bundle.putInt("markup",uploadCurrent.getMarkup());
                navController.navigate(R.id.action_fragment_image_to_fragment_product,bundle);

            }
        });
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    @Override
    public void onClick(View view) {

    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName, textPrice;
        public ImageView imageView;
        public CardView cardView;


        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textPrice = itemView.findViewById(R.id.text_view_price);
            imageView = itemView.findViewById(R.id.image_view_upload);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

}

