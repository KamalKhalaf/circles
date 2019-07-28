package com.circles.circlesapp.notifications;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.circles.circlesapp.Home;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.NotificationLayoutBinding;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity implements NotificationAdapter.NotificationListener,NotificationCallBack {
    private NotificationLayoutBinding layoutBinding;
    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;

    public static void start(Context context){
        Intent intent=new Intent(context,NotificationsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding=DataBindingUtil.setContentView(this, R.layout.notification_layout);
        adapter=new NotificationAdapter();
        adapter.setListener(this);
        layoutBinding.notificationRecyclerView.setAdapter(adapter);
        viewModel= ViewModelProviders.of(this).get(NotificationViewModel.class);
        viewModel.attachView(this);
        layoutBinding.setNotification(viewModel);
        viewModel.reqNotifications();
    }



    @Override
    public void onClick(NotificationModel item, int pos) {

    }

    @Override
    public void loadNotifications(ArrayList<NotificationModel> models) {
      adapter.setData(models);
    }

    @Override
    public NotificationsActivity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
