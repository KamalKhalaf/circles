package com.circles.circlesapp.fcm.service;

import android.util.Log;

import com.circles.circlesapp.fcm.utils.NotificationUtils;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.notifications.NotificationBody;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

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
//        Log.d("onMessage", "here");
//        Log.d("remoteMessage", String.valueOf(remoteMessage.toIntent().putExtra("openNotifications",true)));
//        Log.d("remoteMessage", String.valueOf(remoteMessage.getData()));
//        String message = remoteMessage.getData().get("message");
//        Log.d(TAG, "onMessageReceived: "+message);


        Map<String, String> map = remoteMessage.getData();
//        Log.d("FireBaseData1", map.toString());
        if (map != null) {

            NotificationBody notificationBody = new NotificationBody();
            notificationBody.setMessage(remoteMessage.getData().get("message"));
            notificationBody.setType(remoteMessage.getData().get("type"));
            notificationBody.setNotification_data(remoteMessage.getData().get("notification_data"));
//            show(notificationBody);
            setupNotification(notificationBody);
//            NotificationResponse response = new NotificationResponse();
//            response.setTitle(map.get("title"));
//            response.setBody(map.get("body"));
//            handelActions(response);
        }


    }
    private void setupNotification(NotificationBody notificationTxt) {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.setIntent();
        notificationUtils.sendNotification(notificationTxt);
    }
}
