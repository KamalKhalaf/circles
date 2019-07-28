package com.circles.circlesapp.group;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.RetrofitClient;

public class Repo {
    public void getGroupChatDetail(ApiResponseCallBack<GroupDetail> callBack, int id) {
        AndroidNetworking.post(RetrofitClient.BASE_URL + "getGroupChatDetails")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id", id + "")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            GroupDetail detail = new Gson().fromJson(data.toString(), GroupDetail.class);
                            callBack.success(detail);
                            Log.d("getGroupChatDetail", "onResponse: " + response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callBack.fail("could not get group data");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("getGroupChatDetail", "onError: " + anError.getErrorBody());
                        callBack.fail("could not get group data");
                    }
                });

    }
}
