package com.circles.circlesapp.fcm.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.circles.circlesapp.Home;
import com.circles.circlesapp.R;
import com.circles.circlesapp.fcm.constants.Config;
import com.circles.circlesapp.notifications.NotificationBody;
import com.circles.circlesapp.notifications.NotificationsActivity;


public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();
    private final String CHANNEL_ID = "3117";
    private Context context;
    private Intent intent;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "circles";
            String description = "show circles notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void sendNotification(NotificationBody notificationTxt) {



        createNotificationChannel();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Home.class);
        stackBuilder.addNextIntentWithParentStack(intent);
        stackBuilder.editIntentAt(0).putExtra(Config.PUSH_NOTIFICATION, true);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Circles")
                .setContentText(notificationTxt.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(2, notificationBuilder.build());
        }
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent() {
        this.intent = generateIntent();
    }

    public Intent generateIntent() {
        intent = new Intent(context, NotificationsActivity.class);
        return intent;
    }

}