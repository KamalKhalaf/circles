package com.circles.circlesapp.nearby;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import com.circles.circlesapp.helpers.livedata.AbsentLiveData;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.helpers.livedata.SingleLiveEvent;
import com.circles.circlesapp.retrofit.responses.GenericResponse;

public class NearbyViewModel extends ViewModel {
    private NearbyRepo repo;
    private LiveData<Resource<MapDetailResponse>> nearbyOnMap;
    public SingleLiveEvent<List<GroupObjectRespModel>> nearbyOnMapMutableLD;
    private Observer<Resource<MapDetailResponse>> nearbyOnMapObserver;
    private LiveData<Resource<GenericResponse<JoinGroupResponse>>> requestJoinGroupLD;
    private Observer<Resource<GenericResponse<JoinGroupResponse>>> requestJoinGroupObserver;
    public SingleLiveEvent<JoinGroupResponse> joinGroupMutableLD;
    private double longitude;
    private double latitude;
    private String groupName;

    public NearbyViewModel() {
        nearbyOnMap = AbsentLiveData.create();
        nearbyOnMapObserver = getNearbyObserver();
        nearbyOnMapMutableLD = new SingleLiveEvent<>();
        joinGroupMutableLD = new SingleLiveEvent<>();
        requestJoinGroupLD = AbsentLiveData.create();
        repo = new NearbyRepo();
        requestJoinGroupObserver=observeOnRequestJoinGroup();
    }

    private Observer<Resource<GenericResponse<JoinGroupResponse>>> observeOnRequestJoinGroup() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null) {
                            JoinGroupResponse groupResponse = resp.getData().data;
                            groupResponse.latitude=this.latitude;
                            groupResponse.longitude=this.longitude;
                            groupResponse.groupName=this.groupName;
                            joinGroupMutableLD.setValue(resp.getData().data);
                        }
                        break;
                    case ERROR:
                }
            }
        };
    }

    private Observer<Resource<MapDetailResponse>> getNearbyObserver() {
        return resp -> {
            if (resp != null) {
                switch (resp.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (resp.getData() != null)
                            nearbyOnMapMutableLD.setValue(resp.getData().data);
                        break;
                    case ERROR:
                }
            }
        };
    }

    public void getNearby(LatLng l) {
        nearbyOnMap = repo.getNearbyOnMap(l);
        nearbyOnMap.observeForever(nearbyOnMapObserver);
    }

    public void requestJoinGroup(JoinGroupReqBody l) {
        this.latitude=l.latitude;
        this.longitude=l.longitude;
        this.groupName=l.groupName;
        requestJoinGroupLD = repo.RequestJoinGroup(l);
        requestJoinGroupLD.observeForever(requestJoinGroupObserver);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if (nearbyOnMap != null)
            nearbyOnMap.removeObserver(nearbyOnMapObserver);

        if (requestJoinGroupLD != null)
            requestJoinGroupLD.removeObserver(requestJoinGroupObserver);
    }
}
