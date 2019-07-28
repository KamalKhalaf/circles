package com.circles.circlesapp.addgroup;

import android.support.annotation.NonNull;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import com.circles.circlesapp.helpers.core.ApiResponseCallBack;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.RetrofitClient;

class AddGroupRepo {

    void postNewGroup(AddGroupBody body, ApiResponseCallBack<String> resp) {
        AndroidNetworking.upload(RetrofitClient.BASE_URL + "createPublicGroup")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json ")
                .addMultipartFile("image", (body.getFileList().size() == 0) ? null : body.getFileList().get(0))
                .addMultipartParameter("longitude", body.getLocation().longitude + "")
                .addMultipartParameter("latitude", body.getLocation().latitude + "")
                .addMultipartParameter("name", body.getName())
                .addMultipartParameter("password", body.getPassword())
                .build()
                .getAsString( getParsedRequestListener(resp));
    }

    void postNewPrivateGroup(AddGroupBody body, ApiResponseCallBack<String> resp) {
        AndroidNetworking.upload(RetrofitClient.BASE_URL + "createPrivateGroup")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json ")
                .addMultipartFileList("covers[]", body.getFileList())
                .addMultipartParameter("longitude", body.getLocation().longitude + "")
                .addMultipartParameter("latitude", body.getLocation().latitude + "")
                .addMultipartParameter("name", body.getName())
                .addMultipartParameter("password", body.getPassword())
                .addMultipartParameter("type", "private")

                .build()
                .getAsString( getParsedRequestListener(resp));
    }

    void postNewEvent(AddGroupBody body, ApiResponseCallBack<String> resp) {
        AndroidNetworking.upload(RetrofitClient.BASE_URL + "createPrivateGroup")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json ")
                .addMultipartFileList("covers[]", body.getFileList())
                .addMultipartParameter("longitude", body.getLocation().longitude + "")
                .addMultipartParameter("latitude", body.getLocation().latitude + "")
                .addMultipartParameter("name", body.getName())
                .addMultipartParameter("type", "event")
                .addMultipartParameter("password",  body.getPassword())

                .build()
                .getAsString( getParsedRequestListener(resp));
    }

    @NonNull
    private StringRequestListener getParsedRequestListener(ApiResponseCallBack<String> resp) {
        return new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.d("postNewGroup", "onResponse: "+response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    AddGroupResponseModel m = new Gson().fromJson(jsonObject.getJSONObject("data").toString(),AddGroupResponseModel.class);
                    Log.d("postNewGroup", "onResponse: " + response);
                    resp.success(m.paypal_url);
                }catch (Exception s){
                    resp.success("");
                }

            }

            @Override
            public void onError(ANError anError) {
                AddGroupResponseModel responseModel = new Gson().fromJson(anError.getErrorBody(), AddGroupResponseModel.class);
                resp.fail(responseModel.message);
                Log.d("postNewGroup", "onResponse: " + anError.getErrorBody());
                Log.d("postNewGroup", "onResponse: " + anError.getErrorDetail());

            }
        };
    }
}
