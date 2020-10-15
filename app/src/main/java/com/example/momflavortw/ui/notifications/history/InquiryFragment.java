package com.example.momflavortw.ui.notifications.history;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.momflavortw.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

public class InquiryFragment extends Fragment {
    private String date;
    Handler handler = new Handler();
    Runnable rBack;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_inquiry, container, false);

        final EditText editText = root.findViewById(R.id.inquiry_content);
        Button button = root.findViewById(R.id.inquiry_button);
        final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Bundle args = getArguments();

        if(getArguments()!=null) {
            date = args.getString("date");
            Log.d("date = ",date );
        }

        rBack = new Runnable() {
            @Override
            public void run() {
                NavController navController = Navigation.findNavController(root);
                navController.popBackStack(R.id.fragment_history2, false);
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().matches("")){

                }else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                    String rt = sdf.format(calendar.getTime());

                    Map<String, Object> InquiryData = new HashMap<>();

                    InquiryData.put("Askdate", rt);
                    InquiryData.put("Orderdate",date);
                    InquiryData.put("user", user);
                    InquiryData.put("content", editText.getText().toString());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Inquiry").document(user + " at " + rt)
                            .set(InquiryData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setAlertDialog();
                                    Log.d(TAG, "Inquiry successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing Inquiry", e);
                                }
                            });
                }

            }
        });

        return root;
    }
    public void setAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("感謝您的提問");
        AlertDialog dialog = builder.create();
        dialog.show();
        handler.postDelayed(rBack, 500);

    }

}
