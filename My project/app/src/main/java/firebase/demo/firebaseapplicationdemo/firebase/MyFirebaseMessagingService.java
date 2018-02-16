package firebase.demo.firebaseapplicationdemo.firebase;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import firebase.demo.firebaseapplicationdemo.MainActivity;

/**
 * Created by Atrijit on 14-09-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    String TaskAlertMessage = "";
    String TaskName = "";
    String AssignedBy = "";
    String TaskMessageBody;

    String ChatMessage = "";
    String SendBy = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: ");

        if (NotificationUtils.isAppIsInBackground(this)) {

            TaskAlertMessage = remoteMessage.getData().toString();
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification Message Body: " + TaskAlertMessage.toString());

            try {
                JSONObject jsonObject = new JSONObject(TaskAlertMessage.toString());
                TaskName = jsonObject.getJSONObject("data").getString("title");
                AssignedBy = jsonObject.getJSONObject("data").getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TaskMessageBody = "Assigned by: " + AssignedBy;

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//            resultIntent.putExtra("message", message);
        } else {
            Log.e(TAG, "Notification Body: chat " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "From: chat " + remoteMessage.getFrom());
            ChatMessage = remoteMessage.getNotification().getBody();
            SendBy = remoteMessage.getFrom();
        }
    }

    @Override
    public void onMessageSent(String s) {
        Log.d(TAG, "onMessageSent: " + s);
        super.onMessageSent(s);

    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        Log.d(TAG, "onSendError: " + s + " exveption " + e.toString());
    }
}
