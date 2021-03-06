package com.example.momflavortw.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.ui.product.SliderAdapterExample;
import com.example.momflavortw.ui.product.SliderItem;
import com.example.momflavortw.ui.product.Sliders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private List<HomeProduct> mHomeProducts,mHomeProducts2;
    SliderView sliderView;
    Handler handler = new Handler();
    Runnable setLayoutVisible;
    private TextView home_title_1,home_title_2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        mRecyclerview = root.findViewById(R.id.recycler_view_home);
        mHomeProducts = new ArrayList<>();
        LinearLayoutManager m=new LinearLayoutManager(getActivity());
        m.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview.setLayoutManager(m);

        mRecyclerview2 = root.findViewById(R.id.recycler_view2_home);
        mHomeProducts2 = new ArrayList<>();
        mRecyclerview2.setHasFixedSize(true);
        mRecyclerview2.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        home_title_1 = root.findViewById(R.id.home_title_1);
        home_title_2 = root.findViewById(R.id.home_title_2);


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

       db.collection("home").document("title")
               .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if (task.isSuccessful()) {
                           DocumentSnapshot document = task.getResult();
                           if (document.exists()) {
                                HomeTitle homeTitle = document.toObject(HomeTitle.class);
                                home_title_1.setText(homeTitle.getHome_title_1());
                                home_title_2.setText(homeTitle.getHome_title_2());
                           }
                       }
                   }
               });



        db.collection("home").document("product").collection("home_product_1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeProduct homeProduct = document.toObject(HomeProduct.class);
                                mHomeProducts.add(homeProduct);

                            }
                            mAdapter = new HomeCardAdapter(getContext(),mHomeProducts);
                            mRecyclerview.setAdapter(mAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("home").document("product").collection("home_product_2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeProduct homeProduct = document.toObject(HomeProduct.class);
                                mHomeProducts2.add(homeProduct);
                            }
                            mAdapter2 = new HomeCardAdapter2(getContext(),mHomeProducts2);
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
                                //Log.d("TAG", document.getId() + "=>" + document.getData());
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

