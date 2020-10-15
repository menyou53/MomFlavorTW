package com.example.momflavortw;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.kommunicate.Kommunicate;

import static android.content.ContentValues.TAG;

public class MyFirebaseService extends FirebaseMessagingService {
    public MyFirebaseService() {
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG,"Message data payload: " + remoteMessage.getData());
        }
        if(remoteMessage.getNotification() != null){
            Log.d(TAG,"Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        if (Kommunicate.isKmNotification(this, remoteMessage.getData())) {
            return;
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("token",token);
        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(data);
    }
}
