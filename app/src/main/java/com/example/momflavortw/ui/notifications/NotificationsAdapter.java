package com.example.momflavortw.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momflavortw.Auth;
import com.example.momflavortw.R;
import com.example.momflavortw.data.Common;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;

     NotificationsAdapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }


    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.notifications_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationsAdapter.ViewHolder holder, final int position) {

        final Common common = new Common();


        String title = data.get(position);
        holder.textView.setText(title);

        if(position==0&&title.equals("有新通知")) {
            holder.textView.setTextColor(Color.rgb(194,62,2));
        }
        if(common.isNewMsg()&&position == 4){
            holder.textView.setText("有新客服訊息");
            holder.textView.setTextColor(Color.rgb(194,62,2));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);

                if(position == 0){
                    navController.navigate(R.id.action_navigation_notifications_to_fragment_notice);
                }else if(position == 1) {
                    navController.navigate(R.id.action_navigation_notifications_to_fragment_history);
                }else if(position ==2){
                    navController.navigate(R.id.action_navigation_notifications_to_fragment_aboutus);
                }
                else if(position == 3) {
                    navController.navigate(R.id.action_navigation_notifications_to_fragment_feedback);
                }else if(position ==4){
                    navController.navigate(R.id.action_navigation_notifications_to_fragment_message);
                } else if(position == 5){
                    Log.d("position",String.valueOf(position));
                    FirebaseAuth.getInstance().signOut();
                    v.getContext().startActivity(new Intent(v.getContext(), Auth.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        public ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.notification_text);
            cardView = itemView.findViewById(R.id.cardview_notification);
        }
    }
}
