package com.circles.circlesapp.replies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.circles.circlesapp.helpers.livedata.AbsentLiveData;
import com.circles.circlesapp.helpers.livedata.Resource;

public class RepliesViewModel extends ViewModel {
    public MutableLiveData<List<ReplayModel>> mutableLDResp;
    private RepliesRepo repo;
    private LiveData<Resource<List<ReplayModel>>> liveDataResp;
    private Observer<Resource<List<ReplayModel>>> liveDataObserver;

    public RepliesViewModel() {
        liveDataResp = AbsentLiveData.create();
        liveDataObserver = getNearbyObserver();
        mutableLDResp = new MutableLiveData<>();
        repo = new RepliesRepo();
    }

    private Observer<Resource<List<ReplayModel>>> getNearbyObserver() {
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

    public void getReplies(int commentId) {
        liveDataResp = repo.getReplies(commentId);
        liveDataResp.observeForever(liveDataObserver);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if (liveDataResp != null)
            liveDataResp.removeObserver(liveDataObserver);
    }
}
