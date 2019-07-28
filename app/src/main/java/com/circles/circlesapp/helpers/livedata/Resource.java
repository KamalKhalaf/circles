package com.circles.circlesapp.helpers.livedata;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import com.circles.circlesapp.helpers.App;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.utilities.NetworkUtils;

/**
 * Wrapper class to wrap any type of data that comes from the network or database and provides status values
 *
 * @param <T>
 */
public class Resource<T> {
    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    @NonNull
    private final Status status;

    @Nullable
    private final T data;

    @Nullable
    public final String message;

    private final int responseCode;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        if (status == Status.SUCCESS) {
            WeakReference<Context> contextWeakReference =
                    new WeakReference<>(App.get().getApplicationContext());
            if (!NetworkUtils.isNetworkAvailable(contextWeakReference.get())) {
                //cached
                this.message = contextWeakReference.get()
                        .getResources().getString(R.string.network_error);
                this.responseCode = 500;
            } else {
                this.message = message; //from network
                this.responseCode = 200;
            }

        } else if (status == Status.ERROR) { //status is error or loading
            this.message = message;
            this.responseCode = -1;
        } else {
            this.responseCode = 500;
            this.message = message;
        }
        this.status = status;
        this.data = data;
    }

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message, int responseCode) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.responseCode = responseCode;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Nullable
    public T getData() {
        return data;
    }


    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String errorMsg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, errorMsg);
    }

    public static <T> Resource<T> error(String errorMsg, int responseCode, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, errorMsg, responseCode);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }
}