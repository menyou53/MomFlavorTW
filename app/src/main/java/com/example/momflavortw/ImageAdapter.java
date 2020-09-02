package com.example.momflavortw;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("getName",  "name =>"+uploadCurrent.getName()  );
                /*
                Query query = ref.whereEqualTo("name",uploadCurrent.getName());
                query
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document:task.getResult() ){
                                        Log.d("TAG","success");
                                        Log.d("TAG", document.getId() +"=>"+document.getData() );
                                        Sliders sliders = document.toObject(Sliders.class);
                                        String slider1 = sliders.getSlider1();
                                        String slider2 = sliders.getSlider2();
                                        String slider3 = sliders.getSlider3();
                                        Log.d("TAG",  "slider1 =>"+ slider1 );
                                        Log.d("TAG",  "slider2 =>"+ slider2 );
                                        Log.d("TAG",  "slider3 =>"+ slider3 );
                                    }

                                } else {
                                    Log.w("TAG", "Error getting documents.",task.getException());
                                }

                            }
                        });

*/
                //AppCompatActivity activity = (AppCompatActivity) v.getContext();
                //Fragment newFragment = new ProductFragment();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, newFragment).addToBackStack(null).commit();
                final NavController navController = Navigation.findNavController(v);

                Bundle bundle = new Bundle();
                bundle.putString("name",uploadCurrent.getName());
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
        public TextView textViewName;
        public ImageView imageView;
        public CardView cardView;


        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

}

