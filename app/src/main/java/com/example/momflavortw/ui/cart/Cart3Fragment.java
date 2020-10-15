package com.example.momflavortw.ui.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momflavortw.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;

public class Cart3Fragment extends Fragment {

    private List<Cart> mCart;
    private int payment,total,shipping;
    private String paymentSt,ship;
    BadgeDrawable badge_dashboard;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BottomNavigationView navBottomView = ((AppCompatActivity)this.getContext()).findViewById(R.id.nav_view);
        badge_dashboard = navBottomView.getOrCreateBadge(R.id.navigation_cart);

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_cart3, container, false);

        final EditText editName = root.findViewById(R.id.editTextTextPersonName);
        final EditText editPhone = root.findViewById(R.id.editTextPhone);
        final EditText editMail = root.findViewById(R.id.editTextTextEmailAddress);
        final CheckBox saveInfoBox = root.findViewById(R.id.saveInfoBox);
        final TextView remindName = root.findViewById(R.id.textViewRemindName);
        final TextView remindPhone = root.findViewById(R.id.textViewRemindPhone);
        final TextView remindEmail = root.findViewById(R.id.textViewRemindEmail);
        final TextView paymentText =root.findViewById(R.id.cart3_paymentText);
        mCart = new ArrayList<>();

        if(getArguments() != null){
            shipping = getArguments().getInt("shipping");
            ship = getArguments().getString("ship");
            total = getArguments().getInt("total");
            payment = getArguments().getInt("payment");
            Log.d(TAG,"payment= "+payment);
        }
        if(payment == 1) {
            paymentSt = "匯款";
        }else if(payment == 2){
            paymentSt = "面交";
        }


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserInfo userInfo = document.toObject(UserInfo.class);
                                if (userInfo.getSaveInfo().matches("true")) {
                                    editName.setText(userInfo.getName());
                                    editPhone.setText(userInfo.getPhone());
                                    editMail.setText(userInfo.getEmail());
                                }
                            }   else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());

                        }
                    }
                });

        final Map<String, Object> history = new HashMap<>();
        final Map<String, Object> purchased = new HashMap<>();
        history.put("total",total);

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cart cart = document.toObject(Cart.class);

                                history.put(cart.getProduct(),cart.getNum());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
     /*   db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Cart cart = document.toObject(Cart.class);
                            history.put("total", cart.getTotal());
                        }
                    }
                });*/




        Button btnSendInfo = root.findViewById(R.id.buttonSendInfo);

        btnSendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                final String rt = sdf.format(calendar.getTime());

                if (editName.getText().toString().matches("")) {
                    remindName.setVisibility(root.VISIBLE);
                } else {
                    remindName.setVisibility(root.INVISIBLE);
                }
                if (editPhone.getText().toString().matches("")) {
                    remindPhone.setVisibility(root.VISIBLE);
                } else {
                    remindPhone.setVisibility(root.INVISIBLE);
                }
                if (editMail.getText().toString().matches("")) {
                    remindEmail.setVisibility(View.VISIBLE);
                } else {
                    remindEmail.setVisibility(View.INVISIBLE);
                }
                if (editName.getText().toString().matches("") || editPhone.getText().toString().matches("") || editMail.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "資料不完整", Toast.LENGTH_LONG).show();
                } else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("購買人: " + editName.getText().toString() + "\n\n電話: " + editPhone.getText().toString() + "\n\nemail: " + editMail.getText().toString()+"\n\n付款方式: "+paymentSt+"\n\n取貨方式: "+ship);
                    builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final Map<String, Object> InfoData = new HashMap<>();
                            InfoData.put("name", editName.getText().toString());
                            InfoData.put("phone", editPhone.getText().toString());
                            InfoData.put("email", editMail.getText().toString());
                            InfoData.put("payment",paymentSt);
                            InfoData.put("payed",0);
                            InfoData.put("ship",ship);
                            InfoData.put("shipping",shipping);
                            if(payment == 1){
                                InfoData.put("status","待匯款");
                            }else if(payment == 2){
                                InfoData.put("status","商品準備中");
                            }
                            InfoData.put("changeable",1);

                            db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(rt)
                                    .set(history);


                            db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    final Cart cart = document.toObject(Cart.class);
                                                    purchased.put("name", cart.getName());
                                                    purchased.put("date", rt);
                                                    purchased.put("imageUrl", cart.getImageUrl());
                                                    purchased.put("num", cart.getNum());
                                                    purchased.put("price", cart.getPrice());
                                                    purchased.put("product",cart.getProduct());

                                                    Map<String,Object> stock = new HashMap<>();
                                                    stock.put("stock", FieldValue.increment(-(cart.getNum())));
                                                    db.collection("products").document(cart.getProduct())
                                                            .update(stock);

                                                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("Purchased")
                                                            .add(purchased);
                                                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(rt).collection("historyCollection")
                                                            .add(purchased);


                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });


                            if (saveInfoBox.isChecked()) {
                                InfoData.put("saveInfo", "true");
                                db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("UserInfo")
                                        .set(InfoData, SetOptions.merge());

                            } else {
                                InfoData.put("saveInfo", "false");
                                db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("UserInfo")
                                        .set(InfoData, SetOptions.merge());
                            }
                            db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(rt).collection("info").document("userInfo")
                                    .set(InfoData);

                            Map<String,Object> Orderdata = new HashMap<>();
                            Orderdata.put("date",rt);
                            Orderdata.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            Orderdata.put("payment",paymentSt);
                            Orderdata.put("payed",0);
                            if(payment == 1){
                                Orderdata.put("status","待匯款");
                            }else if(payment == 2){
                                Orderdata.put("status","商品準備中");
                            }
                            db.collection("Order").document(rt+"-"+FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .set(Orderdata);



                            clearCart();





                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                            builder2.setMessage("access");
                            builder2.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    NavController navController = Navigation.findNavController(root);
                                    navController.popBackStack(R.id.navigation_home, false);
                                }
                            });

                            AlertDialog dialog2 = builder2.create();
                            dialog2.show();

                            Button positiveButton2 = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
                            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton2.getLayoutParams();
                            positiveButtonLL.gravity = Gravity.CENTER;
                            positiveButton2.setLayoutParams(positiveButtonLL);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);




                        }
                    });

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                    positiveButtonLL.gravity = Gravity.CENTER;
                    positiveButton.setLayoutParams(positiveButtonLL);
                }



            }

        });


        return root;

    }
    public void clearCart(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                                db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart").document(cart.getName())
                                        .delete();
                            }
                        }else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .delete();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .delete();
        badge_dashboard.setVisible(false);

    }



}
