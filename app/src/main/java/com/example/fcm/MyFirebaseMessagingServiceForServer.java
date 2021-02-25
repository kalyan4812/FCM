package com.example.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.fcm.MainActivity.notificationManager;

public class MyFirebaseMessagingServiceForServer extends FirebaseMessagingService {
SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF),MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN),s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();
        Intent resultIntent = new Intent(MainActivity.mainActivity , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.mainActivity,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int notificationIdentity=1;

        NotificationCompat.Builder n=new NotificationCompat.Builder(MainActivity.mainActivity);


        n.setContentTitle(title)
                .setContentText(message);

        //content text is inner most one.
        n.setSmallIcon(R.drawable.ic_launcher_foreground);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground);
        n.setLargeIcon(bitmap);


        n.setAutoCancel(true);
        n.setContentIntent(pendingIntent);
        n.setDefaults(NotificationCompat.DEFAULT_ALL);

        Uri ringtonepath= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        n.setSound(ringtonepath);


        //if app is installed in Oreo device version 8and 8.1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId="mychannel";
            NotificationChannel channel=new NotificationChannel(channelId, "GOOGLE Promotions", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            n.setChannelId(channelId);
        }

        notificationManager.notify(notificationIdentity, n.build());
    }
}
