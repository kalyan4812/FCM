package com.example.fcm;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApllication extends Application {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("general").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(!task.isSuccessful()){
                    Log.i("token","not succesful");
                    return;
                }
                Log.i("token","succesful");
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
               if(!task.isSuccessful()){
                   Log.i("token","not succesful");
                   return;
               }
               Log.i("token",task.getResult().getToken());
                sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF),MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putString(getString(R.string.FCM_TOKEN),task.getResult().getToken()).apply();
            }
        });
    }
}
