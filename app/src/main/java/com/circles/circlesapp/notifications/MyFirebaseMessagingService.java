package com.circles.circlesapp.notifications;


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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.circles.circlesapp.R;
import com.circles.circlesapp.SplashScreen;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.settings.ui.SettingsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final int REQUEST_CODE = 27072;

    @Override
    public void onNewToken(String token) {
        Log.d("new_token", token);
        storeRegIdInPref(token);
    }

    private void storeRegIdInPref(String token) {
        SharedPrefHelper sharedPreferencesHelper = new SharedPrefHelper(this);
        sharedPreferencesHelper.setDeviceToken(token);
        Log.d("device_token: ", token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        Map<String, String> data = remoteMessage.getData();
//        String title = remoteMessage.getNotification().getTitle();
//        String message = remoteMessage.getNotification().getBody();
//        String imageUrl = (String) data.get("image");
//        String action = (String) data.get("action");
//        Log.i(TAG, "onMessageReceived: title : "+title);
//        Log.i(TAG, "onMessageReceived: message : "+message);
//        Log.i(TAG, "onMessageReceived: imageUrl : "+imageUrl);
//        Log.i(TAG, "onMessageReceived: action : "+action);


//        Log.d("FireBaseData", remoteMessage.toString());
//        Log.d("FireBaseData_data", remoteMessage.getData().toString());
        Map<String, String> map = remoteMessage.getData();
//        Log.d("FireBaseData1", map.toString());
        if (map != null) {

            NotificationBody notificationBody = new NotificationBody();
            notificationBody.setMessage(remoteMessage.getData().get("message"));
            notificationBody.setType(remoteMessage.getData().get("type"));
            notificationBody.setNotification_data(remoteMessage.getData().get("notification_data"));
            show(notificationBody);

//            NotificationResponse response = new NotificationResponse();
//            response.setTitle(map.get("title"));
//            response.setBody(map.get("body"));
//            handelActions(response);
        }

    }

    private void handelActions(NotificationResponse response) {
        showNotifcation(response, new Intent(this, SettingsActivity.class));
    }

    private void show(NotificationBody notificationBody) {

        Intent cartIntent = new Intent(this, SplashScreen.class);
        cartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        cartIntent.putExtra("intent_type", BUNDLE_KEY_IS_PROCEED_TO_CHECKOUT);
//        Log.d(ApplicationConstant.TAG, "Inside m");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, cartIntent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "abandonedCart")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(notificationBody.getMessage())
                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setVibrate(new long[] {2000})
                .setChannelId(String.valueOf(REQUEST_CODE))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.app_name))
                .setBigContentTitle(notificationBody.getMessage()));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(String.valueOf(REQUEST_CODE), getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(34555, notificationBuilder.build());

    }

    private void showNotifcation(NotificationResponse response, Intent intent) {
        NotificationCompat.Builder builder;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, "first");

            manager.createNotificationChannel(new NotificationChannel("first", "first", NotificationManager.IMPORTANCE_HIGH));
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(response.getTitle())
                .setAutoCancel(true)
                .setContentText(response.getBody())
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent);
        manager.notify(getNum(), builder.build());
    }

    private int getNum() {
        Random rand = new Random();
        return rand.nextInt(10000) * 100;
    }

}
