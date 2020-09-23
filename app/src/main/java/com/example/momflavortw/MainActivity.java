package com.example.momflavortw;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.momflavortw.databinding.ActivityMainBinding;
import com.example.momflavortw.ui.cart.CartCount;
import com.example.momflavortw.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_image, R.id.navigation_cart, R.id.navigation_notifications)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if( FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(this,Auth.class);
            startActivity(intent);
            finish();
        }else {

            setBadge();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (fragment instanceof HomeFragment) {
            if (((HomeFragment) fragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public void setBadge(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("total").document("total")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();
                            CartCount cartCount = document.toObject(CartCount.class);
                            int sum = cartCount.getSum();
                            Log.d(TAG, "Cached document data: " + document.getData());
                            Log.d(TAG, "sum = " + sum);

                            BadgeDrawable badge_dashboard = binding.navView.getOrCreateBadge(R.id.navigation_cart);
                            badge_dashboard.setBackgroundColor(Color.RED);
                            badge_dashboard.setBadgeTextColor(Color.WHITE);
                            badge_dashboard.setMaxCharacterCount(3);
                            badge_dashboard.setNumber(sum);
                            badge_dashboard.setVisible(true);
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });

     }


}