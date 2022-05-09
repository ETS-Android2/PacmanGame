package com.omer.mypackman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.omer.mypackman.R;


public class Fragment_Map extends Fragment {

    private GoogleMap myMap;
    private TextView locationDetails ;
    private OnMapReadyCallback callback = googleMap -> { myMap = googleMap; };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        findViews(view);
        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        // Async
        supportMapFragment.getMapAsync(callback);
        return view;
    }
    private void findViews(View view) {
        locationDetails = view.findViewById(R.id.data);
    }


    public void zoomMap(double x, double y) {
        if (x==0.0 && y==0.0) {
            locationDetails.setVisibility(View.VISIBLE);
            locationDetails.setText("No information about location");
        }
        else {
            locationDetails.setVisibility(View.INVISIBLE);
            LatLng point = new LatLng(x, y);
            myMap.addMarker(new MarkerOptions().position(point).title(""));
            focusLocation(point);
        }
    }

    private void focusLocation(LatLng currentLocation)
    {
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,50));
        myMap.animateCamera(CameraUpdateFactory.zoomIn());
        myMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }
}
