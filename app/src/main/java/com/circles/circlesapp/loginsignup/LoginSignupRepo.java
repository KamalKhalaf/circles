package com.circles.circlesapp.loginsignup;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;

import com.circles.circlesapp.retrofit.RetrofitClient;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LoginSignupRepo {
    private PublishSubject publishSubject;
    private MutableLiveData<Boolean> mutableLiveData;

    @SuppressLint("CheckResult")
    public MutableLiveData<Boolean> checkUserName(String searchTerm) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        if (publishSubject == null)
            publishSubject = PublishSubject.create();
        publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(searchValue -> RetrofitClient.getInstance().getApi().checkUserName(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableObserver<CheckUserResponse>() {
                    @Override
                    public void onNext(CheckUserResponse response) {
                        mutableLiveData.postValue(response.isSuccess());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mutableLiveData.postValue(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return mutableLiveData;
    }
}
