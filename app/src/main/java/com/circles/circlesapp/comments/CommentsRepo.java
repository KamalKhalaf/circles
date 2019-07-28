package com.circles.circlesapp.comments;

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

public class CommentsRepo {
    public LiveData<Resource<List<PostCommentMode>>> getPostComments(int postId) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<List<PostCommentMode>, PostCommentResponse>(new AppExecutors()) {
            @Override
            protected List<PostCommentMode> processResult(@Nullable PostCommentResponse result) {
                if (result != null) {
                    return result.data;
                }
                return new ArrayList<>();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PostCommentResponse>> createCall() {
                return api.getPostComments(MyServiceInterceptor.getAuth(),postId);
            }

        }.asLiveData();

    }
}
