package com.example.elyryde;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> locationArrayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("charging_stations");
    FirebaseUser user;
   

    LatLng sydney = new LatLng(-34, 151);
    //LatLng TamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationArrayList = new ArrayList<>();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tripsRef = rootRef.child("charging_stations");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Double> list_lat = new ArrayList<>();
                List<Double> list_long = new ArrayList<>();
                List<String> city = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    int i = 0;
                    Double latitude = ds.child("latitude").getValue(Double.class);
                    Double longitude = ds.child("longitude").getValue(Double.class);
                    String city1 = ds.child("City").getValue(String.class);
                    Log.d("TAG", latitude + " / " + longitude + " / " + city1);
                    list_lat.add(latitude);
                    list_long.add(longitude);
                    city.add(city1);
                    LatLng x = new LatLng(list_lat.get(i), list_long.get(i));
                    locationArrayList.add(x);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));

                    // below lin is use to zoom our camera on map.
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(2.0f));

                    // below line is use to move our camera to the specific location.
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));

                    i++;


                }
                Log.d("TAG", list_lat.get(0) + " / " + list_long.get(0) + " / ");




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        tripsRef.addListenerForSingleValueEvent(valueEventListener);





        // on below line we are adding our
        // locations in our array list.
        //locationArrayList.add(sydney);
        //locationArrayList.add(TamWorth);
        //locationArrayList.add(NewCastle);
        //locationArrayList.add(Brisbane);


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // inside on map ready method
        // we will be displaying all our markers.
        // for adding markers we are running for loop and
        // inside that we are drawing marker on our map.




        for (int i = 0; i < locationArrayList.size(); i++) {

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

            // below lin is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(2.0f));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }
    }
}