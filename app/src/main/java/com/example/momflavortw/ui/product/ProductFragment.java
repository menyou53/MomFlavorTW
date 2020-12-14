package com.example.momflavortw.ui.product;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momflavortw.R;
import com.example.momflavortw.data.Common;
import com.example.momflavortw.ui.cart.CartCount;
import com.example.momflavortw.ui.image.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;


public class ProductFragment extends Fragment {

    SliderView sliderView;
    private String product;
    private SliderAdapterExample adapter;
    private List<Sliders> mSliders;
    private String imageUrl,name,videoUrl;
    private int price,stock;
    private List<SliderItem> sliderItemList = new ArrayList<>();
    private int sum;
    private Spinner spinner;
    private WebView webView;
    private ChoiceAdapter mAdapter;
    private int markup = 0;
    private int[] markPrice ;
    private int checkMarkCount = 0;
    private int[] checkedMarkPrice;
    private String[] checkedMarkName;
    private boolean[] checkedMark;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



    BadgeDrawable badge_dashboard;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBottomView = ((AppCompatActivity)this.getContext()).findViewById(R.id.nav_view);
        badge_dashboard = navBottomView.getOrCreateBadge(R.id.navigation_cart);
        renewNotification();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_product, container, false);

        Common common = new Common();
        common.setChoiceItem("");

        final LinearLayout linearLayout  = root.findViewById(R.id.linearLayoutMarkup);

        if(getArguments()!=null) {
            product = getArguments().getString("product");
            name = product;
            //price = getArguments().getInt("price");
            imageUrl = getArguments().getString("imageUrl");
            videoUrl = getArguments().getString("videoUrl");
            markup = getArguments().getInt("markup");
            if(markup >0 ){
                markPrice = new int[markup];
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("products").document(product).collection("markup")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + "=>" + document.getData());
                                        Sliders sliders = document.toObject(Sliders.class);
                                        final int markupNum = sliders.getMarkupNum()-1;
                                        final String markupName = sliders.getMarkupName();
                                        markPrice[markupNum] = sliders.getMarkupPrice();
                                        final CheckBox cb = new CheckBox(getActivity());
                                        cb.setText(sliders.getMarkupName() + " + "+ valueOf(sliders.getMarkupPrice())+"元");
                                        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if(cb.isChecked()){

                                                    price = price+markPrice[markupNum];
                                                    name = name+markupName;
                                                }else {
                                                    price = price - markPrice[markupNum];
                                                    name = name.replace(markupName,"");
                                                }
                                            }
                                        });
                                        linearLayout.addView(cb);
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
            Log.d("name = ", product);
            Log.d("stock = ", valueOf(stock));
        }

        renewNotification();

        final TextView textStock = root.findViewById(R.id.textProductStock);
        final RecyclerView recyclerChoice = root.findViewById(R.id.product_recyclerView);
        LinearLayoutManager m=new LinearLayoutManager(getActivity());
        m.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerChoice.setLayoutManager(m);



        final Handler handler = new Handler();

        final Runnable renewCart = new Runnable() {
            @Override
            public void run() {
                renewNotification();
            }
        };


        spinner = root.findViewById(R.id.spinner);
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
        //sliderView.setAutoCycle(true);
        //sliderView.startAutoCycle();
        final TextView textprice = root.findViewById(R.id.textPrice);
        final TextView textProductTitle = root.findViewById(R.id.textProductTitle);
        final TextView TextNarrative = root.findViewById(R.id.textNarrative);
        final Button btn = root.findViewById(R.id.renewButton);
        final ConstraintLayout constraintVideo = root.findViewById(R.id.constraintVideo);
        webView = root.findViewById(R.id.product_webView);
       // textprice.setText(valueOf(price)+"元");
        textProductTitle.setText(product);

        if(videoUrl.equals("")){
            constraintVideo.setVisibility(View.GONE);
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.loadUrl(videoUrl);
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products").document(product).collection("slider")
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
        db.collection("products").document(product).collection("choice")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Sliders> mSliders = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Sliders sliders = document.toObject(Sliders.class);
                                mSliders.add(sliders);
                            }
                            mAdapter = new ChoiceAdapter(getContext(),mSliders);
                            recyclerChoice.setAdapter(mAdapter);

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stock>0) {
                    addcart();
                    Context context = getContext();
                    Toast.makeText(context, "已加入購物車", Toast.LENGTH_LONG).show();
                    final NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_fragment_product_to_navigation_cart);
                    //handler.postDelayed(renewCart, 500);
                }else {
                    Map<String ,Object> reservation = new HashMap<>();
                    reservation.put("uid",uid);
                    reservation.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("reservation").document("reservation").collection(product).document(uid)
                            .set(reservation,SetOptions.merge());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("商品補貨時會向您通知");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        db.collection("products").document(product)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Upload upload = document.toObject(Upload.class);
                            price = upload.getPrice();
                            textprice.setText(valueOf(price)+"元");
                            stock = upload.getStock();
                            if(stock <= 0){
                                spinner.setVisibility(View.GONE);
                                textStock.setVisibility(View.VISIBLE);
                                btn.setText("補貨時通知");
                            }else {
                                btn.setText("加入購物車");
                                //spinner = root.findViewById(R.id.spinner);
                                ArrayList<String> items = new ArrayList<String>();
                                for (int i = 1; i <= stock; i++) {
                                    items.add("數量" + valueOf(i));
                                }
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                                spinner.setAdapter(spinnerAdapter);

                            }
                        }
                    }
                });


        db.collection("products").document(product).collection("narrative").document("narrative")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Sliders sliders = document.toObject(Sliders.class);
                            TextNarrative.setText(sliders.getNarrative().replace("NN","\n"));

                        }
                    }
                });



            return root;
    }



    public void addcart(){
        Common common = new Common();
        String choice = common.getChoiceItem();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        //data.put(dbName, FieldValue.increment(spinner.getSelectedItemPosition()+1));
        data.put("sum", FieldValue.increment(spinner.getSelectedItemPosition()+1));
        Map<String, Object> cartdata = new HashMap<>();
        cartdata.put("name", name+" "+choice);
        cartdata.put("num",FieldValue.increment(spinner.getSelectedItemPosition()+1));
        cartdata.put("imageUrl",imageUrl);
        cartdata.put("price",price);
        cartdata.put("choice",choice);
        cartdata.put("product",product);
        Log.d("choice",common.getChoiceItem());

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .set(data, SetOptions.merge());

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart").document(name+" "+choice)
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
                            if (document.exists()) {

                                CartCount cartCount = document.toObject(CartCount.class);
                                sum = cartCount.getSum();
                                badge_dashboard.setBackgroundColor(Color.RED);
                                badge_dashboard.setBadgeTextColor(Color.WHITE);
                                badge_dashboard.setMaxCharacterCount(3);
                                badge_dashboard.setNumber(sum);
                                if(sum == 0){
                                    badge_dashboard.setVisible(false);
                                }else {
                                    badge_dashboard.setVisible(true);
                                }
                                //badge_dashboard.setVisible(true);
                                Log.d(TAG, "Cached document data: " + document.getData());
                                Log.d(TAG, "sum = " + sum);
                            }
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
    }




}

