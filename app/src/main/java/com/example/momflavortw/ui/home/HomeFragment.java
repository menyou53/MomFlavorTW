package com.example.momflavortw.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.momflavortw.Auth;
import com.example.momflavortw.ImagesFragment;
import com.example.momflavortw.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final Button button01=root.findViewById(R.id.Button01);
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragment_image);
            }
        });

        final Button signout=root.findViewById(R.id.Signout);
        signout.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    signOut();
                    toAuth();
                }
            }
        });

        return root;
    }
    private void openImageActivity(){
        Intent intent = new Intent(this.getActivity(), ImagesFragment.class);
        startActivity(intent);

    }
    private void toAuth(){
        Intent intent = new Intent(this.getActivity(), Auth.class);
        startActivity(intent);
    }

    public boolean onBackPressed() {
        return getChildFragmentManager().popBackStackImmediate();
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
}

