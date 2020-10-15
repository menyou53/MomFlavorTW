package com.example.momflavortw.ui.notifications.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momflavortw.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ChangeInfoFragment extends Fragment {

    private String date,name,phone,email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_changeinfo, container, false);

        final EditText changeEditName = root.findViewById(R.id.changeEditName);
        final EditText changeEditPhone = root.findViewById(R.id.changeEditPhone);
        final EditText changeEditEmail = root.findViewById(R.id.changeEditEmail);
        final TextView remindName = root.findViewById(R.id.changeNameRemind);
        final TextView remindPhone = root.findViewById(R.id.changePhoneRemind);
        final TextView remindEmail = root.findViewById(R.id.changeEmailRemind);


        Bundle args = getArguments();
        if(getArguments()!=null) {
            date = args.getString("date");
            name = args.getString("name");
            phone = args.getString("phone");
            email = args.getString("email");
            changeEditName.setText(name);
            changeEditPhone.setText(phone);
            changeEditEmail.setText(email);
        }
        final Button button = root.findViewById(R.id.changeInfoButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeEditName.getText().toString().matches("")) {
                    remindName.setVisibility(root.VISIBLE);
                } else {
                    remindName.setVisibility(root.INVISIBLE);
                }
                if (changeEditPhone.getText().toString().matches("")) {
                    remindPhone.setVisibility(root.VISIBLE);
                } else {
                    remindPhone.setVisibility(root.INVISIBLE);
                }
                if (changeEditEmail.getText().toString().matches("")) {
                    remindEmail.setVisibility(View.VISIBLE);
                } else {
                    remindEmail.setVisibility(View.INVISIBLE);
                }
                if (changeEditName.getText().toString().matches("") || changeEditPhone.getText().toString().matches("") || changeEditEmail.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "資料不完整", Toast.LENGTH_LONG).show();
                } else {
                    Context context = getContext();
                    Toast.makeText(context,"修改成功",Toast.LENGTH_LONG).show();
                     Map<String, Object> InfoData = new HashMap<>();
                    InfoData.put("name", changeEditName.getText().toString());
                    InfoData.put("phone", changeEditPhone.getText().toString());
                    InfoData.put("email", changeEditEmail.getText().toString());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("history").document(date).collection("info").document("userInfo")
                    .update(InfoData);
                    Bundle bundle = new Bundle();
                    bundle.putString("date",date);
                    Navigation.findNavController(v).navigate(R.id.action_fragment_changeinfo_to_fragment_history2,bundle);

                }
            }
        });

        return root;
    }
}
