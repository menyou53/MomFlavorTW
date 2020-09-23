package com.example.momflavortw.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.momflavortw.R;
import com.example.momflavortw.ui.product.SliderAdapterExample;
import com.example.momflavortw.ui.product.SliderItem;
import com.example.momflavortw.ui.product.Sliders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private SliderAdapterExample adapter;
    private RecyclerView mRecyclerview,mRecyclerview2;
    private HomeCardAdapter mAdapter;
    private HomeCardAdapter2 mAdapter2;
    private List<Upload> mUploads,mUploads2;
    SliderView sliderView;
    Handler handler = new Handler();
    Runnable setLayoutVisible;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        mRecyclerview = root.findViewById(R.id.recycler_view_home);
        mUploads = new ArrayList<>();
        LinearLayoutManager m=new LinearLayoutManager(getActivity());
        m.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(m);

        mRecyclerview2 = root.findViewById(R.id.recycler_view2_home);
        mUploads2 = new ArrayList<>();
        mRecyclerview2.setHasFixedSize(true);
        mRecyclerview2.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final ConstraintLayout constraintLayout = root.findViewById(R.id.constraintHome);
        final ProgressBar progressBar = root.findViewById(R.id.homeProgressBar);


        sliderView = root.findViewById(R.id.homeSlider);
        adapter = new SliderAdapterExample(this.getActivity());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


         setLayoutVisible = new Runnable() {
            @Override
            public void run() {
                constraintLayout.setVisibility(View.VISIBLE);
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

       final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("home").document("product").collection("new_product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG(new)", document.getId() + "=>" + document.getData());
                                Upload upload = document.toObject(Upload.class);
                                mUploads.add(upload);
                            }
                            mAdapter = new HomeCardAdapter(getContext(),mUploads);
                            mRecyclerview.setAdapter(mAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("home").document("product").collection("popular")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG(pop)", document.getId() + "=>" + document.getData());
                                Upload upload = document.toObject(Upload.class);
                                mUploads2.add(upload);
                            }
                            mAdapter2 = new HomeCardAdapter2(getContext(),mUploads2);
                            mRecyclerview2.setAdapter(mAdapter2);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });





        db.collection("home").document("homeSlider").collection("slider")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<SliderItem> sliderItemList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Sliders sliders = document.toObject(Sliders.class);
                                SliderItem sliderItem = new SliderItem();
                                sliderItem.setImageUrl(sliders.getSlider());
                                sliderItemList.add(sliderItem);
                            }
                            adapter.renewItems(sliderItemList);
                            handler.postDelayed(setLayoutVisible, 500);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }



    public boolean onBackPressed() {
        return getChildFragmentManager().popBackStackImmediate();
    }

}

