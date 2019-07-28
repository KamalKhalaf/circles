package com.circles.circlesapp.chatlist;

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

public class ChatsRepo {
    LiveData<Resource<List<ChatRoom>>> getChatRooms() {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<List<ChatRoom>, UserMessagesResModel>(new AppExecutors()) {
            @Override
            protected List<ChatRoom> processResult(@Nullable UserMessagesResModel result) {
                if (result != null) {
                    return result.data;
                }
                return new ArrayList<>();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserMessagesResModel>> createCall() {
                return api.getChatList(MyServiceInterceptor.getAuth());
            }

        }.asLiveData();
    }
}
