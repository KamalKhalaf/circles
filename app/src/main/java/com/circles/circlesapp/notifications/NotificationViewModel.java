package com.circles.circlesapp.notifications;


import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.ResponseApi;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationViewModel<v extends NotificationCallBack> extends BaseViewModel<v> {
    private ObservableBoolean loader = new ObservableBoolean();
    private ObservableField<String> error = new ObservableField<>();
    private Api api;
    private Disposable mDisposable;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance().getApi();
    }

    public ObservableBoolean getLoader() {
        return loader;
    }

    public ObservableField<String> getError() {
        return error;
    }

    public void reqNotifications() {
        loader.set(true);
        error.set(null);
        // mDisposable=api.getNotifications(MyServiceInterceptor.userId,MyServiceInterceptor.getAuth())
        mDisposable = api.getNotifications(MyServiceInterceptor.userId, MyServiceInterceptor.getAuth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new Request<>(getView().getActivity(), new RequestListener<ResponseApi<ArrayList<NotificationModel>>>() {
                    @Override
                    public void onResponse(MutableLiveData<ResponseApi<ArrayList<NotificationModel>>> data) {
                        data.observe(getView().getActivity(), e -> {
                            loader.set(false);
                            if (e.getData() != null && e.getData().size() > 0) {
                                getView().loadNotifications(e.getData());
                            } else {
                                onEmpty("No Data Found");
                            }
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        loader.set(false);
                        error.set(msg);
                    }

                    @Override
                    public void onError(String msg) {
                        loader.set(false);
                        error.set(msg);
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        loader.set(false);
                        error.set(msg);
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        loader.set(false);
                        error.set(msg);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.dispose();
    }

    public void retry() {
        reqNotifications();
    }

    public void close() {
        getView().getActivity().onBackPressed();
    }
}
