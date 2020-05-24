package com.android.example.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_PERMISSION_ID = 30;
    private boolean requestingLocationUpdates = true;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check permission if has already granted
        if(!requestPermission())return;
        startLocationWork();
    }

    private void startLocationWork() {
        // Start worker to get current location
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(LocationWorker.class)
                .build();
        // Enqueue work
        WorkManager.getInstance(this).enqueue(workRequest);
        //getCurrentPosition();
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
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Start current location finding on first grant dialog permission
                    startLocationWork();
                } else {
                    // permission denied by user
                    Toast.makeText(this,R.string.permission_denied_message_str,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
