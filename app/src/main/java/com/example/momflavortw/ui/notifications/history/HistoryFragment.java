package com.example.momflavortw.ui.notifications.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.momflavortw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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

public class HistoryFragment extends Fragment {

    private RecyclerView mRycyclerview;
    private HistoyAdapter mAdapter;
    private List<Purchased> mPurchased;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history, container, false);

        mRycyclerview = root.findViewById(R.id.recycler_view_history);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPurchased = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("Purchased");
        Query query = ref.orderBy("date", Query.Direction.DESCENDING);
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                final Purchased purchased = document.toObject(Purchased.class);
                                mPurchased.add(purchased);

                            }
                            mAdapter = new HistoyAdapter(getActivity(),mPurchased);
                            mRycyclerview.setAdapter(mAdapter);
                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    return root;
    }

}

