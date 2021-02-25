package com.example.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    // sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF),MODE_PRIVATE);
    public static NotificationManager notificationManager;
    SharedPreferences sharedPreferences;

    String api_links = "https://oakspro.com/projects/project36/kalyan/Notifications/send_notification.php";


    ///
    public static MainActivity mainActivity;
    final String api_link = "https://fcm.googleapis.com/fcm/send";
    final String contentType = "application/json";
    final String serverkey = "key=" + "AAAA1NEJ_kI:APA91bExwBN2jfEHTKQQ31_4gsTrsqEc-Nb2URYcNRc0iszTcE525Kctz4YSXxDZqAHWicBUHlia0-TfRKSnwyZ5-Ps6tDHU1RUmbO6fTAKdt14UXavCHaCBLZbaxOrnFRA9Oa0Q-cqG";
    EditText ed1, ed2;

    //////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        ed1 = findViewById(R.id.mytitle);
        ed2 = findViewById(R.id.message);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sharedPreferences = getSharedPreferences(getString(R.string.FCM_PREF), MODE_PRIVATE);

    }

    public void send(View view) {
        String topic = "/topics/general";
        String title = ed1.getText().toString();
        String message = ed2.getText().toString();
        JSONObject notification = new JSONObject();
        JSONObject notificationbody = new JSONObject();
        try {
            notificationbody.put("title", title).put("message", message);
            notification.put("to", topic).put("data", notificationbody);
        } catch (JSONException e) {

        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest myrequest = new JsonObjectRequest(api_link, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response", "onResponse: " + response.toString());
                ed1.setText("");
                ed2.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverkey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(myrequest);
    }

    public void sendfromserver(View view) {
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
        StringRequest request = new StringRequest(Request.Method.POST, api_links, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("response", "onResponse: " + response.toString());
                ed1.setText("");
                ed2.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fcm_token", token);
                params.put("title",ed1.getText().toString());
                params.put("message",ed2.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}
