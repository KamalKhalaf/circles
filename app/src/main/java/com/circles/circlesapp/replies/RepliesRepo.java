package com.circles.circlesapp.replies;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import com.circles.circlesapp.helpers.core.AppExecutors;
import com.circles.circlesapp.helpers.livedata.ApiResponse;
import com.circles.circlesapp.helpers.livedata.NetworkOnlyResource;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.RetrofitClient;

public class RepliesRepo {
    public LiveData<Resource<List<ReplayModel>>> getReplies(int commentId) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<List<ReplayModel>, ReplayModelResponse>(new AppExecutors()) {
            @Override
            protected List<ReplayModel> processResult(@Nullable ReplayModelResponse result) {
                if (result != null) {
                    return result.data;
                }
                return new ArrayList<>();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ReplayModelResponse>> createCall() {
                return api.getCommentReplies(MyServiceInterceptor.getAuth(),commentId);
            }

        }.asLiveData();

    }
}
