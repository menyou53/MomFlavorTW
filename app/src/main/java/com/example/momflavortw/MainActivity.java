package com.example.momflavortw;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.momflavortw.data.Common;
import com.example.momflavortw.data.NoticeCount;
import com.example.momflavortw.databinding.ActivityMainBinding;
import com.example.momflavortw.ui.cart.CartCount;
import com.example.momflavortw.ui.home.HomeFragment;
import com.example.momflavortw.ui.notifications.message.LastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    int notice = 0;
    int sum = 0;
    int notRead = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid = FirebaseAuth.getInstance().getUid();


    ActivityMainBinding binding;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_image, R.id.navigation_cart, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, Auth.class);
            startActivity(intent);
            finish();
        } else {
            setBadge();
            setBadgeNotice();
        }

        final Common common = new Common();


        final BottomNavigationView navBar = findViewById(R.id.nav_view);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.fragment_message || destination.getId() == R.id.fragment_messageimg) {
                    navBar.setVisibility(View.GONE);
                    common.setMsgAlet(false);
                } else {
                    navBar.setVisibility(View.VISIBLE);
                    common.setMsgAlet(true);
                }
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            final DocumentReference docRef = db.collection("message").document(uid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                            ? "Local" : "Server";

                    if (snapshot != null && snapshot.exists()) {
                        LastMessage lastMessage = snapshot.toObject(LastMessage.class);
                        if (common.isMsgAlet() && lastMessage.getRead() == 0 && lastMessage.getFrom().equals(uid) == false && lastMessage.getFrom().equals("null") == false) {
                            common.setNewMsg(true);
                            //Toast.makeText(getApplicationContext(), "new message", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("有新的訊息");
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }

                        Log.d(TAG, source + " data: " + snapshot.getData());


                    } else {
                        Log.d(TAG, source + " data: null");
                    }
                }
            });

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


    public void setBadgeMessage(){
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

}