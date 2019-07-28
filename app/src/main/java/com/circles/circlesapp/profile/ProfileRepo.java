package com.circles.circlesapp.profile;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.profile.model.FollowerList;
import com.circles.circlesapp.retrofit.RetrofitClient;

public class ProfileRepo {

    public void getUserDetails(ApiResponseCallBack<ProfileData> resp) {
        AndroidNetworking.post(RetrofitClient.BASE_URL + "getUserDetails")
                .addHeaders("Authorization",MyServiceInterceptor.getAuth())
                .build()

                .getAsObject(ProfileDataResponse.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        ProfileDataResponse r = (ProfileDataResponse) response;
                        Log.d("onResponse", "onResponse: "+response);

                        if (r.message.contains("Success"))
                            resp.success(r.data);
                        else
                            resp.fail(r.message);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("onError", "onError: "+anError);
                       resp.fail(anError.getErrorDetail());
                    }
                });
    }

    public void getUserFollower(ApiResponseCallBack<FollowerList> resp) {
        AndroidNetworking.post(RetrofitClient.BASE_URL + "getFollowedUsers")
                .addHeaders("Authorization",MyServiceInterceptor.getAuth())
                .addHeaders("Accept","application/json")
                .build()

                .getAsObject(FollowerList.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        FollowerList r = (FollowerList) response;
                        Log.d("onResponse", "onResponse: "+response);

                        if (r.getStatus().contains("Success"))
                            resp.success(r);
                        else
                            resp.fail(r.getStatus());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("onError", "onError: "+anError);
                        resp.fail(anError.getErrorDetail());
                    }
                });
    }

    public void getUserFollowing(ApiResponseCallBack<FollowerList> resp) {
        AndroidNetworking.post(RetrofitClient.BASE_URL + "getFollowingUsers")
                .addHeaders("Authorization",MyServiceInterceptor.getAuth())
                .addHeaders("Accept","application/json")
                .build()

                .getAsObject(FollowerList.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        FollowerList r = (FollowerList) response;
                        Log.d("onResponse", "onResponse: "+response);

                        if (r.getStatus().contains("Success"))
                            resp.success(r);
                        else
                            resp.fail(r.getStatus());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("onError", "onError: "+anError);
                        resp.fail(anError.getErrorDetail());
                    }
                });
    }

}
