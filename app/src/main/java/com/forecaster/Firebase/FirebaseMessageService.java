package com.forecaster.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.forecaster.Activity.ChatDetailsActivity;
import com.forecaster.Activity.NotificationActivity;
import com.forecaster.Activity.RequestManagementActivity;
import com.forecaster.Modal.Notification;
import com.forecaster.R;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class FirebaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("server_name",remoteMessage.getFrom());
        Map<String,String> dataMap=remoteMessage.getData();


        Log.e("Data:", String.valueOf(remoteMessage.getData()));

        if(NotificationUtils.isAppIsInBackground(getApplicationContext()))
        {

             if(Objects.requireNonNull(remoteMessage.getData().get("type")).equalsIgnoreCase("chat"))
            {
                Intent intent = new Intent("FCM");
                intent.putExtra("FCM", "Yes");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Intent push = new Intent(this, ChatDetailsActivity.class);
                Log.e("1","1");
                sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);

            }else if(remoteMessage.getData().get("type").equalsIgnoreCase("booking"))
             {
                 Intent intent = new Intent("FCM");
                 intent.putExtra("FCM", "Yes");
                 LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                 Intent push = new Intent(this, RequestManagementActivity.class);
                 sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);

             }

             else {
                Intent intent = new Intent("FCM");
                intent.putExtra("FCM", "Yes");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Intent push = new Intent(this, NotificationActivity.class);
                sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);
                Log.e("2","2");
            }

        }
        else
        {
            if(remoteMessage.getData().get("title").equalsIgnoreCase("Oops! Chat Off"))
            {
                Intent push = new Intent(this, NotificationActivity.class);
                push.putExtra("FCM", "Yes");
                Log.e("4","4");
                sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);

            }
            else if(remoteMessage.getData().get("type").equalsIgnoreCase("chat"))
            {

                Intent push = new Intent(this, ChatDetailsActivity.class);
                push.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                push.putExtra("FCM","Yes");
                push.putExtra(GlobalVariables.roomId,remoteMessage.getData().get("roomId"));
                push.putExtra(GlobalVariables.senderId,remoteMessage.getData().get("senderId"));
                push.putExtra(GlobalVariables.receiverId,remoteMessage.getData().get("receiverId"));
                push.putExtra(GlobalVariables.name,remoteMessage.getData().get("name"));
                push.putExtra(GlobalVariables.profile,remoteMessage.getData().get("profile"));
                Log.e("3","3");
                NotificationUtils.clearNotifications(this);
          //      sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);

            }
            else if(remoteMessage.getData().get("type").equalsIgnoreCase("booking"))
            {

                Intent push = new Intent(this, RequestManagementActivity.class);
                sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);

            }
            else {

                Intent push = new Intent(this, NotificationActivity.class);
                push.putExtra("FCM", "Yes");
                Log.e("4","4");
                sendNotification((dataMap.get("title") == null ? "Boushra" : dataMap.get("title")), dataMap.get("body"), push);

            }

        }



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotification(String title, String body,Intent intent) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int notificationId = 1;

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.round_notify)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(uri);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_ONE_SHOT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());

    }
}

