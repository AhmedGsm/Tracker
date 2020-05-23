package com.android.example.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create services client provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Get last current position
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // If location is null stop program & display error toast message
                if(location == null) {
                    Toast.makeText(MainActivity.this,R.string.locationFindingError,Toast.LENGTH_SHORT).show();
                    return;
                }
                // Display current position in a Toast message
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String logLatMsg = getString(R.string.longLatValues,latitude,longitude);
                Toast.makeText(MainActivity.this,logLatMsg,Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting current position= " + e.getMessage());
            }
        });
    }
}
