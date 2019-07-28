package com.circles.circlesapp.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.circles.circlesapp.helpers.livedata.AbsentLiveData;
import com.circles.circlesapp.helpers.livedata.Resource;

public class HomeViewMode extends ViewModel {
    public MutableLiveData<List<NewsFeedData>> mutableLDResp;
    public MutableLiveData<String> messageMLD;
    public MutableLiveData<Boolean> dataLoading;
    private HomeRepo repo;
    private LiveData<Resource<List<NewsFeedData>>> liveDataResp;
    private Observer<Resource<List<NewsFeedData>>> liveDataObserver;
    private LiveData<Resource<JsonElement>> postNewRecordLD;
    private Observer<Resource<JsonElement>> postNewRecordObserver;
    private LiveData<Resource<JsonElement>> likeOrDislikeLD;
    private LiveData<Resource<JsonElement>> deletePostLD;
    private LiveData<Resource<JsonElement>> updatePostLD;
    private Observer<Resource<JsonElement>> likeOrDislikeObserver;
    private Observer<Resource<JsonElement>> deletePostObserver;
    private Observer<Resource<JsonElement>> updatePostObserver;
    private LiveData<Resource<JsonElement>> sharePostLD;
    private Observer<Resource<JsonElement>> sharePostObserver;
    private LiveData<Resource<JsonElement>> addCommentLD;
    private Observer<Resource<JsonElement>> addCommentObserver;

    private LiveData<Resource<JsonElement>> hearVoiceLD;
    private Observer<Resource<JsonElement>> hearVoiceObserver;


    public HomeViewMode() {
        repo = new HomeRepo();
        dataLoading = new MutableLiveData<>();
        liveDataResp = AbsentLiveData.create();
        mutableLDResp = new MutableLiveData<>();
        liveDataObserver = getNewsFeedObserver();
        postNewRecordLD = AbsentLiveData.create();
        postNewRecordObserver = observeOnPostCreation();
        likeOrDislikeObserver = observeOnLikeOrDislikeApi();
        deletePostObserver = observeOnDeletePostApi();
        updatePostObserver = observeOnUpdatePostApi();
        sharePostObserver = observeOnShareApi();
        addCommentLD = AbsentLiveData.create();
        addCommentObserver = observeOnAddongNewComment();
        messageMLD = new MutableLiveData<>();
    }

    private Observer<Resource<JsonElement>> observeOnAddongNewComment() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            messageMLD.setValue("Comment Added Successfully");
                        break;
                    case ERROR:
                        messageMLD.setValue("failed to add new Comment");

                }
            }
        };
    }

    private Observer<Resource<JsonElement>> observeOnShareApi() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            break;
                    case ERROR:

                }
            }
        };
    }

    private Observer<Resource<JsonElement>> observeOnLikeOrDislikeApi() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            break;
                    case ERROR:
                }
            }
        };
    }

    private Observer<Resource<JsonElement>> observeOnPostCreation() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        break;
                    case SUCCESS:
                        if (resp.getData() != null) {
                            messageMLD.setValue("Post Added Successfully");
                            getNewsFeed();
                        } else
                            dataLoading.setValue(false);
                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        messageMLD.setValue("failed to add post");

                }
            }
        };
    }

    private Observer<Resource<JsonElement>> observeOnDeletePostApi() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        break;
                    case SUCCESS:
                        if (resp.getData() != null) {
                            messageMLD.setValue("Post deleted Successfully");
                            getNewsFeed();
                        } else
                            dataLoading.setValue(false);
                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        messageMLD.setValue("failed to delete post");

                }
            }
        };
    }

    private Observer<Resource<JsonElement>> observeOnUpdatePostApi() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        break;
                    case SUCCESS:
                        if (resp.getData() != null) {
                            messageMLD.setValue("Post updated Successfully");
                            getNewsFeed();
                        } else
                            dataLoading.setValue(false);
                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        messageMLD.setValue("failed to update post");

                }
            }
        };
    }

    private Observer<Resource<List<NewsFeedData>>> getNewsFeedObserver() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        dataLoading.setValue(true);
                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            mutableLDResp.setValue(resp.getData());
                        dataLoading.setValue(false);
                        break;
                    case ERROR:
                        dataLoading.setValue(false);
                        messageMLD.setValue("failed to get news feed");

                }
            }
        };
    }

    public void addPostRecord(String filePath, String text, ArrayList<String> images) {
        if (filePath == null && text == null) {
            messageMLD.setValue("failed to add post");

            return;
        }
        postNewRecordLD = repo.postNewRecord(filePath, text, images);
        postNewRecordLD.observeForever(postNewRecordObserver);
    }

    public void updatePost(int id ,String text, ArrayList<String> images) {
//        if (filePath == null && text == null) {
//            messageMLD.setValue("failed to add post");
//
//            return;
//        }
        updatePostLD = repo.updatePost(id ,text, images);
        updatePostLD.observeForever(updatePostObserver);
    }

    public void addComment(int postId, String text, ArrayList<String> images, String recordpath) {
        if (postId == 0){
            messageMLD.setValue("failed to add comment");
            return;
        }
        addCommentLD = repo.postNewComment(postId, recordpath, text, images);
        addCommentLD.observeForever(addCommentObserver);
    }

    public void onLikeBtnclicked(String postId) {
        likeOrDislikeLD = repo.likeOtDislike(postId);
        likeOrDislikeLD.observeForever(likeOrDislikeObserver);
    }

    public void getNewsFeed() {
        liveDataResp = repo.getNewsFeed();
        liveDataResp.observeForever(liveDataObserver);
    }

    public void onDeletePost(String postId) {
        deletePostLD = repo.deletePost(postId);
        deletePostLD.observeForever(deletePostObserver);
    }

    public void onShareBtnClicked(String postId) {
        sharePostLD = repo.sharePost(postId);
        sharePostLD.observeForever(sharePostObserver);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (liveDataResp != null)
            liveDataResp.removeObserver(liveDataObserver);
        if (postNewRecordLD != null)
            postNewRecordLD.removeObserver(postNewRecordObserver);
        if (likeOrDislikeLD != null)
            likeOrDislikeLD.removeObserver(likeOrDislikeObserver);
        if (deletePostLD != null)
            deletePostLD.removeObserver(deletePostObserver);
        if (updatePostLD != null)
            updatePostLD.removeObserver(updatePostObserver);
        if (sharePostLD != null)
            sharePostLD.removeObserver(sharePostObserver);
        if (addCommentLD != null)
            addCommentLD.removeObserver(addCommentObserver);
    }


    public void hearVoice(@NotNull String postId) {
     // hearVoiceLD = repo.hearTheVoice(postId);
    }

}
