package com.circles.circlesapp.retrofit.api;

import android.arch.lifecycle.MutableLiveData;

public interface RequestListener<t> {
    void onResponse(MutableLiveData<t> data);

    void onEmpty(String msg);

    void onError(String msg);

    void onSessionExpired(String msg);

    void onNetWorkError(String msg);

}
