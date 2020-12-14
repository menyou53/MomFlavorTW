package com.example.momflavortw.ui.notifications.aboutus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.momflavortw.R;
import com.example.momflavortw.data.Contact;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AboutusFragment extends Fragment {

    private GoogleMap googleMap;
    private MapView mMapView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_aboutus, container, false);

        final TextView textAddress = root.findViewById(R.id.textViewTakeAddress);
        final TextView textRemittance = root.findViewById(R.id.TextViewRemittance);

        FirebaseFirestore db =  FirebaseFirestore.getInstance();
        db.collection("Contact").document("contact")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
                            textAddress.setText(contact.getAddress().replace("NN","\n"));
                            textRemittance.setText(contact.getRemittance().replace("NN","\n"));

                        }
                    }
                }) ;

        MapView mMapView = root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                UiSettings uiSettings = mMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setAllGesturesEnabled(true);
                uiSettings.setMapToolbarEnabled(true);



                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng latLng = new LatLng(22.642124, 120.352018);
                LatLng latLng1 = new LatLng(22.6292483,120.2673082);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("取貨地點"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng )      // Sets the center of the map to Mountain View
                        .zoom(18)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });



        return root;
    }

}


