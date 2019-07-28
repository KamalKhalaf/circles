package com.circles.circlesapp.notifications;

/**/

import com.circles.circlesapp.settings.callBacks.BaseCallBack;

import java.util.ArrayList;


public interface NotificationCallBack extends BaseCallBack {
    void loadNotifications(ArrayList<NotificationModel> models);

    NotificationsActivity getActivity();
}
