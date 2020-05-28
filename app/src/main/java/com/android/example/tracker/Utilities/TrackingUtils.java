package com.android.example.tracker.Utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.android.example.tracker.Constants;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class TrackingUtils {

    private static String TAG = TrackingUtils.class.getSimpleName();

    /**
     * Send notification to current area
     *
     * @param context current Context
     * @param latitude Current user latitude
     * @param longitude Current user longitude
     * @param radius Value of radius of the area to send background notifications
     */
    public static void sendNotification(Context context,double latitude, double longitude, int radius) {
        try {
            String jsonResponse;
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic " + Constants.TRACKER_APP_ONESIGNAL_KEY);
            con.setRequestMethod("POST");
            String strJsonBody = "{"
                    + "\"app_id\": \"588a2481-5cda-4687-9e33-7545cba61801\","
                    + "\"data\": {\"emitter\": \"" + getHashedPhoneUniqueID(context) + "\"},"
                    + "\"contents\": {\"en\": \"Notification message content\"},"
                    + "\"filters\": [{\"field\": \"location\",\"radius\": \"" + radius + "\",\"lat\": \"" + latitude + "\",\"long\": \"" + longitude + "\"}]"
                    + "}";

            System.out.println("strJsonBody:\n" + strJsonBody);
            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);
            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);
            if (httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Return encrypted phone unique ID
     * @param context current Context
     */
    private static String getHashedPhoneUniqueID(Context context)  {

        /*
         * getDeviceId() returns the unique device ID
         */
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i(TAG,"Current Device ID: " + deviceId);

        return hashUniqueID(deviceId);
    }

    /**
     * Converts device IMEI to hashed ID
     * @param deviceId device IMEI
     * @return Hashed unique ID
     */
    private static String hashUniqueID(String deviceId) {
            String generatedDeviceID = null;
            try {
                // Create MessageDigest instance for SHA-256
                MessageDigest md = MessageDigest.getInstance(Constants.HASH_ALGORITHM);
                //Add password bytes to digest
                md.update(deviceId.getBytes());
                //Get the hash's bytes
                byte[] bytes = md.digest();
                //This bytes[] has bytes in decimal format;
                //Convert it to hexadecimal format
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < bytes.length ; i++) {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                //Get complete hashed password in hex format
                generatedDeviceID = sb.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            return generatedDeviceID;
    }
}
