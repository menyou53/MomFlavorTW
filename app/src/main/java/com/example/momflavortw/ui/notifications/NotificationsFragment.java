package com.example.momflavortw.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.momflavortw.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotificationsAdapter mAdapter;
    private ArrayList<String> items;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        items = new ArrayList<>();
        items.add("Notice");
        items.add("Purchase History");
        items.add("chat");
        items.add("4th item");
        items.add("Feedback");
        items.add("6th item");
        items.add("Sign Out");

        mRecyclerView = root.findViewById(R.id.recycler_view_notification);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NotificationsAdapter(getActivity(),items);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                String item = items.get(itemPosition);
                Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();



            }
        });

        return root;
    }
}
