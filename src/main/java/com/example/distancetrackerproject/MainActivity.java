package com.example.distancetrackerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    TextView currentAddress, lati, longi, distanceTraveled;
    Location oldLoc, newLoc;
    private int lastUpdateTime;
    float distance;
    float totalDistance;
    String a = "";
    ListView listView;
    List<Address> addresses;

    public ArrayList<Location> addressesList = new ArrayList<>();
    public ArrayList<Integer> timesList = new ArrayList<>();

    public ArrayList<Track> tList = new ArrayList<>();

    StringBuilder sb;
    Address address;
    Geocoder geocoder;
    double latitude; // latitude
    double longitude; // longitude

    LocationListener locationListener;
    CustomAdapter adapter;

    public void setAdapter()
    {
        adapter = new CustomAdapter(this, R.layout.adapter_layout, tList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if (savedInstanceState != null) {
            //tList = savedInstanceState.getParcelableArrayList("TRACK_LIST");
          //  setAdapter();
        //}
        currentAddress = findViewById(R.id.currentAddress);
        lati = findViewById(R.id.Latitude);
        longi = findViewById(R.id.Longitude);
        distanceTraveled = findViewById(R.id.Distance);
        listView = findViewById(R.id.listView);
        geocoder = new Geocoder(this, Locale.US);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            //@SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Log.d("Lat", String.valueOf(location.getLatitude()));
                Log.d("Lon", String.valueOf(location.getLongitude()));
                lati.setText("Latitude: " + String.valueOf(location.getLatitude()));
                longi.setText("Longitude: " + String.valueOf(location.getLongitude()));

                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                    a = addresses.get(0).getAddressLine(0);
                    if(addresses != null)
                    {
                        Log.d("address", a);
                    }
                    currentAddress.setText("Current Address: " + addresses.get(0).getAddressLine(0));
                    addressesList.add(location);
                    //int currentTime = (int) System.currentTimeMillis();
                    //int elapsedTime = currentTime - lastUpdateTime;
                    //lastUpdateTime = currentTime;

                    //lastUpdateTime = (int) System.currentTimeMillis();
                    updateArrayList(new Track(a, (int)getElapsedTime())/*formatTime(elapsedTime))*/);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (oldLoc != null) {
                    if (newLoc == null) {
                        newLoc = location;
                        distance = oldLoc.distanceTo(newLoc);
                        totalDistance += distance; // Add the distance to the total distance
                        Log.d("distance", String.valueOf(distance));
                        distanceTraveled.setText("Distance Traveled: " + String.valueOf(totalDistance) + "m");
                        return;
                    }
                    oldLoc = newLoc;
                    newLoc = location;
                    distance = oldLoc.distanceTo(newLoc);
                    totalDistance += distance; // Add the distance to the total distance
                    Log.d("distance", String.valueOf(distance));
                    distanceTraveled.setText("Distance Traveled: " + String.valueOf(totalDistance) + "m");

                } else {
                    oldLoc = location;
                }

                a = String.valueOf(location);
                Log.d("a", a);
                setAdapter();
            }
        };

        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else startLocationUpdates();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }
    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 10F, locationListener);
        }

    }
    public void updateArrayList(Track object)
    {
        tList.add(object);
        setAdapter();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            //Toast.makeText(MainActivity.this, "You must allow to continue", Toast.LENGTH_SHORT).show();
        }

    }

    private int getElapsedTime() {
        if (timesList.isEmpty())
        {
            timesList.add((int)System.currentTimeMillis());
            return 0;
        }
        else
        {
            int currentTime = (int) System.currentTimeMillis();
            int lastTime = timesList.get(timesList.size() - 1);
            int elapsedTime = currentTime - lastTime;
            timesList.add(currentTime);
            return elapsedTime/1000;
        }
    }
}
