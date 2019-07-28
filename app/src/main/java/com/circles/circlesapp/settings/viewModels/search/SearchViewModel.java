package com.circles.circlesapp.settings.viewModels.search;



import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.search.SearchResult;
import com.circles.circlesapp.settings.callBacks.SearchCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel<v extends SearchCallBack> extends BaseViewModel<v> {
    private ObservableBoolean loader = new ObservableBoolean();
    private ObservableField<String> error = new ObservableField<>();
    private Api api;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance().getApi();
    }

    public ObservableBoolean getLoader() {
        return loader;
    }

    public ObservableField<String> getError() {
        return error;
    }

    public void reqSearch(String query) {
        loader.set(true);
        error.set(null);
        api.search(MyServiceInterceptor.getAuth(), query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(arrayListResponseApi -> arrayListResponseApi.getData())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<ArrayList<SearchResult>>() {
                    @Override
                    public void onResponse(MutableLiveData<ArrayList<SearchResult>> data) {
                        data.observe(getView().getFragment(), e -> {
                            if (e.size() > 0) {
                                loader.set(false);
                                getView().loadData(e);
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
}
