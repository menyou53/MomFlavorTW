package com.example.momflavortw;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.momflavortw.data.NoticeCount;
import com.example.momflavortw.databinding.ActivityMainBinding;
import com.example.momflavortw.ui.cart.CartCount;
import com.example.momflavortw.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import io.kommunicate.Kommunicate;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    int notice = 0;
    int sum = 0;
    int notRead = 0;
    FirebaseFirestore db;
    CollectionReference ref;

    ActivityMainBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            setBadgeNotice();
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put("token",token);
                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                .update(data);
                        Kommunicate.updateDeviceToken(getApplicationContext(), token);


                        // Log and toast
                        Log.d(TAG, token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

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
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                CartCount cartCount = document.toObject(CartCount.class);
                                sum = cartCount.getSum();
                                Log.d(TAG, "Cached document data: " + document.getData());
                                Log.d(TAG, "sum = " + sum);
                                BadgeDrawable badge_dashboard = binding.navView.getOrCreateBadge(R.id.navigation_cart);
                                if(sum>0) {
                                    badge_dashboard.setBackgroundColor(Color.RED);
                                    badge_dashboard.setBadgeTextColor(Color.WHITE);
                                    badge_dashboard.setMaxCharacterCount(3);
                                    badge_dashboard.setNumber(sum);
                                    badge_dashboard.setVisible(true);
                                }else{
                                    badge_dashboard.setVisible(false);
                                }
                            }else{
                                Log.d(TAG, "sum not exist");
                            }
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
    }
    public void setBadgeNotice(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("noticeCount").document("NoticeCount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                NoticeCount noticecount = document.toObject(NoticeCount.class);
                                notice = noticecount.getNotice();
                                notRead = noticecount.getNotRead();
                                Log.d(TAG, "notice =  " + notice);
                                Log.d(TAG, "notRead =  " + notRead);
                                BadgeDrawable badge_dashboard = binding.navView.getOrCreateBadge(R.id.navigation_notifications);
                                if(notRead >0 ) {
                                    badge_dashboard.setBackgroundColor(Color.RED);
                                    badge_dashboard.setMaxCharacterCount(3);
                                    badge_dashboard.setNumber(notRead);
                                    badge_dashboard.setVisible(true);

                                }else{
                                    badge_dashboard.setVisible(false);
                                }
                            }else{
                                Log.d(TAG, "notcie not exist");

                            }
                        }else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }
                    }
                });
    }
    private void notification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentText("Code Sphere")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setAutoCancel(true)
                .setContentText("data is addded");
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());
    }


}