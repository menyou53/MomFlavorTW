package com.example.momflavortw.ui.notifications.notice;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private List<Notice> mNotice;
    private NoticeAdapter mAdapter;
    private TextView textView ;
    int notice = 0;
    int notRead = 0;
    Handler handler = new Handler();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notice, container, false);

        mRecyclerview = root.findViewById(R.id.recycler_view_notice);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNotice = new ArrayList<>();
        mRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        textView = root.findViewById(R.id.textViewNoNotice);

        new Thread(new Runnable(){
            @Override
            public void run(){
                loadNotice();

            }
        }).start();


        return root;

    }

    public void loadNotice(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("noticeCount").document("NoticeCount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                NoticeCount noticecount = document.toObject(NoticeCount.class);
                                notice = noticecount.getNotice();
                                notRead = noticecount.getNotRead();
                                if(notice > 0){
                                    textView.setVisibility(View.GONE);
                                }else {
                                    textView.setVisibility(View.VISIBLE);
                                }
                            }else {
                                textView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        CollectionReference ref = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("notice");
        Query query = ref.orderBy("date", Query.Direction.DESCENDING);
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){

                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Notice notice = document.toObject(Notice.class);
                                mNotice.add(notice);
                            }
                            mAdapter = new NoticeAdapter(getActivity(),mNotice);
                            mRecyclerview.setAdapter(mAdapter);

                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}

