package com.circles.circlesapp.helpers;
/**/

import android.content.Context;
import android.content.Intent;

import com.circles.circlesapp.Home;

public class BroadcastHelper {
    public static final String BROADCAST_EXTRA_METHOD_NAME = "INPUT_METHOD_CHANGED";
    public static final String BROADCAST_EXTRA_DATA = "EXTRA_DATA";
    public static final String BROADCAST_EXTRA_ANOTHERDATA = "EXTRA_ANOTHER_DATA";

    public static final String ACTION_NAME = "action_name";

    private static final String UPDATE_LOCATION_METHOD = "updateLocation";


    public static void sendInform(Context context, String method) {

        Intent intent = new Intent();

        intent.setAction(ACTION_NAME);

        intent.putExtra(BROADCAST_EXTRA_METHOD_NAME, method);

        try {

            context.sendBroadcast(intent);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void sendInform(Context context, String method , String data) {

        Intent intent = new Intent();

        intent.setAction(ACTION_NAME);

        intent.putExtra(BROADCAST_EXTRA_METHOD_NAME, method);
        intent.putExtra(BROADCAST_EXTRA_DATA, data);

        try {
            context.sendBroadcast(intent);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void sendInform(Context context, String method , String data , String anotherData) {

        Intent intent = new Intent();

        intent.setAction(ACTION_NAME);

        intent.putExtra(BROADCAST_EXTRA_METHOD_NAME, method);
        intent.putExtra(BROADCAST_EXTRA_DATA, data);
        intent.putExtra(BROADCAST_EXTRA_ANOTHERDATA, anotherData);

        try {

            context.sendBroadcast(intent);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    public static void sendInform(Context context, String method, Intent intent) {

        intent.setAction(ACTION_NAME);

        intent.putExtra(BROADCAST_EXTRA_METHOD_NAME, method);

        try {

            context.sendBroadcast(intent);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
