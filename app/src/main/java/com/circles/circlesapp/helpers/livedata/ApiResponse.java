package com.circles.circlesapp.helpers.livedata;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

import com.circles.circlesapp.helpers.App;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.utilities.NetworkUtils;
import retrofit2.Response;

/**
 * Wrapper helper class that wraps the result of the network calls
 * </br>
 * Provides easy access to response body and code
 *
 * @param <T>
 */
public class ApiResponse<T> {

    private static final String TAG = "ApiResponse";
    public final int code;
    @Nullable
    public final T body;

    @Nullable
    private final String errorMessage;

    private final boolean isSuccessful;

    private WeakReference<Context> contextWeakReference = new
            WeakReference<>(App.get().getApplicationContext());

    private Resources appResources = contextWeakReference.get().getResources();


    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        isSuccessful = false;
        Log.e(TAG, error.getMessage(), error);
        if (contextWeakReference.get() != null &&
                !NetworkUtils.isNetworkAvailable(contextWeakReference.get())) {
            errorMessage = appResources.getString(R.string.network_error);
        } else
            errorMessage = "Could not get any response from server";
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
            isSuccessful = true;
        } else {
            isSuccessful = false;
            body = null;

            if (contextWeakReference.get() != null) {
                switch (response.code()) {
                    case 401:
                        errorMessage = appResources.getString(R.string.invalid_credentials);
                        break;
                    default:
                        errorMessage = appResources.getString(R.string.general_error);

                }
            } else
                errorMessage = "Something happened, Please try again later";
        }
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

}