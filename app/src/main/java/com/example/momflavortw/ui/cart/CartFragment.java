package com.example.momflavortw.ui.cart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class CartFragment extends Fragment {

    private RecyclerView mRycyclerview;
    private CartAdapter mAdapter;
    private List<Cart> mCart;
    TextView textView;
    int total,sum;
    ConstraintLayout constraintLayout;
    BadgeDrawable badge_dashboard;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBottomView = ((AppCompatActivity)this.getContext()).findViewById(R.id.nav_view);
        badge_dashboard = navBottomView.getOrCreateBadge(R.id.navigation_cart);
        badge_dashboard.setVisible(false);
        if(sum != 0){
            badge_dashboard.setVisible(true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_cart, container, false);

        constraintLayout = root.findViewById(R.id.ConstraintLayoutCart);
        mRycyclerview = root.findViewById(R.id.recycler_view_cart);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCart = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        textView = root.findViewById(R.id.textCart);

        renewNotification();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                CartCount cartCount = document.toObject(CartCount.class);
                                sum = cartCount.getSum();
                                Log.d(TAG, "Cached document data: " + document.getData());
                                Log.d(TAG, "sum = " + sum);
                                if (sum > 0) {
                                    constraintLayout.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.GONE);
                                }else{
                                    textView.setVisibility(View.VISIBLE);
                                }
                            }else {
                                textView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });



        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Cart cart = document.toObject(Cart.class);
                                mCart.add(cart);
                            }
                            mAdapter = new CartAdapter(getActivity(),mCart);
                            mRycyclerview.setAdapter(mAdapter);

                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        final Button checkout=root.findViewById(R.id.Checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTotal();
                checkout();
                renewNotification();

                final NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_navigation_cart_to_fragment_cart2);

            }
        });

        return root;
    }
    public void setTotal(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        total = 0;
        for(int i = 0; i < mAdapter.getItemCount(); i++){
            TextView text = ((TextView) mRycyclerview.findViewHolderForAdapterPosition(i)
                    .itemView.findViewById(R.id.text_cart_price));
            total = total + Integer.parseInt(text.getText().toString());
        }
        Map<String, Object> checkdata = new HashMap<>();
        checkdata.put("total",total);
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .set(checkdata, SetOptions.merge());

    }

    public void checkout(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int sum=0;
        for(int i = 0; i < mAdapter.getItemCount(); i++){
            Map<String, Object> cartdata = new HashMap<>();
            Map<String, Object> checkdata = new HashMap<>();

            TextView textNum = ((TextView) mRycyclerview.findViewHolderForAdapterPosition(i)
                    .itemView.findViewById(R.id.text_view_product_count));
            int num = 0;
            num = Integer.parseInt(textNum.getText().toString());
            sum = sum+num;
            cartdata.put("num",num);
            TextView textName = ((TextView) mRycyclerview.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.text_view_product_name));
            checkdata.put(textName.getText().toString(),num);
            db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart").document(textName.getText().toString())
                    .set(cartdata, SetOptions.merge());
            db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .update(checkdata);
            db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                    .update(checkdata);
        }
        Map<String, Object> totaldata = new HashMap<>();
        totaldata.put("sum",sum);
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .update(totaldata);
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
                                Log.d(TAG, "Cached document data: " + document.getData());
                                Log.d(TAG, "sum = " + sum);
                                if (sum > 0) {
                                    badge_dashboard.setBackgroundColor(Color.RED);
                                    badge_dashboard.setBadgeTextColor(Color.WHITE);
                                    badge_dashboard.setMaxCharacterCount(3);
                                    badge_dashboard.setNumber(sum);
                                    badge_dashboard.setVisible(true);
                                }
                            }
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
    }

}
