package com.android.example.tracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.example.tracker.Utilities.TrackingUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;

public class LocationWorker extends Worker {
    private static String TAG = LocationWorker.class.getSimpleName();
    private Context mContext;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private int NOTIFICATION_ID = 30;
    private String CHANNEL_ID = "com.android.example.tracker.NOTIFICATION_CHANNEL_ID";

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Create services client provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        startLocationUpdates();
        return Result.success();
    }

    /**
     * Get current position repeatedly
     */
    private void startLocationUpdates() {
        // Define location Request
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Get Location every Constants.REQUESTING_INTERVAL_TIME milliseconds
        mLocationRequest.setInterval(Constants.REQUESTING_INTERVAL_TIME);
        // Define location Callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(mContext, R.string.locationFindingError, Toast.LENGTH_SHORT).show();
                    return;
                }
                Location location = locationResult.getLastLocation();

                // If location is null stop program & display error toast message
                if (location == null) {
                    Toast.makeText(mContext, R.string.locationFindingError, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Display current position in a Toast message
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();
                // Display current position in log
                Log.i(TAG, "Current Latitude: " + latitude);
                Log.i(TAG, "Current Longitude: " + longitude);
                // Display current position in Toast message
                String latLongMsg = mContext.getString(R.string.longLatValues, latitude, longitude);
                Toast.makeText(mContext, latLongMsg, Toast.LENGTH_SHORT).show();
                // Send background notification telling UID
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        TrackingUtils.sendNotification(mContext,latitude,longitude,Constants.RADIUS_AREA_CIRCLE);
                    }
                };
                thread.start();
            }
        };
        // Request locations Update
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.getMainLooper());

    }

}
