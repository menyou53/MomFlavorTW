package com.example.momflavortw.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.momflavortw.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class Cart2Fragment extends Fragment {

    private Cart2ViewModel cart2ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cart2ViewModel =
                ViewModelProviders.of(this).get(Cart2ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart2, container, false);
        final TextView textView = root.findViewById(R.id.text_cart2);
        cart2ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });



        final Button button03=root.findViewById(R.id.Button03);
        // button02.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_cart_to_fragment_cart2));
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new Cart3Fragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
// Commit the transaction
                transaction.commit();
            }
        });
        return root;

    }


}
