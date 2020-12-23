package com.example.momflavortw.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.ui.image.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ImageViewHolder>  {

    private Context mContext;
    private List<HomeProduct> mHomeProducts;
    private List<com.example.momflavortw.ui.image.Upload> mUploads;

    public HomeCardAdapter(Context context,List<HomeProduct> homeProducts){
        mContext = context;
        mHomeProducts = homeProducts;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.home_card_item,parent,false);
        return new HomeCardAdapter.ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final HomeProduct homeProductCurrent = mHomeProducts.get(position);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //final com.example.momflavortw.ui.image.Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(homeProductCurrent.getName());
        Picasso.get()
                .load(homeProductCurrent.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                db.collection("products").document(homeProductCurrent.getProduct())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Upload upload = document.toObject(Upload.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("product",upload.getProduct());
                            bundle.putString("imageUrl",upload.getImageUrl());
                            bundle.putString("videoUrl",upload.getVideoUrl());
                            bundle.putInt("markup",upload.getMarkup());
                            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_fragment_product, bundle);
                        }
                    }
                });
            }
        });
        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("product",uploadCurrent.getProduct());
                bundle.putString("imageUrl",uploadCurrent.getImageUrl());
                bundle.putString("videoUrl",uploadCurrent.getVideoUrl());
                bundle.putInt("markup",uploadCurrent.getMarkup());
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_fragment_product, bundle);

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return mHomeProducts.size();
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public ImageView imageView;
        public CardView cardView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.home_card_text);
            imageView = itemView.findViewById(R.id.home_card_image);
            cardView = itemView.findViewById(R.id.home_cardview);
        }
    }

}
