package com.example.momflavortw.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.UserService;
import com.example.momflavortw.BuildConfig;
import com.example.momflavortw.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLoginHandler;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.users.KMUser;

public class ChatFragment extends Fragment {

    boolean exit = false;
    public static final String APP_ID = BuildConfig.APPLICATION_ID;
    private static final String INVALID_APP_ID = "INVALID_APPLICATIONID";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_chat, container, false);

        Kommunicate.init(getActivity(),APP_ID);


        final KMUser user = new KMUser();
        user.setUserId( FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Kommunicate.login(getActivity(), user, new KMLoginHandler() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {

                UserService.getInstance(getActivity().getApplicationContext()).updateLoggedInUser(user);
                new KmConversationBuilder(getActivity().getApplicationContext())
                        .setKmUser(user)
                        .launchConversation(new KmCallback() {
                            @Override
                            public void onSuccess(Object message) {
                                Kommunicate.openConversation(getActivity());
                                Log.d("Conversation", "Success : " + message);
                            }

                            @Override
                            public void onFailure(Object error) {
                                Log.d("Conversation", "Failure : " + error);
                            }
                        });

            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                // You can perform actions such as repeating the login call or throw an error message on failure
            }
        });


        return root;
    }
}
