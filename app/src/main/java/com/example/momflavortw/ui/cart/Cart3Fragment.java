package com.example.momflavortw.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momflavortw.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class Cart3Fragment extends Fragment {

    private Cart3ViewModel cart3ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cart3ViewModel =
                ViewModelProviders.of(this).get(Cart3ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart3, container, false);
        final TextView textView = root.findViewById(R.id.text_cart3);
        cart3ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;

    }


}
