package com.example.momflavortw.ui.notifications.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momflavortw.R;
import com.example.momflavortw.data.PaymentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PaymentFragment extends Fragment {

    private String date,cDate;
    private boolean click =false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_payment, container, false);

        final EditText editAccountNum = root.findViewById(R.id.editTextAccountNum);
        final EditText editPicker = root.findViewById(R.id.editTextPicker);
        final EditText editAdress = root.findViewById(R.id.editTextAdress);
        final EditText editRemarks = root.findViewById(R.id.editTextRemarks);
        final TextView accountNumNotice = root.findViewById(R.id.accountNumNotice);
        final TextView payDateNotice = root.findViewById(R.id.payDateNotice);
        final TextView pickerNotice = root.findViewById(R.id.textPickerNotice);
        final TextView adressNotice = root.findViewById(R.id.textAdressNotice);
        final TextView textCalender = root.findViewById(R.id.textCalender);
        final CalendarView calendarView = root.findViewById(R.id.calendarView);
        final ImageView imageView = root.findViewById(R.id.imageShowCalender);
        final ConstraintLayout constraintLayout = root.findViewById(R.id.constraintCalender);
        final ConstraintLayout constraintLayoutChoose = root.findViewById(R.id.constraintLayoutChooseDate);
        final CheckBox checkBox = root.findViewById(R.id.checkBoxSavePayment);

        Bundle args = getArguments();
        if(getArguments()!=null) {
            date = args.getString("date");

            Log.d("date = ",date );
        }

        editAdress.setHint("若面交自取 請輸入自取");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        final String rt = sdf.format(calendar.getTime());
        cDate = rt.substring(5,7)+"月"+rt.substring(8,10)+"日";
        textCalender.setText(cDate);



        constraintLayoutChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click==false) {
                    constraintLayout.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    click = true;
                }else{
                    constraintLayout.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    click = false;
                }
            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cDate = month+1+"月"+dayOfMonth+"日";
                textCalender.setText(cDate);
                Log.d("NEW_DATE", cDate);
                constraintLayout.setVisibility(View.GONE);
                click = false;
                imageView.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("DeliveryInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                PaymentInfo paymentInfo = document.toObject(PaymentInfo.class);
                                if(paymentInfo.getSave() == 1){
                                    editPicker.setText(paymentInfo.getPicker());
                                    editAdress.setText(paymentInfo.getAddress());
                                }
                            }
                        }
                    }
                    });

        Button button = root.findViewById(R.id.paymentButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAccountNum.getText().toString().length()!=5){
                    accountNumNotice.setVisibility(View.VISIBLE);
                }else {
                    accountNumNotice.setVisibility(View.INVISIBLE);
                }
                if(editPicker.getText().toString().equals("")){
                    pickerNotice.setVisibility(View.VISIBLE);
                }else {
                    pickerNotice.setVisibility(View.INVISIBLE);
                }
                if(editAdress.getText().toString().equals("")){
                    adressNotice.setVisibility(View.VISIBLE);
                }else {
                    adressNotice.setVisibility(View.INVISIBLE);
                }
                if(editAccountNum.getText().toString().length()!=5||editPicker.getText().toString().equals("")||editAdress.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "資料不完整", Toast.LENGTH_LONG).show();
                }else{
                    final Map<String, Object> InfoData = new HashMap<>();
                    final Map<String, Object> paymentData = new HashMap<>();
                    final Map<String, Object>deliveryData = new HashMap<>();

                    if(checkBox.isChecked()){
                        deliveryData.put("save",1);
                    }else {
                        deliveryData.put("save",0);
                    }
                    deliveryData.put("picker",editPicker.getText().toString());
                    deliveryData.put("address",editAdress.getText().toString());
                    InfoData.put("payed",1);
                    InfoData.put("status","已匯款");
                    InfoData.put("changeable",0);
                    paymentData.put("account",editAccountNum.getText().toString());
                    paymentData.put("payDate",cDate);
                    paymentData.put("picker",editPicker.getText().toString());
                    paymentData.put("address",editAdress.getText().toString());
                    paymentData.put("remarks",editRemarks.getText().toString());
                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("info").document("userInfo")
                        .update(InfoData);
                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("info").document("paymentInfo")
                            .set(paymentData, SetOptions.merge());
                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("UserInfo").document("DeliveryInfo")
                            .set(deliveryData, SetOptions.merge());
                    Map<String,Object> Orderdata = new HashMap<>();
                    Orderdata.put("payed",1);
                    Orderdata.put("status","已匯款");
                    db.collection("Order").document(date+"-"+FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            .update(Orderdata);
                    Bundle bundle = new Bundle();
                    bundle.putString("date",date);
                    Navigation.findNavController(v).navigate(R.id.action_fragment_payment_to_fragment_history2,bundle);

                }
            }
        });

        return root;
    }
}
