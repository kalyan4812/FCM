package com.example.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.fcm.MainActivity.notificationManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("refreshtoken",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title="",message="";
        Log.i("messagestatus","message recieved");
        if(remoteMessage.getData()!=null && remoteMessage.getData().size()==0 && remoteMessage.getNotification()!=null){
            title=remoteMessage.getNotification().getTitle();
             message=remoteMessage.getNotification().getBody();
             Log.i("infos","nodata");
           // Toast.makeText(MainActivity.mainActivity,"nodata",Toast.LENGTH_SHORT).show();
        }
        else if(remoteMessage.getData()!=null) {
             title=remoteMessage.getData().get("title");

             message=remoteMessage.getData().get("message");
            Log.i("infos","withdata");
       //     Toast.makeText(MainActivity.mainActivity,"withdata",Toast.LENGTH_SHORT).show();
        }
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
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground);
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
