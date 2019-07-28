package com.circles.circlesapp.chatlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.circles.circlesapp.helpers.livedata.AbsentLiveData;
import com.circles.circlesapp.helpers.livedata.Resource;

public class ChatsViewModel extends ViewModel {
    private ChatsRepo repo;
    private LiveData<Resource<List<ChatRoom>>> liveDataResp;
    public MutableLiveData<List<ChatRoom>> mutableLDResp;
    private Observer<Resource<List<ChatRoom>>> liveDataObserver;

    public ChatsViewModel() {
        liveDataResp = AbsentLiveData.create();
        liveDataObserver = getNearbyObserver();
        mutableLDResp = new MutableLiveData<>();
        repo = new ChatsRepo();
    }

    private Observer<Resource<List<ChatRoom>>> getNearbyObserver() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            mutableLDResp.setValue(resp.getData());
                        break;
                    case ERROR:
                }
            }
        };
    }

    public void getChatRooms() {
        liveDataResp = repo.getChatRooms();
        liveDataResp.observeForever(liveDataObserver);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if (liveDataResp != null)
            liveDataResp.removeObserver(liveDataObserver);
    }
}
