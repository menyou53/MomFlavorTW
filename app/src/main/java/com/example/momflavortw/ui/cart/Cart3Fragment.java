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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;

public class Cart3Fragment extends Fragment {

    int total ;


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



        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("UserInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            UserInfo userInfo = document.toObject(UserInfo.class);
                            if(userInfo.getSaveInfo().matches("true")){
                                editName.setText(userInfo.getName());
                                editPhone.setText(userInfo.getPhone());
                                editMail.setText(userInfo.getEmail());
                            }

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());

                        }
                    }
                });

        final Map<String, Object> history = new HashMap<>();
        final Map<String, Object> purchased = new HashMap<>();


        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cart cart = document.toObject(Cart.class);

                                history.put(cart.getName(),cart.getNum());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("checked")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        Cart cart = document.toObject( Cart.class);
                        history.put("total",cart.getTotal());
                    }
                });




        Button btnSendInfo = root.findViewById(R.id.buttonSendInfo);

        btnSendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                final String rt = sdf.format(calendar.getTime());

                if(editName.getText().toString().matches("")) {
                    remindName.setVisibility(root.VISIBLE);
                }else{
                    remindName.setVisibility(root.INVISIBLE);
                }
                if(editPhone.getText().toString().matches("")){
                    remindPhone.setVisibility(root.VISIBLE);
                }else{
                    remindPhone.setVisibility(root.INVISIBLE);
                }
                if(editMail.getText().toString().matches("")){
                    remindEmail.setVisibility(View.VISIBLE);
                }else{
                    remindEmail.setVisibility(View.INVISIBLE);
                }
                if(editName.getText().toString().matches("") || editPhone.getText().toString().matches("")|| editMail.getText().toString().matches("")){
                    Toast.makeText(getActivity(),"資料不完整",Toast.LENGTH_LONG).show();
                }else {
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final Map<String, Object> InfoData = new HashMap<>();
                    InfoData.put("name", editName.getText().toString());
                    InfoData.put("phone", editPhone.getText().toString());
                    InfoData.put("email", editMail.getText().toString());

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
                                            purchased.put("name",cart.getName());
                                            purchased.put("date",rt);
                                            purchased.put("imageUrl",cart.getImageUrl());
                                            purchased.put("num",cart.getNum());
                                            purchased.put("price",cart.getPrice());

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
                                .update(InfoData);
                    }else{
                        InfoData.put("saveInfo", "false");
                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("UserInfo")
                                .update(InfoData);
                    }
                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(rt).collection("info").document("userInfo")
                            .set(InfoData);
                }


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Access");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final NavController navController = Navigation.findNavController(root);
                        navController.navigate(R.id.action_fragment_cart3_to_navigation_home);
                    }
                });


                final AlertDialog dialog = builder.create();
                dialog.show();


                final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                positiveButtonLL.gravity = Gravity.CENTER;
                positiveButton.setLayoutParams(positiveButtonLL);

            }
        });



        return root;

    }


}
