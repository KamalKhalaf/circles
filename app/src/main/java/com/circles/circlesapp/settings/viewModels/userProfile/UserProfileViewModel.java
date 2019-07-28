package com.circles.circlesapp.settings.viewModels.userProfile;



import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.profile.UserInfo;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.ResponseApi;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.settings.callBacks.UserProfileCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserProfileViewModel<v extends UserProfileCallBack> extends BaseViewModel<v> implements UserProfileVmodel<v> {
    private Api api;
    private ObservableField<UserInfo> userInfo = new ObservableField<>();
    private ObservableField<String> error = new ObservableField<>();
    private ObservableBoolean loader = new ObservableBoolean();
    private ObservableBoolean loaderFollow = new ObservableBoolean();

    private int userId;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance().getApi();

    }

    public ObservableField<UserInfo> getUserInfo() {
        return userInfo;
    }

    @BindingAdapter("userImage")
    public static void userImage(CircleImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.sign_up_profile))
                .into(imageView);
    }

    public ObservableBoolean getLoaderFollow() {
        return loaderFollow;
    }

    public ObservableField<String> getError() {
        return error;
    }

    public void setError(String error) {
        this.error.set(error);
    }

    public ObservableBoolean getLoader() {
        return loader;
    }

    public void setLoader(boolean loader) {
        this.loader.set(loader);
    }

    @Override
    public void reqUserProfile(int userId) {
        this.userId = userId;
        api.getUserDetails(MyServiceInterceptor.getAuth(), userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(profileDataResponseApi ->
                        profileDataResponseApi.getData())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<UserInfo>() {
                    @Override
                    public void onResponse(MutableLiveData<UserInfo> data) {
                        data.observe(getView().getFragment(), e -> {
                            if (e == null) return;
                            Log.d("gfgfvffg", e.toString());
                            userInfo.set(e);
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {

                    }

                    @Override
                    public void onError(String msg) {

                    }

                    @Override
                    public void onSessionExpired(String msg) {

                    }

                    @Override
                    public void onNetWorkError(String msg) {

                    }
                }));

    }

    public void reqFollowOrUnFollow() {
        loaderFollow.set(true);
        api.followOrUnFollow(MyServiceInterceptor.getAuth(), userInfo.get().getUsername() , MyServiceInterceptor.userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<ResponseApi>() {
                    @Override
                    public void onResponse(MutableLiveData<ResponseApi> data) {
                        data.observe(getView().getFragment(), e -> {
                            loaderFollow.set(false);
                            userInfo.get().setFollow(!userInfo.get().isFollow());
                            Toast.makeText(getView().getFragment().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        loaderFollow.set(false);
                        getView().onError(msg);
                    }

                    @Override
                    public void onError(String msg) {
                        loaderFollow.set(false);
                        getView().onError(msg);
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        loaderFollow.set(false);
                        getView().onError(msg);
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        loaderFollow.set(false);
                        getView().onError(msg);
                    }
                }));
    }

    @Override
    public void follow(View view) {
        reqFollowOrUnFollow();
    }

    @Override
    public void retry(View view) {
        reqUserProfile(userId);
        getView().reqPosts();
    }
}
