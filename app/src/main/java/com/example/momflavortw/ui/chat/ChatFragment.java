package com.example.momflavortw.ui.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.momflavortw.BuildConfig;
import com.example.momflavortw.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ChatFragment extends Fragment {

    boolean exit = false;
    public static final String APP_ID = BuildConfig.APPLICATION_ID;
    private static final String INVALID_APP_ID = "INVALID_APPLICATIONID";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_chat, container, false);

        final Button button = root.findViewById(R.id.btnAddLine);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri ="https://lin.ee/di1jB4v";
                Intent i =new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
            }
        });



        return root;
    }
}
