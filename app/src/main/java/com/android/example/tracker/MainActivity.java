package com.android.example.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_PERMISSION_ID = 30;
    private FusedLocationProviderClient mFusedLocationClient;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check permission if has already granted
        if(!requestPermission())return;
        getCurrentPosition();
    }

    private void getCurrentPosition() {
        // Create services client provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Get last current position
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // If location is null stop program & display error toast message
                if (location == null) {
                    Toast.makeText(MainActivity.this, R.string.locationFindingError, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Display current position in a Toast message
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String longLatMsg = getString(R.string.longLatValues, latitude, longitude);
                Toast.makeText(MainActivity.this, longLatMsg, Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, R.string.locationFindingError, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error getting current position= " + e.getMessage());
                    }
                });
    }

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Show permission dialog only on android 23 an above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // If permission dialog has already rejected (NO BUTTON)
                    Toast.makeText(MainActivity.this, R.string.locationPermissionAlreadyRejected, Toast.LENGTH_SHORT).show();
                } else {
                    // If permission granted (User click Yes button in dialog permission request
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION_ID);
                }
            }
            return false;
        } else {
            // If the permission has already granted
            //getCurrentPosition();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentPosition();
                } else {
                    // permission denied by user
                    Toast.makeText(this,R.string.permission_denied_message_str,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
