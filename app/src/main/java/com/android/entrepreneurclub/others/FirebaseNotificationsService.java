package com.android.entrepreneurclub.others;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.entrepreneurclub.R;
import com.android.entrepreneurclub.fragment.FriendsFragment;
import com.android.entrepreneurclub.fragment.RequestsFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseNotificationsService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("payload :", "" + remoteMessage.getData());

        String titleNotif = remoteMessage.getData().get("title_notif");
        String contentNotif = remoteMessage.getData().get("body_notif");
        String from = remoteMessage.getData().get("from_user_id");
        String typeNotif = remoteMessage.getData().get("type");

        if(typeNotif.equals("request")){
        Intent i  = new Intent(this, RequestsFragment.class);
        i.putExtra("notif_frag","new_req");
        PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
        createNotification(titleNotif,contentNotif,resultingIntent);

        }
        else if(typeNotif.equals("confirmed")){
            Intent i = new Intent(this, RequestsFragment.class);
            String user1 = from;
            String user2 = remoteMessage.getData().get("to_user_id");
            i.putExtra("user1",user1);
            i.putExtra("user2",user2);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("declined")){
            Intent i = new Intent(this, FriendsFragment.class);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }



    }

    private void createNotification(String titleNotif, String contentNotif, PendingIntent resultPendingIntent) {

        Notification notification = new Notification();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setDefaults(notification.defaults);
        mBuilder.setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(titleNotif)
                .setContentText(contentNotif)
                .setAutoCancel(true)
                .setVibrate(new long[]{50, 350, 200, 350, 200})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }


}