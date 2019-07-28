package com.circles.circlesapp.search;


import android.annotation.SuppressLint;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private static RxBus mRxBus;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    public static RxBus get(){
        if (mRxBus==null){
        synchronized (RxBus.class){
            mRxBus=new RxBus();
        }
    }
    return mRxBus;
    }

    private Subject<String> searchQuery;

    @SuppressLint("CheckResult")
    private Subject<String> getSearchQuery(){
        if (searchQuery==null){
           searchQuery= PublishSubject.create();
           searchQuery.observeOn(AndroidSchedulers.mainThread())
                   .subscribeOn(Schedulers.io());
        }
        return searchQuery;
    }

    public Disposable subscribeSearchQuery(Consumer<String> consumer){
        Disposable disposable=getSearchQuery().subscribe(consumer,e -> Log.d("RxBus",e.getMessage()));
        compositeDisposable.add(disposable);
        return disposable;
    }

    public void sendSearchQuery(String query){
        getSearchQuery().onNext(query);
    }

    public void unSubscribe(Disposable disposable){
        compositeDisposable.remove(disposable);
    }
}
