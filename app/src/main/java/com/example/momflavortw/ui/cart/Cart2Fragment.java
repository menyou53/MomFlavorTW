package com.example.momflavortw.ui.cart;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Cart2Fragment extends Fragment {

    private RecyclerView mRycyclerview;
    private Cart2Adapter mAdapter;
    private List<Cart> mCart;
    private int total;
    TextView textTotal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_cart2, container, false);

         textTotal = root.findViewById(R.id.text_total);



        mRycyclerview = root.findViewById(R.id.recycler_view_cart2);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCart = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        Cart cart = document.toObject( Cart.class);
                        total = cart.getTotal();
                        textTotal.setText(String.valueOf(total));
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
                            mAdapter = new Cart2Adapter(getActivity(),mCart);
                            mRycyclerview.setAdapter(mAdapter);

                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        final Button toCart3 = root.findViewById(R.id.CheckoutInfo) ;
        toCart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_fragment_cart2_to_fragment_cart3);
            }
        });

        return root;

    }


}
