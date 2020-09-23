package com.example.momflavortw.ui.notifications.history;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class History2Fragment extends Fragment {

    private RecyclerView mRycyclerview;
    private String date,total;
    private List<Purchased> mPurchased;
    private History2Adapter mAdapter;
    private TextView textView1,textView2;
    Handler handler = new Handler();
    Runnable setLayoutVisible;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history2, container, false);
        Bundle args = getArguments();

       if(getArguments()!=null) {
            date = args.getString("date");
            Log.d("date = ",date );
        }

        mRycyclerview = root.findViewById(R.id.recycler_view_history2_1);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPurchased = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        textView1 = root.findViewById(R.id.history2_text1);
        textView2 = root.findViewById(R.id.history2_text2);
        final LinearLayout linearLayout = root.findViewById(R.id.history2Linear);
        final ProgressBar progressBar = root.findViewById(R.id.historyProgressBar);


        setLayoutVisible = new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };


        new Thread(new Runnable(){
            @Override
            public void run(){
                loadLayout();
                progressBar.setProgress(10);

            }
        }).start();





        return root;
    }

    public void loadLayout(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("historyCollection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Purchased purchased =document.toObject( Purchased.class);

                                String year = date.substring(0,4);
                                String month = date.substring(5,7);
                                String day = date.substring(8,10);
                                String dateYMD =(year+"年"+month+"月"+day+"日").toString();
                                textView1.setText(dateYMD);
                                mPurchased.add(purchased);
                            }
                            mAdapter = new History2Adapter(getActivity(),mPurchased);
                            mRycyclerview.setAdapter(mAdapter);

                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Purchased purchased = document.toObject(Purchased.class);
                            total = String.valueOf(purchased.getTotal());
                            textView2.setText(total);
                            handler.postDelayed(setLayoutVisible, 500);
                        }
                    }
                });
    }

}
