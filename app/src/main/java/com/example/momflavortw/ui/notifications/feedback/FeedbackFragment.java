package com.example.momflavortw.ui.notifications.feedback;

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

public class FeedbackFragment extends Fragment {

    Handler handler = new Handler();
    Runnable rToHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        final EditText editText = root.findViewById(R.id.feedback_content);
        Button button = root.findViewById(R.id.feedback_button);
        final String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        rToHome = new Runnable() {
            @Override
            public void run() {
                NavController navController = Navigation.findNavController(root);
                navController.popBackStack(R.id.navigation_home, false);

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

                    Map<String, Object> feedbackData = new HashMap<>();

                    feedbackData.put("date", rt);
                    feedbackData.put("user", user);
                    feedbackData.put("content", editText.getText().toString());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("feedback").document(user + " at " + rt)
                            .set(feedbackData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setAlertDialog();
                                    Log.d(TAG, "feedback successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing feedback", e);
                                }
                            });
                }

            }
        });



        return root;
    }

   public void setAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thx");
        AlertDialog dialog = builder.create();
        dialog.show();
       handler.postDelayed(rToHome, 500);

   }

}
