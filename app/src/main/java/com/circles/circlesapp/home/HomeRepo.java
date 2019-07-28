package com.circles.circlesapp.home;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.circles.circlesapp.helpers.core.AppExecutors;
import com.circles.circlesapp.helpers.livedata.ApiResponse;
import com.circles.circlesapp.helpers.livedata.NetworkOnlyResource;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class HomeRepo {

    LiveData<Resource<List<NewsFeedData>>> getNewsFeed() {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<List<NewsFeedData>, NewsFeedResponse>(new AppExecutors()) {
            @Override
            protected List<NewsFeedData> processResult(@Nullable NewsFeedResponse result) {
                if (result != null) {
                    return result.getData();
                }
                return new ArrayList<>();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NewsFeedResponse>> createCall() {
//                return api.getNewsfeed2(MyServiceInterceptor.getAuth() , 1);
                return api.getNewsfeed2(MyServiceInterceptor.getAuth());
            }
        }.asLiveData();
    }

    LiveData<Resource<JsonElement>> postNewRecord(String filePath, String text, ArrayList<String> images) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part record =
                MultipartBody.Part.createFormData("voice_note", file.getName(), requestFile);

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            File f = new File(images.get(i));
            RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            MultipartBody.Part image =
                    MultipartBody.Part.createFormData("image", f.getName(), img);
            parts.add(image);
        }
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {

            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return api.addPost2(MyServiceInterceptor.getAuth(), record, text, parts);
            }
        }.asLiveData();
    }


    LiveData<Resource<JsonElement>> updatePost(int id, String text, ArrayList<String> images) {

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            File f = new File(images.get(i));
            RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            MultipartBody.Part image =
                    MultipartBody.Part.createFormData("image", f.getName(), img);
            parts.add(image);
        }
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {

            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return api.updatePost(MyServiceInterceptor.getAuth(), id, text, parts);
            }
        }.asLiveData();
    }

    LiveData<Resource<JsonElement>> postNewComment(int postId, String filePath, String text, ArrayList<String> images) {

        File file = null;
        MultipartBody.Part record;

        try {
            file = new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.length() < 2) {
            record = null;
        } else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            record = MultipartBody.Part.createFormData("voice_note", file.getName(), requestFile);
        }

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            File f = new File(images.get(i));
            RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            MultipartBody.Part image =
                    MultipartBody.Part.createFormData("image", f.getName(), img);
            parts.add(image);
        }
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {

            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                if (record == null) {
                    return api.addComment(MyServiceInterceptor.getAuth(), postId, text, parts);
                } else
                    return api.addVoiceComment(MyServiceInterceptor.getAuth(), postId, record);
            }
        }.asLiveData();
    }

    LiveData<Resource<JsonElement>> likeOtDislike(String postId) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {

            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return api.likeAndDislikePost2(MyServiceInterceptor.getAuth(), postId);
            }
        }.asLiveData();
    }


    LiveData<Resource<JsonElement>> deletePost(String postId) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {

            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return api.deletePost(MyServiceInterceptor.getAuth(), postId);
            }
        }.asLiveData();
    }

    LiveData<Resource<JsonElement>> sharePost(String postId) {
        Api api = RetrofitClient.getInstance().getApi();
        return new NetworkOnlyResource<JsonElement, JsonElement>(new AppExecutors()) {

            @Override
            protected JsonElement processResult(@Nullable JsonElement result) {
                return result;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<JsonElement>> createCall() {
                return api.sharePost2(MyServiceInterceptor.getAuth(), postId);
            }
        }.asLiveData();
    }

    public Call<ApiResponse<JsonElement>> hearTheVoice(String postId) {
       Api api = RetrofitClient.getInstance().getApi();

       return api.hearVoice(MyServiceInterceptor.getAuth(),postId);
    }
}
