package com.circles.circlesapp.helpers.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Interceptor which adds headers from shared preferences according to the added custom headers,
 * Authentication, languageCode and languageId headers by default.
 * <br>
 * when No-Authentication or Single-Language header is set to true add Authentication and multi
 * language headers from prefs
 */
public class MyServiceInterceptor implements Interceptor {

    public static String tokenType;
    public static String accessToken;
    public static String authentication;
    public static int userId;
    private Request.Builder requestBuilder;
    public static String scanFail;
    public MyServiceInterceptor() {
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        requestBuilder = request.newBuilder();
        Log.i("MyServiceInterceptor", "in MyServiceInterceptor " + request.headers().get("No-Authentication"));
        if (request.header("No-Authentication") == null || "false".equalsIgnoreCase(request.headers().get("No-Authentication"))) {
            addAuthenticationHeader();
            requestBuilder.removeHeader("No-Authentication");

        } else
            requestBuilder.removeHeader("No-Authentication");
        if (request.header("Single-Language") == null || "false".equalsIgnoreCase(request.headers().get("Single-Language"))) {
            requestBuilder.removeHeader("Single-Language");

        } else
            requestBuilder.removeHeader("Single-Language");

        return chain.proceed(requestBuilder.build());
    }

    private void addAuthenticationHeader() {
        if (accessToken != null) {
            requestBuilder.addHeader("Authorization", accessToken+tokenType);
        }
    }



    public static String getAuth() {
        return authentication;
    }
}