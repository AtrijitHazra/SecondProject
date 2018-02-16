package firebase.demo.firebaseapplicationdemo.firebase;

import android.app.DownloadManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import firebase.demo.firebaseapplicationdemo.R;
import firebase.demo.firebaseapplicationdemo.UserDetails;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendMessage extends AppCompatActivity {
    String TAG = "SendMessage";
    EditText edit_text_message;
    ArrayList<UserDetails> tokens;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edit_text_message = findViewById(R.id.edit_text_message);
        tokens = getIntent().getParcelableArrayListExtra("tokens");
        jsonArray = new JSONArray();
        jsonArray.put(tokens.get(0).getUserToken());
        Log.d(TAG, "onCreate: "+jsonArray.toString());

    }

    public void sendMessage(View v) {
        final String message = edit_text_message.getText().toString().trim();
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();

                    JSONObject notification = new JSONObject();
                    notification.put("body", "Hi");
                    notification.put("title", "Test message");

                    JSONObject data = new JSONObject();
                    data.put("message", message);

                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", jsonArray);

                    String result = postToFCM(root.toString());
                    Log.d(TAG, "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(getApplicationContext(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        String SERVER_KEY = "AAAAlRumkKY:APA91bEJKp80EN9XbAuT-eZ2OfLUSnLfZY-A3EnJzMYVLImzH_DHu4Pn5hdyoXIO9eU4C8Bj0smOPRB7Es8smgNFbhOdUcE4HRGfAkTQ7t7ozbGYrijU4Jh7Cvjkv-sbemnQ09vaAbnn";
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        OkHttpClient mClient = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
