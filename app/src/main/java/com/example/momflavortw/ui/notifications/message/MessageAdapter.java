package com.example.momflavortw.ui.notifications.message;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.annotation.NonNull;
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

        if(message.getEmail().equals(email)) {
            holder.linearLayout.setGravity(Gravity.RIGHT);
            holder.textMessage.setText(message.getMessage().replace("*NN","\n"));
        }else {
            holder.linearLayout.setGravity(Gravity.LEFT);
            holder.textMessage.setText(message.getMessage().replace("*NN","\n"));
        }
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        LinearLayout linearLayout;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.TextMessage);
            linearLayout = itemView.findViewById(R.id.linearMessage);
        }
    }
}
