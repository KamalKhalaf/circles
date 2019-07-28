package com.circles.circlesapp.helpers.livedata;

import android.arch.lifecycle.LiveData;

/**
 * Helper class that creates empty live data object
 */
public class AbsentLiveData extends LiveData {
    @SuppressWarnings("unchecked")
    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}