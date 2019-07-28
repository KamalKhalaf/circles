package com.circles.circlesapp.comments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.circles.circlesapp.helpers.livedata.AbsentLiveData;
import com.circles.circlesapp.helpers.livedata.Resource;

public class CommentsViewModel extends ViewModel {
    public MutableLiveData<List<PostCommentMode>> mutableLDResp;
    private CommentsRepo repo;
    private LiveData<Resource<List<PostCommentMode>>> liveDataResp;
    private Observer<Resource<List<PostCommentMode>>> liveDataObserver;

    public CommentsViewModel() {
        liveDataResp = AbsentLiveData.create();
        liveDataObserver = getNearbyObserver();
        mutableLDResp = new MutableLiveData<>();
        repo = new CommentsRepo();
    }

    private Observer<Resource<List<PostCommentMode>>> getNearbyObserver() {
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

    public void getPostComments(int postId) {
        liveDataResp = repo.getPostComments(postId);
        liveDataResp.observeForever(liveDataObserver);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if (liveDataResp != null)
            liveDataResp.removeObserver(liveDataObserver);
    }
}
