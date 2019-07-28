package com.circles.circlesapp.messaging.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.JsonElement;

import java.util.List;

import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.livedata.AbsentLiveData;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.messaging.model.Message;
import com.circles.circlesapp.messaging.model.MessageDetailsResponse;
import com.circles.circlesapp.messaging.model.SendMessageBody;
import com.circles.circlesapp.messaging.model.repository.MessagingRepository;

public class MessagesViewModel extends ViewModel {

    private MessagingRepository messagingRepository;
    public final MutableLiveData<Boolean> dataLoading;
    public MutableLiveData<List<Message>> messagesListItems;
    private LiveData<Resource<MessageDetailsResponse>> messageDataLiveData;
    private Observer<Resource<MessageDetailsResponse>> messageDataObserver;
    public MutableLiveData<String> newMessageContent;
    public MutableLiveData<String> toastMsg;
    public MutableLiveData<Boolean> mesSentSuccessfully;
    private LiveData<Resource<JsonElement>> sendMsgLiveData;
    private Observer<Resource<JsonElement>> sendMsgObserver;

    public MessagesViewModel() {
        messagingRepository = new MessagingRepository();
        dataLoading = new MutableLiveData<>();
        newMessageContent = new MutableLiveData<>();
        messagesListItems = new MutableLiveData<>();
        messageDataObserver = getMessageDataObserver();
        toastMsg = new MutableLiveData<>();
        sendMsgLiveData = AbsentLiveData.create();
        sendMsgObserver = ObserveOnSendingMsg();
        mesSentSuccessfully = new MutableLiveData<>();
    }

    private Observer<Resource<JsonElement>> ObserveOnSendingMsg() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        Log.d("MessageViewModel", "ObserveOnSendingMsg: " + "LOADING");

                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            mesSentSuccessfully.setValue(true);
                        Log.d("MessageViewModel", "ObserveOnSendingMsg: " + "true");
                        break;
                    case ERROR:
                        mesSentSuccessfully.setValue(false);
                        Log.d("MessageViewModel", "ObserveOnSendingMsg: " + "false");
                        break;
                }
            }
        };
    }

    public void loadMessages(int chatHistoryId) {
        messageDataLiveData = messagingRepository.getMessageDetails(chatHistoryId);
        messageDataLiveData.observeForever(messageDataObserver);
    }

    public void sendMessage(SendMessageBody body) {
        sendMsgLiveData = messagingRepository.sendMessage(body);
        sendMsgLiveData.observeForever(sendMsgObserver);
    }

    public void sendMessage2(SendMessageBody body) {
       messagingRepository.sendMessage(body, new ApiResponseCallBack<String>() {
            @Override
            public void success(String s) {
                mesSentSuccessfully.setValue(true);
            }

            @Override
            public void fail(String message) {
                mesSentSuccessfully.setValue(false);
            }
        });
    }


    public void sendMessage3(SendMessageBody body) {
        messagingRepository.sendMessage(body, new ApiResponseCallBack<String>() {
            @Override
            public void success(String s) {
                mesSentSuccessfully.setValue(true);
            }

            @Override
            public void fail(String message) {
                mesSentSuccessfully.setValue(false);
            }
        });
    }

    private Observer<Resource<MessageDetailsResponse>> getMessageDataObserver() {
        return getMessageDetailsResponseResource -> {
            if (getMessageDetailsResponseResource != null) {
                switch (getMessageDetailsResponseResource.getStatus()) {
                    case LOADING:

                        break;
                    case SUCCESS:
                        if (getMessageDetailsResponseResource.getData() != null)
                            messagesListItems.setValue(getMessageDetailsResponseResource.getData().getMessagesList());
                        break;
                    case ERROR:
                       // toastMsg.setValue("can not retreive messages");
                        break;
                }
            }
        };
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (messageDataLiveData != null)
            messageDataLiveData.removeObserver(messageDataObserver);

        if (sendMsgLiveData != null)
            sendMsgLiveData.removeObserver(sendMsgObserver);
    }
}
