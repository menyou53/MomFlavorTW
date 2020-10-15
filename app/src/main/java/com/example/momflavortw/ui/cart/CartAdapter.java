package com.example.momflavortw.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momflavortw.MainActivity;
import com.example.momflavortw.R;
import com.example.momflavortw.ui.image.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Cart> mCarts;
    private int count;
    public CartAdapter(Context context,List<Cart> carts){
        mContext = context;
        mCarts = carts;
    }



    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_item,parent,false);
        return new ImageViewHolder(v);

    }



    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        final Cart cartCurrent = mCarts.get(position);
        cartCurrent.setmPriceCount(cartCurrent.getPrice()*cartCurrent.getNum());
        holder.textViewName.setText(cartCurrent.getName());
        holder.textViewNum.setText(String.valueOf(cartCurrent.getNum()));
        holder.textCartPrice.setText(String.valueOf(cartCurrent.getPriceCount()));
        final int[] stock = new int[mCarts.size()];
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("products").document(cartCurrent.getProduct());
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
        if(count==1){
            holder.minusImg.setImageResource(R.drawable.ic_baseline_delete_24);
        }
        Picasso.get()
                .load(cartCurrent.getImageUrl())
                .fit()
                .centerCrop(20)
                .into(holder.imageView);
        holder.minusImg.setOnClickListener(new  View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count= Integer.parseInt((String) holder.textViewNum.getText());
                    //Integer.parseInt(String.valueOf(cartCurrent.getNum())) ;
                    if(count>2) {
                        count--;
                        cartCurrent.setmPriceCount(cartCurrent.getPrice()*count);
                        holder.textViewNum.setText(String.valueOf(count));
                        holder.textCartPrice.setText(String.valueOf(cartCurrent.getPriceCount()));
                    }else if(count == 2){
                        holder.minusImg.setImageResource(R.drawable.ic_baseline_delete_24);
                        count--;
                        cartCurrent.setmPriceCount(cartCurrent.getPrice()*count);
                        holder.textViewNum.setText(String.valueOf(count));
                        holder.textCartPrice.setText(String.valueOf(cartCurrent.getPriceCount()));
                    }
                    else if(count == 1){
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                                .update("sum", FieldValue.increment(-cartCurrent.getNum()));
                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart").document(cartCurrent.getName())
                                .delete();


                        mCarts.remove(position);
                        notifyDataSetChanged();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mCarts.size());
                        holder.minusImg.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);

                        if(mContext instanceof MainActivity){
                            ((MainActivity)mContext).setBadge();
                        }
                    }
                }
        });
        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt((String) holder.textViewNum.getText());
                //Integer.parseInt(String.valueOf(cartCurrent.getNum())) ;
                if(count < stock[position] && count > 1){
                    count++;
                    cartCurrent.setmPriceCount(cartCurrent.getPrice()*count);
                    holder.textViewNum.setText(String.valueOf(count));
                    holder.textCartPrice.setText(String.valueOf(cartCurrent.getPriceCount()));
                }else if(count == 1 && count < stock[position]){
                    holder.minusImg.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                    count++;
                    cartCurrent.setmPriceCount(cartCurrent.getPrice()*count);
                    holder.textViewNum.setText(String.valueOf(count));
                    holder.textCartPrice.setText(String.valueOf(cartCurrent.getPriceCount()));
                }
                if(count >= stock[position]){
                    Toast.makeText(v.getContext(),"現在庫存為"+stock[position],Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return mCarts.size();
    }

    public CartAdapter(Context context){
        this.mContext = context;
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNum,textViewName,textCartPrice;
        public ImageView imageView;
        public CardView cardView;
        public ImageView addImg;
        public ImageView minusImg;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewNum = itemView.findViewById(R.id.text_view_product_count);
            textViewName = itemView.findViewById(R.id.text_view_product_name);
            textCartPrice = itemView.findViewById(R.id.text_cart_price);
            imageView = itemView.findViewById(R.id.image_view_cart);
            cardView = itemView.findViewById(R.id.cardview_cart);
            addImg = itemView.findViewById(R.id.imageViewAdd);
            minusImg = itemView.findViewById(R.id.imageViewMinus);
        }
    }

}
