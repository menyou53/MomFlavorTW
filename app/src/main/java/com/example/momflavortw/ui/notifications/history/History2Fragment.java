package com.example.momflavortw.ui.notifications.history;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.data.PaymentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class History2Fragment extends Fragment {

    private RecyclerView mRycyclerview;
    private String date,total,payment,status,name,email,phone;
    private int payed,changeable;
    private List<Purchased> mPurchased;
    private History2Adapter mAdapter;
    private TextView textViewDate,textViewTotal,textName,textPhone,textEmail,textPayment,textPaymentInfo,textChangeInfo;
    private TextView textPicker,textAddress,textAccount,textPayDate,textRemarks,changePaymentInfo,cancelText,shipText,shippingText;
    private Button btnAsk,btnCancel;
    private boolean cancelProposed = false ;
    Handler handler = new Handler();
    Runnable setLayoutVisible;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history2, container, false);
        Bundle args = getArguments();

       if(getArguments()!=null) {
            date = args.getString("date");
            Log.d("date = ",date );
        }

        mRycyclerview = root.findViewById(R.id.recycler_view_history2_1);
        mRycyclerview.setHasFixedSize(true);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPurchased = new ArrayList<>();
        mRycyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        textViewDate = root.findViewById(R.id.history2_textDate);
        textViewTotal = root.findViewById(R.id.history2_textTotal);
        textName = root.findViewById(R.id.history2_textName);
        textPhone = root.findViewById(R.id.history2_textPhone);
        textEmail = root.findViewById(R.id.history2_textEmail);
        textPayment = root.findViewById(R.id.history2_textPayment);
        textPaymentInfo = root.findViewById(R.id.textPaymentInfo);
        textChangeInfo = root.findViewById(R.id.history2_changeData);
        textPicker = root.findViewById(R.id.history2_textPicker);
        textAddress = root.findViewById(R.id.history2_textAddress);
        textAccount = root.findViewById(R.id.history2_textAccount);
        textPayDate = root.findViewById(R.id.history2_textPayDate);
        textRemarks = root.findViewById(R.id.history2_textRemarks);
        changePaymentInfo = root.findViewById(R.id.history2_changePaymentInfo);
        btnAsk = root.findViewById(R.id.history2_btnAsk);
        btnCancel = root.findViewById(R.id.history2_btnCancel);
        cancelText = root.findViewById(R.id.history2_cancelText);
        shipText = root.findViewById(R.id.history2_textShip);
        shippingText = root.findViewById(R.id.history2_textShipping);
        final LinearLayout linearCancel = root.findViewById(R.id.history2_linearCancel);
        final LinearLayout linearLayout = root.findViewById(R.id.history2Linear);
        final ProgressBar progressBar = root.findViewById(R.id.historyProgressBar);
        final ConstraintLayout constraintPaymentInfo = root.findViewById(R.id.constraintPaymentInfo);

        setLayoutVisible = new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if(changeable==1){
                    if(cancelProposed == true){
                        cancelText.setVisibility(View.VISIBLE);
                    }else {
                        linearCancel.setVisibility(View.VISIBLE);
                    }
                    textChangeInfo.setVisibility(View.VISIBLE);
                    textChangeInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("date",date);
                            bundle.putString("name",name);
                            bundle.putString("phone",phone);
                            bundle.putString("email",email);
                            Navigation.findNavController(v).navigate(R.id.action_fragment_history2_to_fragment_changeinfo,bundle);
                        }
                    });
                }else {
                    textChangeInfo.setVisibility(View.GONE);
                }
                if(payment.equals("匯款")) {
                    if(payed == 0) {
                        textPaymentInfo.setText("輸入匯款資訊");
                        textPaymentInfo.setTextColor(Color.rgb(2,150,194));
                        textPaymentInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("date",date);

                                Navigation.findNavController(v).navigate(R.id.action_fragment_history2_to_fragment_payment,bundle);
                            }
                        });
                    } else if(payed == 1) {
                        textPaymentInfo.setText(status);
                        constraintPaymentInfo.setVisibility(View.VISIBLE);
                        if(changeable == 1){
                            changePaymentInfo.setVisibility(View.VISIBLE);
                            changePaymentInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("date",date);
                                    Navigation.findNavController(v).navigate(R.id.action_fragment_history2_to_fragment_payment,bundle);
                                }
                            });
                        }
                    }
                }else if(payment.equals("面交")){
                    textPaymentInfo.setText(status);
                }
            }
        };


        new Thread(new Runnable(){
            @Override
            public void run(){
                loadLayout();
                progressBar.setProgress(10);

            }
        }).start();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("提出取消訂單");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Map<String, Object> Cancel = new HashMap<>();
                        Cancel.put("cancelProposed",true);
                        final Map<String,Object> Cancel3 = new HashMap<>();
                        Cancel3.put("status","已提出取消訂單");
                         FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date)
                                .set(Cancel, SetOptions.merge());
                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("info").document("userInfo")
                                .set(Cancel3,SetOptions.merge());
                        db.collection("Order").document(date+"-"+FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                .update(Cancel3);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        builder2.setMessage("取消訂單已提出");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragment_history2_to_fragment_message);
            }
        });




        return root;
    }

    public void loadLayout(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("historyCollection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + "=>" + document.getData());
                                Purchased purchased =document.toObject( Purchased.class);

                                String year = date.substring(0,4);
                                String month = date.substring(5,7);
                                String day = date.substring(8,10);
                                String dateYMD =(year+"年"+month+"月"+day+"日");
                                textViewDate.setText(dateYMD);
                                mPurchased.add(purchased);
                            }
                            mAdapter = new History2Adapter(getActivity(),mPurchased);
                            mRycyclerview.setAdapter(mAdapter);

                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Purchased purchased = document.toObject(Purchased.class);
                                total = String.valueOf(purchased.getTotal());
                                textViewTotal.setText(total);
                                cancelProposed = purchased.isCancelProposed();
                            }
                        }
                    }
                });

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("info").document("paymentInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                PaymentInfo paymentInfo = document.toObject(PaymentInfo.class);
                                textPicker.setText(paymentInfo.getPicker());
                                textAddress.setText(paymentInfo.getAddress());
                                textAccount.setText(paymentInfo.getAccount());
                                textPayDate.setText(paymentInfo.getPayDate());
                                textRemarks.setText(paymentInfo.getRemarks());
                            }
                        }
                    }
                });
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("info").document("userInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserInfo userInfo = document.toObject(UserInfo.class);
                                textName.setText(userInfo.getName());
                                name = userInfo.getName();
                                textPhone.setText(userInfo.getPhone());
                                phone = userInfo.getPhone();
                                textEmail.setText(userInfo.getEmail());
                                email = userInfo.getEmail();
                                textPayment.setText(userInfo.getPayment());
                                status = userInfo.getStatus();
                                payment = userInfo.getPayment();
                                payed = userInfo.getPayed();
                                changeable = userInfo.getChangeable();
                                shippingText.setText(String.valueOf(userInfo.getShipping())+"元");
                                if(userInfo.getShip().equals("面交")){
                                    shipText.setText(userInfo.getShip()+"   "+userInfo.getPickday());
                                }else {
                                    shipText.setText(userInfo.getShip());
                                }

                                Log.d(TAG,"payed= "+payed+"  payment="+payment);

                            }
                            handler.postDelayed(setLayoutVisible, 500);
                        }
                    }
                });

    }

}
