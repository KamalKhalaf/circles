package com.circles.circlesapp.nearby;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import com.circles.circlesapp.helpers.core.AppExecutors;
import com.circles.circlesapp.helpers.livedata.ApiResponse;
import com.circles.circlesapp.helpers.livedata.NetworkOnlyResource;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.responses.GenericResponse;

public class NearbyRepo {
    public LiveData<Resource<MapDetailResponse>> getNearbyOnMap(LatLng currentLoc) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<MapDetailResponse, MapDetailResponse>(new AppExecutors()) {
            @Override
            protected MapDetailResponse processResult(@Nullable MapDetailResponse result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MapDetailResponse>> createCall() {
                return api.getMapDetails(MyServiceInterceptor.getAuth(),currentLoc.longitude, currentLoc.latitude);
            }

        }.asLiveData();
    }

    public LiveData<Resource<GenericResponse<JoinGroupResponse>>> RequestJoinGroup(JoinGroupReqBody l) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<GenericResponse<JoinGroupResponse>, GenericResponse<JoinGroupResponse>>(new AppExecutors()) {
            @Override
            protected GenericResponse<JoinGroupResponse> processResult(@Nullable GenericResponse<JoinGroupResponse> result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenericResponse<JoinGroupResponse>>> createCall() {
                return api.joinGroup(MyServiceInterceptor.getAuth(),l.id, l.latitude,l.longitude,l.password);
            }

        }.asLiveData();
    }
}
