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
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.ui.cart.CartCount;
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
import com.google.firebase.firestore.SetOptions;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;


public class ProductFragment extends Fragment {

    SliderView sliderView;
    private String name;
    private SliderAdapterExample adapter;
    private List<Sliders> mSliders;
    private String slider1;
    private String slider2;
    private String slider3;
    private String imageUrl;
    private int price;
    private List<SliderItem> sliderItemList = new ArrayList<>();
    private int sum;
    private Spinner spinner;
    TextView notifictionNumber;

    BadgeDrawable badge_dashboard;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBottomView = ((AppCompatActivity)this.getContext()).findViewById(R.id.nav_view);
        badge_dashboard = navBottomView.getOrCreateBadge(R.id.navigation_cart);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_product, container, false);


        if(getArguments()!=null) {
            //ProductFragmentArgs args = ProductFragmentArgs.fromBundle(getArguments());
           // name = args.getName();
            name = getArguments().getString("name");
            Log.d("name = ",name );

        }


        final Handler handler = new Handler();

        final Runnable renewCart = new Runnable() {
            @Override
            public void run() {
                renewNotification();
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
           // sliderView.setAutoCycle(true);
          //  sliderView.startAutoCycle();
            final TextView textprice = root.findViewById(R.id.textPrice);


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
                              //  mSliders.add(sliders);
                               // slider1 = sliders.getSlider1();
                             //   slider2 = sliders.getSlider2();
                             //   slider3 = sliders.getSlider3();
                                imageUrl = sliders.getImageUrl();

                                price = sliders.getPrice();
                                textprice.setText(String.valueOf(price));
                            }

                        } else {
                            Log.w("TAG", "Error getting documents.",task.getException());
                        }
                    }
                });

        db.collection("sliderImage").document(name).collection("slider")
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
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


        final Button btn = root.findViewById(R.id.renewButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcart();
                handler.postDelayed(renewCart, 1000);


            }
        });

            return root;
    }



    public void addcart(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(name, FieldValue.increment(spinner.getSelectedItemPosition()+1));
        data.put("sum", FieldValue.increment(spinner.getSelectedItemPosition()+1));

        Map<String, Object> cartdata = new HashMap<>();
        cartdata.put("name",name);
        cartdata.put("num",FieldValue.increment(spinner.getSelectedItemPosition()+1));
        cartdata.put("imageUrl",imageUrl);
        cartdata.put("price",price);

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .update(data);

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart").document(name)
                .set(cartdata, SetOptions.merge());
    }

    public void renewNotification(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();
                            CartCount cartCount = document.toObject(CartCount.class);
                            sum = cartCount.getSum();
                            badge_dashboard.setBackgroundColor(Color.RED);
                            badge_dashboard.setBadgeTextColor(Color.WHITE);
                            badge_dashboard.setMaxCharacterCount(3);
                            badge_dashboard.setNumber(sum);
                            badge_dashboard.setVisible(true);
                            Log.d(TAG, "Cached document data: " + document.getData());
                            Log.d(TAG, "sum = "+sum);
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
    }

}

