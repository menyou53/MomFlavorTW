package com.example.momflavortw;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImagesFragment extends Fragment   {
    private RecyclerView mRycyclerview;
    private ImageAdapter mAdapter;
    private List<Upload> mUploads;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_images, container, false);

        mRycyclerview = view.findViewById(R.id.recycler_view);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUploads = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //navController.navigate(R.id.action_fragment_image_to_fragment_product);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Upload upload = document.toObject(Upload.class);
                                mUploads.add(upload);
                            }
                            mAdapter = new ImageAdapter(getActivity(), mUploads);
                            mRycyclerview.setAdapter(mAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        return view;

    }
}

/*
public class ImagesActivity extends AppCompatActivity {
    private RecyclerView mRycyclerview;
    private ImageAdapter mAdapter;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRycyclerview = findViewById(R.id.recycler_view);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(this,2));




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult() ){
                                Log.d("TAG", document.getId() +"=>"+document.getData() );
                                Upload upload = document.toObject(Upload.class);
                                mUploads.add(upload);
                            }
                            mAdapter = new ImageAdapter(ImagesActivity.this,mUploads);
                            mRycyclerview.setAdapter(mAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.",task.getException());
                        }
                    }
                });
    }


}
*/