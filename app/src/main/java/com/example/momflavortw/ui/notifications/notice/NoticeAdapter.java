package com.example.momflavortw.ui.notifications.notice;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momflavortw.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Notice> mNotice;


    public NoticeAdapter(Context context,List<Notice> notices){
        mContext = context;
        mNotice = notices;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.notice_item,parent,false);
        return new NoticeAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        final Notice notice = mNotice.get(position);
        if(notice.getRead() == 0){
            holder.textTitle.setTextColor(Color.BLACK);
            holder.textContent.setTextColor(Color.BLACK);
        }
        holder.textTitle.setText(notice.getTitle());
        holder.textContent.setText(notice.getContent());
        holder.textDate.setText(notice.getDate().substring(0,4)+"年"+notice.getDate().substring(5,7)+"月"+notice.getDate().substring(8,10)+"日");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("date",notice.getDate());
                bundle.putString("title",notice.getTitle());
                bundle.putString("content",notice.getContent());
                Navigation.findNavController(v).navigate(R.id.action_fragment_notice_to_fragment_notice2,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotice.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textTitle,textDate,textContent;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview_notice);
            textTitle = itemView.findViewById(R.id.text_view_notice_title);
            textDate = itemView.findViewById(R.id.text_view_notice_date);
            textContent = itemView.findViewById(R.id.text_view_notice_content);

        }
    }
}
