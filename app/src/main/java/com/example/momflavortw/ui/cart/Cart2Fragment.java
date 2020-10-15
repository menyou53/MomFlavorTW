package com.example.momflavortw.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.data.Contact;
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
    private TextView textTotal,textPayment;
    private Spinner spinner;
    private int total,payment,shipping;
    private String adress;


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

        final RadioButton radioButton = root.findViewById(R.id.mRadioButton0);
        final RadioButton radioButton1 = root.findViewById(R.id.mRadioButton1);

        textPayment = root.findViewById(R.id.cart2_textPayment);
        spinner = root.findViewById(R.id.cart2_spinner2);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition()==0) {
                    textPayment.setText("面交地址為"+adress+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊");
                }else if(spinner.getSelectedItemPosition()==1){
                    textPayment.setText("匯款後請至訂單歷史輸入匯款資訊及宅配地址");
                }
            }
        });
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition()==0){
                    textPayment.setText("面交地址為"+adress);
                }else if(spinner.getSelectedItemPosition()==1){
                    textPayment.setText("匯款後請至訂單歷史輸入宅配地址");

                }
            }
        });


        String[] items = new String[]{"面交","宅配"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    textTotal.setText(String.valueOf(total));
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton.setChecked(true);
                    textPayment.setText("面交地址為"+adress+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊");
                }else {
                    textTotal.setText(String.valueOf(total+shipping));
                    radioButton1.setVisibility(View.INVISIBLE);
                    radioButton.setChecked(true);
                    textPayment.setText("運費為"+shipping+"元"+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊及宅配地址");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Cart cart = document.toObject(Cart.class);
                            total = cart.getTotal();
                            textTotal.setText(String.valueOf(total));
                        }
                    }
                });

        db.collection("Contact").document("Adress")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
                            adress = contact.getAdress();
                            textPayment.setText("面交地址為"+adress+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊");

                        }
                    }
                }) ;
        db.collection("Contact").document("shipping")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
                            shipping = contact.getShipping();

                        }
                    }
                }) ;


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
                if(radioButton.isChecked()){
                    payment = 1;
                }else {
                    payment = 2;
                }
                Bundle bundle = new Bundle();

                if(spinner.getSelectedItemPosition()==0){
                    bundle.putInt("total",total);
                    bundle.putString("ship","面交");
                    bundle.putInt("shipping",0);
                }else if(spinner.getSelectedItemPosition()==1){
                    bundle.putInt("total",total+shipping);
                    bundle.putString("ship","宅配");
                    bundle.putInt("shipping",shipping);
                }
                bundle.putInt("payment",payment);
                navController.navigate(R.id.action_fragment_cart2_to_fragment_cart3,bundle);
            }
        });

        return root;

    }


}
