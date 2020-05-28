package com.android.example.tracker;

public class Constants {

    // Tracker App key
    public static final String TRACKER_APP_ONESIGNAL_KEY = "NGE1YWQxNzUtNDFiZC00NGQ2LWE4M2YtNGYxZTAxZjNkNjA1";

    // Radius of area that phones receives hidden notifications
    static final int  RADIUS_AREA_CIRCLE = 10; // Meters

    // Hashing algorithm used to hash device unique ID
    public static final String HASH_ALGORITHM = "SHA-256";

    // Interval time to update current position & send hidden notifications
    static int REQUESTING_INTERVAL_TIME = 8000; // In milliseconds

    // Min requesting time
    static int MIN_REQUESTING_INTERVAL_TIME = 2000;

    // Min requesting distance to update location & send notifications
    static int MIN_DISPLACEMENT_DISTANCE = 5;

}
