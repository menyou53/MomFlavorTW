package com.example.momflavortw.ui.notifications.notice;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.data.NoticeCount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

public class Notice2Fragment extends Fragment {

    int notRead = 0;
    private String date,title,content;
    TextView textTitle,textContent,textDate;
    BadgeDrawable badge_dashboard;
    Handler handler = new Handler();
    Runnable setBadge;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBottomView = ((AppCompatActivity)this.getContext()).findViewById(R.id.nav_view);
        badge_dashboard = navBottomView.getOrCreateBadge(R.id.navigation_notifications);
        if(notRead == 0){
            badge_dashboard.setVisible(false);
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notice2, container, false);
        Bundle args = getArguments();


        textTitle = root.findViewById(R.id.notice2_textTitle);
        textDate = root.findViewById(R.id.notice2_textDate);
        textContent = root.findViewById(R.id.notice2_textContent);

        if(getArguments()!=null) {
            date = args.getString("date");
            title = args.getString("title");
            content = args.getString("content");
        }
        textTitle.setText(title);
        textDate.setText(date.substring(0,4)+"年"+date.substring(5,7)+"月"+date.substring(8,10)+"日");
        textContent.setText(content);



        setBadge = new Runnable() {
            @Override
            public void run() {
                setBadgeNotice();
            }
        };


        new Thread(new Runnable(){
            @Override
            public void run(){
                loadNotice2();

            }
        }).start();



        return root;
    }

    public void loadNotice2(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("notice");
        final Query query = ref.whereEqualTo("date",date);
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult() ){
                                Log.d("TAG", document.getId() +"=>"+document.getData() );
                                Notice notice = document.toObject(Notice.class);
                                if(notice.getRead() == 0){
                                   db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("notice").document(date)
                                           .update("read",1);
                                   db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("noticeCount").document("NoticeCount")
                                           .update("notRead", FieldValue.increment(-1));
                               }
                            }
                            handler.postDelayed(setBadge, 500);
                        } else {
                            Log.w("TAG", "Error getting documents.",task.getException());
                        }

                    }
                });

    }

    public void setBadgeNotice(){
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
                                    badge_dashboard.setBackgroundColor(Color.RED);
                                    badge_dashboard.setMaxCharacterCount(3);
                                    badge_dashboard.setNumber(notRead);
                                    badge_dashboard.setVisible(true);
                                }else{
                                    badge_dashboard.setVisible(false);
                                }
                            }else{
                                Log.d(TAG, "notcie not exist");

                            }
                        }else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
    }
}
