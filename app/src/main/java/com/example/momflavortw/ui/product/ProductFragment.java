package com.example.momflavortw.ui.product;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.momflavortw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;




public class ProductFragment extends Fragment {


    SliderView sliderView;
    private String name;
    private SliderAdapterExample adapter;
    private List<Sliders> mSliders;
    private String slider1;
    private String slider2;
    private String slider3;
    private List<SliderItem> sliderItemList = new ArrayList<>();
    private int addNum;
    private Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        if(getArguments()!=null) {
            ProductFragmentArgs args = ProductFragmentArgs.fromBundle(getArguments());
            name = args.getName();
        }

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                renewItems();
            }
        };



        spinner = root.findViewById(R.id.spinner);
        String[] items = new String[]{"數量1","數量2","數量3","數量4","數量5","數量6","數量7","數量8","數量9","數量10"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(spinnerAdapter);



        sliderView = root.findViewById(R.id.imageSlider);
            mSliders = new ArrayList<>();
            adapter = new SliderAdapterExample(this.getActivity());
            sliderView.setSliderAdapter(adapter);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("sliderImage");
        Query query = ref.whereEqualTo("name",name);
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult() ){
                                Log.d("TAG", document.getId() +"=>"+document.getData() );
                                Sliders sliders = document.toObject(Sliders.class);
                                mSliders.add(sliders);
                                slider1 = sliders.getSlider1();
                                slider2 = sliders.getSlider2();
                                slider3 = sliders.getSlider3();
                                // Log.d("TAG",  "slider1 =>"+ slider1 );
                                // Log.d("TAG",  "slider2 =>"+ slider2 );
                                // Log.d("TAG",  "slider3 =>"+ slider3 );
                                handler.postDelayed(r, 1000);

                            }

                        } else {
                            Log.w("TAG", "Error getting documents.",task.getException());
                        }

                    }
                });


        final Button btn = root.findViewById(R.id.renewButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcart();
            }
        });

            return root;
    }

    public void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i  == 0) {
                sliderItem.setImageUrl(slider1);
            } if(i == 1) {
                sliderItem.setImageUrl(slider2);
            }if(i == 2){
                sliderItem.setImageUrl(slider3);
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void addcart(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(name, FieldValue.increment(spinner.getSelectedItemPosition()+1)
        );
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(data);
    }
}

