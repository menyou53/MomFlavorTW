package com.example.momflavortw.ui.product;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.ui.notifications.history.PaymentFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ImageViewHolder>{

    private Context mContext;
    private List<Sliders> mSliders;
    int row_index = 0;
    private PaymentFragment paymentFragment;

    public ChoiceAdapter(Context context,List<Sliders> sliders){
        mContext = context;
        mSliders = sliders;
    }




    @NonNull
    @Override
    public ChoiceAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_choice,parent,false);
        return new ChoiceAdapter.ImageViewHolder(v);    }


    @Override
    public void onBindViewHolder(@NonNull final ChoiceAdapter.ImageViewHolder holder, final int position) {
        final Sliders sliders = mSliders.get(position);
        holder.textChoice.setText(sliders.getChoice());
        Common common = new Common();
        if(common.getChoiceItem().equals("")){
            common.setChoiceItem(mSliders.get(0).getChoice());
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                row_index = position;
                Common.currentItem = sliders;
                notifyDataSetChanged();
                Common common = new Common();
                common.setChoiceItem(sliders.getChoice());
                Log.d("choice",common.getChoiceItem());


            }
        });
        if(row_index == position){
            holder.textChoice.setTextColor(Color.WHITE);
            holder.constraintLayout.setBackgroundColor(Color.rgb(194,62,2));
        }else{
            holder.textChoice.setTextColor(Color.rgb(194,62,2));
            holder.constraintLayout.setBackgroundResource(R.drawable.card_border);
        }

    }

    @Override
    public int getItemCount() {
        return mSliders.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textChoice;
        public CardView cardView;
        public ConstraintLayout constraintLayout;
        public TextView textprice;

        ItemClickListener itemclicklistener;

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemclicklistener = itemClickListener;

        }

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textChoice = itemView.findViewById(R.id.product_choice_text);
            cardView = itemView.findViewById(R.id.product_choice_card);
            constraintLayout = itemView.findViewById(R.id.product_choice_constrant);
            textprice = itemView.findViewById(R.id.textPrice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemclicklistener.onClick(view,getAdapterPosition());
        }
    }
}
