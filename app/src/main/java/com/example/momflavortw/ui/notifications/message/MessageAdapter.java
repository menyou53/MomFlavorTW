package com.example.momflavortw.ui.notifications.message;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private List<Message> mMessage;
    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    public MessageAdapter(Context context,List<Message> messages){
        mContext = context;
        mMessage = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        return new MessageAdapter.MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        final Message message = mMessage.get(position);

        if(message.getType().equals("message")) {
            if (message.isAdministrator()==false) {
                holder.constraintImg.setVisibility(View.GONE);
                holder.cardMsg.setVisibility(View.VISIBLE);
                holder.linearLayout.setGravity(Gravity.RIGHT);
                holder.textMessage.setText(message.getMessage().replace("*NN", "\n"));
            } else {
                holder.constraintImg.setVisibility(View.GONE);
                holder.cardMsg.setVisibility(View.VISIBLE);
                holder.linearLayout.setGravity(Gravity.LEFT);
                holder.textMessage.setText(message.getMessage().replace("*NN", "\n"));
            }
        }else if(message.getType().equals("image")){
            if(message.isAdministrator()==false){
                holder.constraintImg.setVisibility(View.VISIBLE);
                holder.cardMsg.setVisibility(View.GONE);
                holder.linearLayout.setGravity(Gravity.RIGHT);
                Picasso.get()
                        .load(message.getMessage())
                        .resize(400, 550)
                        .onlyScaleDown()
                        .centerInside()
                        .into(holder.imageView);

            }else {
                holder.constraintImg.setVisibility(View.VISIBLE);
                holder.cardMsg.setVisibility(View.GONE);
                holder.linearLayout.setGravity(Gravity.LEFT);
                Picasso.get()
                        .load(message.getMessage())
                        .resize(400, 550)
                        .onlyScaleDown()
                        .centerInside()
                        .into(holder.imageView);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final NavController navController = Navigation.findNavController(v);

                    Bundle bundle = new Bundle();
                    bundle.putString("imageUrl",message.getMessage());
                    navController.navigate(R.id.action_fragment_message_to_fragment_messageimg,bundle);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        LinearLayout linearLayout;
        ImageView imageView;
        ConstraintLayout constraintImg;
        CardView cardMsg;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.TextMessage);
            linearLayout = itemView.findViewById(R.id.linearMessage);
            imageView = itemView.findViewById(R.id.imageViewMessage);
            constraintImg = itemView.findViewById(R.id.constraintImg);
            cardMsg = itemView.findViewById(R.id.cardMsg);
        }
    }
}
