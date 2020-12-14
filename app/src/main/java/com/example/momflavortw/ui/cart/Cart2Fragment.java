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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private RecyclerView mRecyclerview2;
    private Extragoods2Adapter mAdapter2;
    private TextView textTotal,textPayment;
    private Spinner spinner;
    private int total,payment,shipping;
    private String address;
    private int extra;
    private String[] extraName;
    private String[] extraImageUrl;
    private int[] extraPrice;
    private int[] extraNum;
    private String sDay1,sDay2,sDay3,pickday;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_cart2, container, false);
        final ConstraintLayout constraintLayoutCalender = root.findViewById(R.id.cartconstraintCalender);
        final TextView textViewCalendar = root.findViewById(R.id.textViewCalendar);

         textTotal = root.findViewById(R.id.text_total);

         ArrayList<Extragoods2> extragoodsArrayList= new ArrayList<>();

         if(getArguments()!=null) {
            extra = getArguments().getInt("extra");
            extraNum = getArguments().getIntArray("extraNum");
            extraPrice = getArguments().getIntArray("extraPrice");
            extraName = getArguments().getStringArray("extraName");
            extraImageUrl = getArguments().getStringArray("extraImageUrl");

            for(int i=1;i<=extra;i++){
                if(extraNum[i-1]>0){
                    extragoodsArrayList.add(new Extragoods2(extraImageUrl[i-1],extraName[i-1],extraPrice[i-1],extraNum[i-1]));
                    total = total+extraNum[i-1]*extraPrice[i-1];
                }
            }
            mRecyclerview2 = root.findViewById(R.id.recycler_view_cart2_extra);
            mRecyclerview2.setHasFixedSize(true);
            mRecyclerview2.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerview2.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mAdapter2 =new Extragoods2Adapter(extragoodsArrayList);
            mRecyclerview2.setAdapter(mAdapter2);


        }
         final RadioButton radioDay1 = root.findViewById(R.id.radioButtonDay1);
         final RadioButton radioDay2 = root.findViewById(R.id.radioButtonDay2);
         final RadioButton radioDay3 = root.findViewById(R.id.radioButtonDay3);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day1 = calendar.getTime();
        sDay1 = sdf.format(day1).substring(0,2)+"月"+sdf.format(day1).substring(3,5)+"日";
        radioDay1.setText(sDay1);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day2 = calendar.getTime();
        sDay2 = sdf.format(day2).substring(0,2)+"月"+sdf.format(day2).substring(3,5)+"日";
        radioDay2.setText(sDay2);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day3 = calendar.getTime();
        sDay3 = sdf.format(day3).substring(0,2)+"月"+sdf.format(day3).substring(3,5)+"日";
        radioDay3.setText(sDay3);

        if(radioDay1.isChecked()){
            pickday = sDay1;
        }else if(radioDay2.isChecked()){
            pickday = sDay2;
        }else if (radioDay3.isChecked()){
            pickday = sDay3;
        }





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
                    textPayment.setText(address+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊");
                }else if(spinner.getSelectedItemPosition()==1){
                    textPayment.setText("匯款後請至訂單歷史輸入匯款資訊及宅配地址");
                }
            }
        });
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition()==0){
                    textPayment.setText(address);
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
                    constraintLayoutCalender.setVisibility(View.VISIBLE);
                    textTotal.setText(String.valueOf(total));
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton.setChecked(true);
                    textPayment.setText(address+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊");
                }else {
                    constraintLayoutCalender.setVisibility(View.GONE);
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
                            total = total+cart.getTotal();
                            textTotal.setText(String.valueOf(total));
                        }
                    }
                });

        db.collection("Contact").document("contact")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
                            address = contact.getAddress().replace("NN","\n");
                            textPayment.setText("面交地址為"+address.replace("NN","\n")+"\n\n"+"匯款後請至訂單歷史輸入匯款資訊");

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
                    bundle.putString("pickday",pickday);
                }else if(spinner.getSelectedItemPosition()==1){
                    bundle.putInt("total",total+shipping);
                    bundle.putString("ship","宅配");
                    bundle.putInt("shipping",shipping);
                    bundle.putString("pickday","null");
                }
                bundle.putInt("payment",payment);
                bundle.putInt("extra",extra);
                bundle.putIntArray("extraNum",extraNum);
                bundle.putIntArray("extraPrice",extraPrice);
                bundle.putStringArray("extraName",extraName);
                bundle.putStringArray("extraImageUrl",extraImageUrl);
                navController.navigate(R.id.action_fragment_cart2_to_fragment_cart3,bundle);
            }
        });

        return root;

    }


}
