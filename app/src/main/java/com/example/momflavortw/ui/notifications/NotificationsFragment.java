package com.example.momflavortw.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.momflavortw.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsFragment extends Fragment {

    private RecyclerView mRycyclerview;
    private NotificationsAdapter mAdapter;
    private ArrayList<String> items;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        items = new ArrayList<>();
        items.add("1st item");
        items.add("Purchase History");
        items.add("3rd item");
        items.add("4th item");
        items.add("5th item");
        items.add("6th item");
        items.add("Sign Out");

        mRycyclerview = root.findViewById(R.id.recycler_view_notification);
        mRycyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NotificationsAdapter(getActivity(),items);
        mRycyclerview.setAdapter(mAdapter);

        return root;
    }
}
