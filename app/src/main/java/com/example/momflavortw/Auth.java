package com.example.momflavortw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Auth extends AppCompatActivity {

    private static final String TAG = "LoginRegisterActivity";
    int AUTHUI_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }

    public void handleLoginRegister(View view) {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
               // .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                //.setLogo(R.mipmap.ic_launcher_round)
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
                //Checking for User (New/Old)
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    //This is a New User
                } else {
                    //This is a returning user
                }
                updateUI(FirebaseAuth.getInstance().getCurrentUser());


            } else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }
            }
        }
    }




    private void updateUI(FirebaseUser user) {
        if(user!=null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    String token_id = getTokenResult.getToken();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Map<String, Object> token = new HashMap<>();
                    token.put("uid",uid);
                    token.put("token_id",token_id);
                    token.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    token.put("name",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("message").document(uid)
                            .set(token, SetOptions.merge());
                }
            });



            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            Map<String, Object> data = new HashMap<>();
            data.put("Email", userEmail);
            db.collection("User").document(userEmail)
                    .set(data);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();

        }
    }

}