package com.android.example.tracker;

import android.content.ContentValues;
import android.util.Log;
import com.android.example.tracker.provider.ImprintContract;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;
import org.json.JSONException;

public class NotificationService extends NotificationExtenderService {

    private String TAG = NotificationService.class.getSimpleName();

    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult receivedResult) {

        String emitterPhoneID = null;
        try {
            emitterPhoneID = receivedResult.payload.additionalData.getString("emitter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* Logging notification payload */
        Log.i(TAG, "Notification received : Emitter Phone ID is: " + emitterPhoneID);
        // Save Emitter phone ID in database
        savePhoneEmitterHashedUniqueID(emitterPhoneID);
        // Do not show notifications, so set true
        return true;
    }

    private void savePhoneEmitterHashedUniqueID(String imprint) {
        // Values to Insert to database
        ContentValues values = new ContentValues();
        // Value of hashed unique ID
        values.put(ImprintContract.ImprintEntry.COLUMN_IMPRINT, imprint);
        long currentTime = System.currentTimeMillis();
        // Value of current timestamp in milliseconds
        values.put(ImprintContract.ImprintEntry.COLUMN_TIMESTAMP, currentTime);
        getContentResolver().insert(ImprintContract.ImprintEntry.CONTENT_URI,values);
    }
}
