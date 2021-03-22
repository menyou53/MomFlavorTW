package com.example.momflavortw.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.momflavortw.R;
import com.example.momflavortw.data.NoticeCount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class NotificationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotificationsAdapter mAdapter;
    private ArrayList<String> items;
    private int notRead = 0;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mRecyclerView = root.findViewById(R.id.recycler_view_notification);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("noticeCount").document("NoticeCount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                NoticeCount noticecount = document.toObject(NoticeCount.class);
                                notRead = noticecount.getNotRead();
                                Log.d(TAG, "notRead =  " + notRead);
                                if(notRead >0 ) {
                                    items = new ArrayList<>();
                                    items.add("有新通知");
                                    items.add("歷史訂單");
                                    items.add("面交及匯款資訊");
                                    items.add("Feedback");
                                    items.add("客服中心");
                                    items.add("登出");


                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    mAdapter = new NotificationsAdapter(getActivity(),items);
                                    mRecyclerView.setAdapter(mAdapter);

                                }else{
                                    items = new ArrayList<>();
                                    items.add("通知");
                                    items.add("歷史訂單");
                                    items.add("面交及匯款資訊");
                                    items.add("Feedback");
                                    items.add("客服中心");
                                    items.add("登出");


                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    mAdapter = new NotificationsAdapter(getActivity(),items);
                                    mRecyclerView.setAdapter(mAdapter);
                                }
                            }else{
                                items = new ArrayList<>();
                                items.add("通知");
                                items.add("歷史訂單");
                                items.add("面交及匯款資訊");
                                items.add("Feedback");
                                items.add("客服中心");
                                items.add("登出");


                                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mAdapter = new NotificationsAdapter(getActivity(),items);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }

                    }
                });


        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                String item = items.get(itemPosition);
                Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();

            }
        });



        return root;
    }
}
