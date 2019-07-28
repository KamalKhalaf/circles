package com.circles.circlesapp.messaging.model.repository;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.circles.circlesapp.helpers.App;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.core.AppExecutors;
import com.circles.circlesapp.helpers.livedata.ApiResponse;
import com.circles.circlesapp.helpers.livedata.NetworkOnlyResource;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.messaging.model.Message;
import com.circles.circlesapp.messaging.model.MessageDetailsResponse;
import com.circles.circlesapp.messaging.model.SendMessageBody;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MessagingRepository {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public LiveData<Resource<MessageDetailsResponse>> getMessageDetails(int chatHistoryId) {

        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<MessageDetailsResponse, MessageDetailsResponse>(new AppExecutors()) {
            @Override
            protected MessageDetailsResponse processResult(@Nullable MessageDetailsResponse result) {
                if (result == null) return result;
                List<Message> messagesList = result.getMessagesList();
                if (messagesList != null && messagesList.size() > 0) {
                    List<Message> reversedMsgList = new ArrayList<>();
                    for (int i = messagesList.size() - 1; i >= 0; i--) {
                        reversedMsgList.add(messagesList.get(i));
                    }
                    result.setMessagesList(reversedMsgList);
                }
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MessageDetailsResponse>> createCall() {
                return api.getChatHistoryDetails(MyServiceInterceptor.getAuth(), chatHistoryId);
            }
        }.asLiveData();

    }

    public LiveData<Resource<JsonElement>> sendMessage(SendMessageBody body) {
//        List<MultipartBody.Part> parts = new ArrayList<>();
//        File file = new File(body.getFileList().get(0).getAbsolutePath());
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part record =
//                MultipartBody.Part.createFormData("message_files[]", file.getName(), requestFile);
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < body.getFileList().size(); i++) {
            File f = body.getFileList().get(i);
            RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            MultipartBody.Part image =
                    MultipartBody.Part.createFormData("message_files[]", f.getName(), img);
            parts.add(image);
        }
        Log.d("sendMessage", "sendMessage: " + body.toString());
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {
            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return api.sendMessage(MyServiceInterceptor.getAuth(), body.getId()
                        , body.getLatitude(),
                        body.getLongitude(),
                        body.getMessageFileType(),
                        body.getMessageBody(),
                        body.getMessageType(),
                        parts);
            }
        }.asLiveData();

    }

    private String removeDoubleQuotesIfExist(String s) {
       /* if (s == null) return s;
        if (s.charAt(0) == '"') {
            String replace = s.replace('"', ' ');
            return replace;
        }*/
        return s;
    }

    public void sendMessage(SendMessageBody body, ApiResponseCallBack<String> callBack) {
        if (body.getFileList().size() > 0) {
            Log.d("sendMessage", "sendMessage: " + body.toString());
            notificationBuilder = new NotificationCompat.Builder(App.get(), "uploadcircle")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(App.get().getString(R.string.Uploading))
                    .setContentText(App.get().getString(R.string.uploadinprogress))
                    .setChannelId(String.valueOf(34468))
                    .setProgress(100, 0, false)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_LOW);
            NotificationManager mNotificationManager =
                    (NotificationManager) App.get().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(String.valueOf(34468), App.get().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }
            notificationManager = (NotificationManager) App.get().getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(notificationManager).notify(34455, notificationBuilder.build());

            AndroidNetworking.upload(RetrofitClient.BASE_URL + "sendMessage")
                    .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                    .addHeaders("Accept", "application/json")
                    .addMultipartParameter("message_body", removeDoubleQuotesIfExist(body.getMessageBody()))
                    .addMultipartParameter("type", removeDoubleQuotesIfExist(body.getMessageType()))
                    .addMultipartParameter("id", body.getId() + "")
                    .addMultipartParameter("latitude", body.getLatitude() + "")
                    .addMultipartParameter("longitude", body.getLongitude() + "")
                    .addMultipartParameter("message_files_type", removeDoubleQuotesIfExist(body.getMessageFileType()))
                    .addMultipartFileList("message_files[]", body.getFileList())
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                        int percent = (int) ((bytesUploaded * 100) / totalBytes);

                        notificationBuilder.setProgress(100, percent, false)
                                .setContentText(String.valueOf(percent) + " %")
                                .setOngoing(true);
                        notificationManager.notify(34455, notificationBuilder.build());

                    })
                    .getAsString(getRequestListener(callBack));
        } else
            AndroidNetworking.upload(RetrofitClient.BASE_URL + "sendMessage")
                    .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                    .addHeaders("Accept", "application/json")
                    .addMultipartParameter("message_body", removeDoubleQuotesIfExist(body.getMessageBody()))
                    .addMultipartParameter("type", removeDoubleQuotesIfExist(body.getMessageType()))
                    .addMultipartParameter("id", body.getId() + "")
                    .addMultipartParameter("latitude", body.getLatitude() + "")
                    .addMultipartParameter("longitude", body.getLongitude() + "")
                    .build()
                    .getAsString(getRequestListener2(callBack));

    }

    @NonNull
    private StringRequestListener getRequestListener(ApiResponseCallBack<String> callBack) {
        return new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                notificationBuilder.setContentText(App.get().getString(R.string.uploadcompleted))
                        .setOngoing(false)
                        .setProgress(0, 0, false);
                notificationManager.notify(34455, notificationBuilder.build());
                callBack.success(response);
            }

            @Override
            public void onError(ANError anError) {
                notificationBuilder.setContentText(App.get().getString(R.string.uploadfailed))
                        .setOngoing(false)
                        .setProgress(0, 0, false);
                notificationManager.notify(34455, notificationBuilder.build());
                callBack.fail(anError.getErrorDetail());
            }
        };
    }

    @NonNull
    private StringRequestListener getRequestListener2(ApiResponseCallBack<String> callBack) {
        return new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.d("sendMessage", "onResponse: " + response);
                callBack.success(response);
            }

            @Override
            public void onError(ANError anError) {
                Log.d("sendMessage", "anError: " + anError.getErrorDetail());
                Log.d("sendMessage", "anError: " + anError.getErrorBody());

                callBack.fail(anError.getErrorDetail());
            }
        };
    }
}
