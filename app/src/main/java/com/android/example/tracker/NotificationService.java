package com.android.example.tracker;

import android.util.Log;
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
        // Do not show notifications, so set true
        return true;
    }
}
